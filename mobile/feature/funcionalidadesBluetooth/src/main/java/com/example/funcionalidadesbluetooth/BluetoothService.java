package com.example.funcionalidadesbluetooth;

import static androidx.core.content.ContextCompat.startActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

public class BluetoothService {
    private BluetoothAdapter bladapter;

    public void bluetoothEncendido(Context context){
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            bladapter = bluetoothManager.getAdapter();
        } else {
            bladapter = null;
        }

        if (bladapter == null) {
            Toast.makeText(context, "Bluetooth no es compatible con este dispositivo", Toast.LENGTH_SHORT).show();
        } else {
            if (bladapter.isEnabled()) {
                bladapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {
                    @Override
                    public void onServiceConnected(int profile, BluetoothProfile proxy) {
                        if (profile == BluetoothProfile.HEADSET) {
                            if (proxy.getConnectedDevices().size() > 0) {
                               Toast.makeText(context, "Bluetooth está encendido y conectado", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Redirigiendo a ajustes de Bluetooth...", Toast.LENGTH_LONG).show();

                                // 3. Abrir la pantalla específica de configuración de Bluetooth
                                Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                            bladapter.closeProfileProxy(BluetoothProfile.HEADSET, proxy);
                        }
                    }
                    @Override
                    public void onServiceDisconnected(int profile) {
                        // No hacer nada
                    }
                }, BluetoothProfile.HEADSET);
            } else {
                Toast.makeText(context, "Bluetooth está apagado. Redirigiendo a ajustes de Bluetooth...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}
