package com.example.cache;

import android.content.Context;
import android.os.Handler;

import java.io.File;

public class LimpiarCache {

    public static void clearCache(Context context) {

        try{
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static void cleanCache(Context context){
        clearCache(context); // Limpiar caché
        clearMemory(context); // Limpiar memoria

        // No toques la base de datos, para mantener los datos.


    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static void clearMemory(final Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.gc();  // Llama al recolector de basura para limpiar la memoria.
            }
        }, 1000);
    }


}
