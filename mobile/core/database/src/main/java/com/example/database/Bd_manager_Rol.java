package com.example.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Bd_manager_Rol {
    private bd_connection connection;
    private SQLiteDatabase database;
    private final Context mycontext;
    /// Tabla usuario
    private static final String Table_user="rol";
    private static final String Rol_id="id";
    private static final String Rol_name="nombre";

    //conexión
    public Bd_manager_Rol(Context context) {
        connection = new bd_connection(context);
        connection.open();
        mycontext = context;
    }

    public Bd_manager_Rol open_to_write() throws SQLException {
        database = connection.getWritableDatabase();
        return this;
    }
    public Bd_manager_Rol open_to_read() throws SQLException {
        database = connection.getReadableDatabase();
        return this;
    }

    public void close(){
        connection.close();
    }

    public Long getIdRol(String nombre){
        Long id= Long.valueOf(-1);
        this.open_to_read();
        Cursor result = database.rawQuery("Select id from rol where nombre = " + '"' +nombre + '"',null);

        if(result.moveToFirst()){
            do {
                int col= result.getColumnIndex(Rol_id);
                id = result.getLong(col);
            } while (result.moveToNext());
        }
        result.close();
        this.close();
        return id;
    }

    public String getnombreRol(Long rol){
        String nombre=null;
        this.open_to_read();
        Cursor result = database.rawQuery("Select nombre from rol where id = " + rol,null);

        if(result.moveToFirst()){
            do {
                int col= result.getColumnIndex(Rol_name);
                nombre = result.getString(col);
            } while (result.moveToNext());
        }
        result.close();
        this.close();

        return nombre;
    }
}
