package com.example.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import java.io.File;

public class UninstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_FULLY_REMOVED)) {
            // Eliminar la base de datos
            context.deleteDatabase("sensores_inteligentes.db");

            // Eliminar archivos en el almacenamiento interno
            File dir = context.getFilesDir();
            deleteRecursive(dir);

            // Eliminar archivos en el almacenamiento externo
            File externalDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (externalDir != null) {
                deleteRecursive(externalDir);
            }
        }
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }
}
