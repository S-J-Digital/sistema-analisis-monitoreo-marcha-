package com.example.getsensores;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Spinner;

import androidx.core.app.ActivityCompat;

import java.util.List;

public class SeleccionSensores {
    private SeleccionSensores() {
        throw new IllegalStateException("Utility class");
    }

    public static int seleccionAcele(Spinner acel) {
        String config = (String) acel.getSelectedItem();
        int acele = 0;

        switch (config) {
            case "2g":
                acele = 2;
                break;
            case "4g":
                acele = 4;
                break;
            case "8g":
                acele = 8;
                break;
            case "16g":
                acele = 16;
                break;
            default:
                break;
        }

        return acele;
    }

    public static int seleccionGiros(Spinner giros) {
        String config = (String) giros.getSelectedItem();
        int girosc = 0;

        switch (config) {
            case "250 degrees/sec":
                girosc = 250;
                break;
            case "500 degrees/sec":
                girosc = 500;
                break;
            case "1000 degrees/sec":
                girosc = 1000;
                break;
            case "2000 degrees/sec":
                girosc = 2000;
                break;
            default:
                break;
        }

        return girosc;
    }

    public static int seleccionFrecuencia(Spinner frecuencia) {
        String config = (String) frecuencia.getSelectedItem();
        int srd = 0;

        switch (config) {
            case "0":
                srd = 0;
                break;
            case "1":
                srd = 1;
                break;
            case "2":
                srd = 2;
                break;
            case "3":
                srd = 3;
                break;
            case "4":
                srd = 4;
                break;
            default:
                break;
        }

        return srd;
    }

    public static int seleccionTiempo(Spinner tiempo) {
        String time = (String) tiempo.getSelectedItem();
        int t = 0;

        switch (time) {
            case "30 seg":
                t = 30;
                break;
            case "40 seg":
                t = 40;
                break;
            case "50 seg":
                t = 50;
                break;
            case "60 seg":
                t = 60;
                break;
            default:
                break;
        }

        return t;
    }

    public static String seleccionDispositivo(Spinner disp, Context context) {
        String dispositivo = (String) disp.getSelectedItem();
        String dispo = null;

        switch (dispositivo) {
            case "Joy-Con":
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return dispo;
                }
                dispo = nombreDispositivo(context).getAddress();
                break;
            case "Dispositivo Móvil":
                dispo = "Dispositivo Móvil";
                break;
            default:
                break;
        }

        return dispo;
    }

    public static BluetoothDevice nombreDispositivo(Context context) {
        BluetoothAdapter bladapter = null;
        BluetoothDevice joycon = null;
        List<BluetoothDevice> devices = null;
        String nombre = null;

       try{
           if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
               // TODO: Consider calling
               //    ActivityCompat#requestPermissions
               // here to request the missing permissions, and then overriding
               //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
               //                                          int[] grantResults)
               // to handle the case where the user grants the permission. See the documentation
               // for ActivityCompat#requestPermissions for more details.
               return joycon;
           }
           BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
           if (bluetoothManager != null) {
               bladapter = bluetoothManager.getAdapter();

           } else {
               bladapter = null;
           }
       }catch (Exception e) {
           BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
           if (bluetoothManager != null) {
               bladapter = bluetoothManager.getAdapter();
               Log.e("Error", e.getMessage());
               Log.e("Error", bladapter.getBondedDevices().toString());
           }
       }

        for(BluetoothDevice device : bladapter.getBondedDevices()){
            nombre = device.getName();
            if( nombre != null && nombre.toLowerCase().contains("joy-con")){
                joycon = device;
            }
        }
        return joycon;
    }
}
