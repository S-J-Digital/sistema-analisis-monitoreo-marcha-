package com.example.funcionalidadesbluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.model.Sennales;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BluetoothJoyCon {
    private static final String TAG = "BluetoothJoyCon";
    private static final int PSM_CONTROL = 0x11;
    private static final int PSM_INTERRUPT = 0x13;

    private BluetoothSocket controlSocket;
    private BluetoothSocket interrupSocket;
    private OutputStream controlOut;
    private InputStream interrupIn;

    private JoyConListener listener;

    private int packetCounter = 0;
    private boolean conectado = false;
    private boolean escuchando = false;

    BluetoothAdapter bladapter;
    BluetoothDevice joycon;

    public void conectar(Context context, String bldevice) {
        new Thread(() -> {
            try {
                BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
                if (bluetoothManager != null) {
                    bladapter = bluetoothManager.getAdapter();
                    joycon = bladapter.getRemoteDevice(bldevice);
                }

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    controlSocket = joycon.createL2capChannel(PSM_CONTROL);
                    interrupSocket = joycon.createL2capChannel(PSM_INTERRUPT);
                }
                controlSocket.connect();
                interrupSocket.connect();

                controlOut = controlSocket.getOutputStream();
                interrupIn = interrupSocket.getInputStream();

                conectado = true;
                Log.d(TAG, "Joy-Con conectado");

                if(listener != null){
                    listener.onConectado();
                }

                Thread.sleep(300);
                enableVibration();
                Thread.sleep(100);
                enableIMU();
                Thread.sleep(100);
                setIMUSensitivity();
                Thread.sleep(100);
                setModoReporte();

                escucharDatos();

            }catch (IOException | InterruptedException e){
                Log.e(TAG, "Error al conectar " + e.getMessage());
                notificarError("Error al conectar " +e.getMessage());
                desconectar();
            }
        }).start();
    }

    private void enableVibration() throws IOException, InterruptedException {
        // Enviar comando para habilitar el IMU del Joy-Con
        byte[] enableIMUCommand = new byte[]{0x01,
                0x01,
                (byte) (packetCounter++ & 0x0F),
                0x00, 0x01, 0x40, 0x40,
                0x00, 0x01, 0x40, 0x40,
                0x48, 0x01
        }; // Comando específico para habilitar el IMU
        controlOut.write(enableIMUCommand);
        controlOut.flush();
        esperarRespuesta();
        Log.d(TAG,"IMU activado");
    }
    private void enableIMU() throws IOException, InterruptedException {
        // Enviar comando para habilitar el IMU del Joy-Con
        byte[] enableIMUCommand = new byte[]{0x01,
                0x00,
                (byte) (packetCounter++ & 0x0F),
                0x00, 0x01, 0x40, 0x40,
                0x00, 0x01, 0x40, 0x40,
                0x40, 0x01
        }; // Comando específico para habilitar el IMU
       controlOut.write(enableIMUCommand);
       controlOut.flush();
       esperarRespuesta();
       Log.d(TAG,"IMU activado");
    }

    private void setIMUSensitivity() throws IOException, InterruptedException {
        // Enviar comando para habilitar el IMU del Joy-Con
        byte[] enableIMUCommand = new byte[]{0x01,
                0x01,
                (byte) (packetCounter++ & 0x0F),
                0x00, 0x01, 0x40, 0x40,
                0x00, 0x01, 0x40, 0x40,
                0x41, 0x03,0x00,0x01,0x01
        }; // Comando específico para habilitar el IMU
        controlOut.write(enableIMUCommand);
        controlOut.flush();
        esperarRespuesta();
        Log.d(TAG,"IMU activado");
    }
    private void setModoReporte() throws IOException, InterruptedException {
        // Enviar comando para habilitar el IMU del Joy-Con
        byte[] enableIMUCommand = new byte[]{
                0x01,
                (byte) (packetCounter++ & 0x0F),
                0x00, 0x01, 0x40, 0x40,
                0x00, 0x01, 0x40, 0x40,
                0x03, 0x30
        }; // Comando específico para habilitar el IMU
        controlOut.write(enableIMUCommand);
        controlOut.flush();
        esperarRespuesta();
        Log.d(TAG,"Modo 0x30 activado");
    }

    private void escucharDatos(){
        escuchando = true;
        byte[] buffer = new byte[64];

        while(conectado && escuchando){
            try{
                int byteleidos = interrupIn.read(buffer);

                if(byteleidos >= 49 && (buffer[0] & 0xff) == 0x30){
                    Sennales sennales = parsearComoSennales(buffer);
                    if(listener != null){
                        listener.onSennalRecibida(sennales);
                    }
                }

            } catch (IOException e) {
                if(escuchando){
                    notificarError("Conexión perdida");
                    desconectar();
                }
                break;
            }
        }

    }

    private void esperarRespuesta() throws IOException, InterruptedException {
        byte[] buffer = new byte[64];
        Long timeout = System.currentTimeMillis() +1000;
        while(System.currentTimeMillis() < timeout){
            if(interrupIn.available() > 0){
                int bytesLeidos = interrupIn.read(buffer);
                Log.d(TAG, "ACK recibido: 0x" + String.format("%02X", buffer[0] & 0xFF));
                if((buffer[0] & 0xFF)==0x21){
                    return;
                }
            }
            Thread.sleep(10);
        }
        Log.d(TAG, "Timeout esperando respuesta del JoyCon");
    }

    private Sennales parsearComoSennales(byte[] data, int offset){
        
            //Acelerometro - bytes 13-18(little-endian)
            short rawAcelX = (short) ((data[offset+1] << 8) | (data[13] & 0xFF));
            short rawAcelY = (short) ((data[offset+3] << 8) | (data[offset+2] & 0xFF));
            short rawAcelZ = (short) ((data[offset+5] << 8) | (data[offset+4] & 0xFF));

            //Acelerometro - bytes 19-24(little-endian)
            short rawGyroX = (short) ((data[offset+7] << 8) | (data[offset+6] & 0xFF));
            short rawGyroY = (short) ((data[offset+9] << 8) | (data[offset+8] & 0xFF));
            short rawGyroZ = (short) ((data[offset+11] << 8) | (data[offset+10] & 0xFF));

            //Convertir a unidades reales
            //Acelerometro: raw * G-rango / 16384 /1000
            double acelX = rawAcelX * 8.0 /16384.0 /1000.0;
            double acelY = rawAcelY *  8.0 / 16384.0 / 1000.0;
            double acelZ = rawAcelZ *  8.0 / 16384.0 / 1000.0;

            //Acelerometro: 1 LSB = 0.06103 g
            double gyroX = rawGyroX * 0.06103;
            double gyroY = rawGyroY * 0.06103;
            double gyriZ = rawGyroZ * 0.06103;

            String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            //Crear Señales
            return new Sennales(0, fecha,acelX,acelY,acelZ,
                    gyroX,gyroY,gyriZ,
                    0.0,0.0,0.0,
                    0.0,8,2000,0);
    }

    public void desconectar(){
        escuchando = false;
        conectado = false;
        try{
            if(controlOut != null){
                controlOut.close();
            }
            if(interrupIn != null){
                interrupIn.close();
            }
            if(controlSocket != null){
                controlSocket.close();
            }
            if(interrupSocket != null){
                interrupSocket.close();
            }
        }catch (IOException e){
            Log.e(TAG,"Error al desconectar " + e.getMessage());
        }
    }

    public boolean isConectado(){
        return conectado;
    }

    private void notificarError(String msg){
        if(listener != null){
            listener.onError(msg);
        }
    }
}
