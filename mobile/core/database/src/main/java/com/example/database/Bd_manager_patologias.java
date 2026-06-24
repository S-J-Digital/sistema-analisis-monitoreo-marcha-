package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Bd_manager_patologias {

    private bd_connection connection;
    private SQLiteDatabase database;
    private final Context mycontext;

    /// Tabla patologia
    private static final String Table_patologia="patologia";
    private static final String Patologia_id="id";
    private static final String Patologia_nombre = "nombre";

    public Bd_manager_patologias(Context context) {
        connection = new bd_connection(context);
        connection.open();
        mycontext = context;
    }

    public Bd_manager_patologias open_to_write() throws SQLException {
        database = connection.getWritableDatabase();
        return this;
    }
    public Bd_manager_patologias open_to_read() throws SQLException {
        database = connection.getReadableDatabase();
        return this;
    }

    public void close(){
        connection.close();
    }

    public void insert_patologia(String nombre){
       if(nombre != null){
           if(getpatologiabyname(nombre)==-1){
               ContentValues values = new ContentValues();
               this.open_to_write();
               values.put(Patologia_nombre, nombre);
               database.insert(Table_patologia,null,values);
               this.close();
           }
       }
    }

    public int getpatologiabyname(String name){
        int id=-1;
        this.open_to_read();
        String [] whereArgs = new String[]{name};
        Cursor result = database.rawQuery("Select id from patologia where nombre = " + '"' + name + '"',null);

        if(result.moveToFirst()){
            do {
                int col= result.getColumnIndex(Patologia_id);
                id = result.getInt(col);
            } while (result.moveToNext());
        }
        result.close();
        this.close();

        return id;
    }

    public String getpatologiabyid(int id){
        String name = null;
       try{
           this.open_to_read();
           Cursor result = database.rawQuery("Select nombre from patologia where id = " + id,null);

           if(result.moveToFirst()){
               do {
                   int col= result.getColumnIndex(Patologia_nombre);
                   name = result.getString(col);
               } while (result.moveToNext());
           }
           result.close();
           this.close();
       }catch (Exception e){
           e.printStackTrace();
       }

        return name;
    }

    public List<String> allpatologia(){
        List<String> patologialist = new ArrayList<>();
        String name = null;

        try{
            this.open_to_read();
            Cursor result = database.rawQuery("Select * from patologia ",null);
            if(result.moveToFirst()){
                do {
                    int col= result.getColumnIndex(Patologia_nombre);
                    name = result.getString(col);
                    patologialist.add(name);
                } while (result.moveToNext());
            }
            result.close();
            this.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return patologialist;
    }
}
