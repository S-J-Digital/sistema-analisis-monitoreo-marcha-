package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class bd_connection extends SQLiteOpenHelper {
    private static final String BD_NAME = "sensores_inteligentes.db";
    //private static String bd_path = "/data/data/com.example.caminata/databases/";
    private static final int BD_Version =1;
    private SQLiteDatabase database;
    private final Context mycontext;


    public bd_connection(Context context) {
        super(context, BD_NAME, null, BD_Version);
        mycontext = context;

    }
    public void createdatabase() throws IOException {
        boolean dbexist = checkdatabase();
        if(!dbexist){
            this.getReadableDatabase();

            try{
                copydatabase();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private boolean checkdatabase(){
        SQLiteDatabase checkDB= null;
        try{
            String path= mycontext.getDatabasePath(BD_NAME).getAbsolutePath();
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

        } catch (SQLiteException e) {
            checkDB = null ;
        }
        if(checkDB != null){
            checkDB.close();

        }
        return checkDB != null? true : false;
    }

    public void copydatabase() throws IOException {
       try{
           InputStream myImput= mycontext.getAssets().open(BD_NAME);
           String outfilename =mycontext.getDatabasePath(BD_NAME).getAbsolutePath();
           OutputStream myoutput = new FileOutputStream(outfilename);
           byte[] buffer = new byte[1024];
           int length;
           while ((length = myImput.read(buffer))>0){
               myoutput.write(buffer,0,length);
           }
           myoutput.flush();
           myoutput.close();
           myImput.close();
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    public void open(){
        String path= mycontext.getDatabasePath(BD_NAME).getAbsolutePath();
        try{
               crearBD();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //SQL Code
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //Prueba
    private void copyDatabaseToExternalStorage() {
        File dbFile = mycontext.getDatabasePath("sensores_inteligentes.db");

        // Cambiar a getExternalFilesDir para Scoped Storage
        File externalDir = new File( mycontext.getExternalFilesDir(null), "MyAppFolder");

        // Verifica si el archivo de base de datos existe
        if (!dbFile.exists()) {
            Log.e("DatabaseCopy", "La base de datos no existe: " + dbFile.getAbsolutePath());
            return;
        }

        // Crea el directorio si no existe
        if (!externalDir.exists()) {
            if (!externalDir.mkdirs()) {
                Log.e("DatabaseCopy", "No se pudo crear el directorio: " + externalDir.getAbsolutePath());
                return;
            }
        }

        File newFile = new File(externalDir, "sensores_inteligentes.db");

        try (FileInputStream fis = new FileInputStream(dbFile);
             FileOutputStream fos = new FileOutputStream(newFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            Log.d("DatabaseCopy", "Base de datos copiada correctamente al almacenamiento externo: " + newFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("DatabaseCopy", "Error al copiar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void eliminar(){
        File dir = new File("/data/data/com.example.caminata/databases/");

        // Verifica si el directorio existe y es un directorio válido
        if (dir.exists() && dir.isDirectory()) {
            // Obtiene todos los archivos del directorio
            File[] files = dir.listFiles();

            if (files != null) {
                for (File file : files) {
                    // Verifica si el archivo es un archivo normal (no un directorio)
                    if (file.isFile()) {
                        boolean deleted = file.delete();  // Elimina el archivo
                    }
                }
                crearBD();
            }
        }
    }

    public void crearBD(){
        try {
            createdatabase();
            copyDatabaseToExternalStorage();
            String myPath = mycontext.getDatabasePath(BD_NAME).getAbsolutePath();
            database = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READONLY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
