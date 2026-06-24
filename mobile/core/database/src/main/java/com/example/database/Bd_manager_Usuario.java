package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.User;

import java.util.ArrayList;

public class Bd_manager_Usuario {
    private bd_connection connection;
    private SQLiteDatabase database;
    private final Context mycontext;
    /// Tabla usuario
    private static final String Table_user="usuario";
    private static final String User_id="id";
    private static final String User_name="nombre";
    private static final String User_username = "username";
    private static final String User_password = "contrasenna";
    private static final String User_no_profesion = "noprofesional";
    private static final String User_rol = "idrol";

    //conexión
    public Bd_manager_Usuario(Context context) {
        connection = new bd_connection(context);
        connection.open();
        mycontext = context;
    }

    public Bd_manager_Usuario open_to_write() throws SQLException {
        database = connection.getWritableDatabase();
        return this;
    }
    public Bd_manager_Usuario open_to_read() throws SQLException {
        database = connection.getReadableDatabase();
        return this;
    }

    public void close(){
        connection.close();
    }

    public void user_insert(String nombre, String username, String pass, String noprofesional, String rol){
        try{
        Bd_manager_Rol manager_rol = new Bd_manager_Rol(mycontext);
        Long roles = manager_rol.getIdRol(rol);
        ContentValues values = new ContentValues();
        this.open_to_write();

            values.put(User_name, nombre);
            values.put(User_rol,roles);
            values.put(User_username,username);
            values.put(User_password,pass);
            values.put(User_no_profesion,noprofesional);

            database.insert(Table_user,null,values);
            this.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public ArrayList<User> user_list(int excludedUserId) {
        ArrayList<User> list = new ArrayList<User>();
        int name = -1;
        int username = -1;
        int pass = -1;
        int roles = -1;
        int noprof = -1;

        this.open_to_read();
       try{
           Cursor result = database.rawQuery("SELECT * FROM usuario WHERE id <> ? AND noprofesional IS NOT NULL", new String[]{String.valueOf(excludedUserId)});
           if (result != null && result.getCount() >0 ) {
               result.moveToFirst();
               try {
                   do {
                       name = result.getColumnIndex(User_name);
                       username = result.getColumnIndex(User_username);
                       pass= result.getColumnIndex(User_password);
                       roles = result.getColumnIndex(User_rol);
                       noprof = result.getColumnIndex(User_no_profesion);
                       String nombre = result.getString(name);
                       String usern = result.getString(username);
                       String passw = result.getString(pass);
                       String noprofesional = result.getString(noprof);
                       int rol = result.getInt(roles);
                       User user = new User(nombre,usern,passw,getnombreRol((long) rol),noprofesional);
                       list.add(user);
                   } while (result.moveToNext());
               } finally {
                   result.close();
                   this.close();
               }
           }

       } catch (Exception e){
           e.printStackTrace();
       }

        return list;
    }

    public ArrayList<User> user_listAll() {
        ArrayList<User> list = new ArrayList<User>();
        int name = -1;
        int username = -1;
        int pass = -1;
        int roles = -1;
        int noprof = -1;

        this.open_to_read();
        try{
            Cursor result = database.rawQuery("SELECT * FROM usuario ", null);
            if (result != null && result.getCount() >0 ) {
                result.moveToFirst();
                try {
                    do {
                        name = result.getColumnIndex(User_name);
                        username = result.getColumnIndex(User_username);
                        pass = result.getColumnIndex(User_password);
                        roles = result.getColumnIndex(User_rol);
                        noprof = result.getColumnIndex(User_no_profesion);
                        String nombre = result.getString(name);
                        String usern = result.getString(username);
                        String passw = result.getString(pass);
                        String noprofesional = result.getString(noprof);
                        int rol = result.getInt(roles);
                        User user = new User(nombre, usern, passw,getnombreRol((long) rol),noprofesional);
                        list.add(user);
                    } while (result.moveToNext());
                } finally {
                    result.close();
                    this.close();
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public void user_delete(String name){
        int colmdelete= 0;
        String whereClause = User_id + " = ?";
        int id = user_getID(name);
        String [] whereArgs = new String[]{String.valueOf(id)};
        this.open_to_write();
        database.execSQL("PRAGMA foreign_keys=ON;");
        colmdelete = database.delete(Table_user,whereClause,whereArgs);
        this.close();
    }

    public void user_update(String last_name, String last_pass, String new_name,
                            String new_pass, String last_noprofecional,
                            String new_noprofecional) {
        int id = user_getID(last_name);
        String whereClause = User_id + " = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        ContentValues values = new ContentValues();
        this.open_to_write();

        // Configuración de valores para cada parámetro no nulo
        if (new_name != null) {
            values.put(User_name, new_name);
        } else {
            values.put(User_name, last_name);
        }

        if (new_pass != null) {
            values.put(User_password, new_pass);
        } else {
            values.put(User_username, last_pass);
        }

        if (new_noprofecional != null) {
            values.put(User_no_profesion, new_noprofecional);
        } else {
            values.put(User_no_profesion, last_noprofecional);
        }

        // Actualizar la base de datos
        database.update(Table_user, values, whereClause, whereArgs);
        this.close();
    }

    public void user_updateRol(String last_rol, String new_rol, String name) {
        int id = user_getID(name);
        String whereClause = User_id + " = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        ContentValues values = new ContentValues();
        this.open_to_write();

        // Configuración de valores
        if (new_rol != last_rol) {
            Bd_manager_Rol manager_rol = new Bd_manager_Rol(mycontext);
            Long rol = manager_rol.getIdRol(new_rol);
            values.put(User_rol, rol);
        }

        // Actualizar la base de datos
        database.update(Table_user, values, whereClause, whereArgs);
        this.close();
    }


    public void user_deleteAll(){
        int colmdelete= 0;
        this.open_to_write();
        colmdelete = database.delete(Table_user,String.valueOf(1),null);
        this.close();
    }

    public int user_getID(String name){
        int id=-1;
        this.open_to_read();
        String [] whereArgs = new String[]{name};
        Cursor result = database.rawQuery("Select id from usuario where nombre = " + '"' +name + '"' + " or username = " + '"' + name + '"',null);

        if(result.moveToFirst()){
            do {
                int col= result.getColumnIndex(User_id);
                id = result.getInt(col);
            } while (result.moveToNext());
        }
        result.close();
        this.close();
        return id;
    }

    public User getUser(String name){
        int id=-1;
        User user = null;
        int namecol = -1;
        int pass = -1;
        int repitpass = -1;
        int roles = -1;
        int noprof = -1;

        this.open_to_read();
        String [] whereArgs = new String[]{name};
        Cursor result = database.rawQuery("Select * from usuario where nombre = " + '"' + name + '"'+ " or username = "+ '"' + name + '"',null);

        if(result.moveToFirst()){
            do {
                namecol = result.getColumnIndex(User_name);
                pass = result.getColumnIndex(User_username);
                repitpass= result.getColumnIndex(User_password);
                roles = result.getColumnIndex(User_rol);
                noprof = result.getColumnIndex(User_no_profesion);
                String nombre = result.getString(namecol);
                String passw = result.getString(pass);
                String repitpassw = result.getString(repitpass);
                String noprofesional = result.getString(noprof);
                int rol = result.getInt(roles);
                user = new User(nombre,passw,repitpassw,getnombreRol((long) rol),noprofesional);
            } while (result.moveToNext());
        }
        result.close();
        this.close();

        return user;
    }

    public String getpassword(String name){
        int id= user_getID(name);
        String pass = null;
        this.open_to_read();
        Cursor result = database.rawQuery("Select contrasenna from usuario where id = " + id ,null);

        if(result.moveToFirst()){
            do {
                int col= result.getColumnIndex(User_password);
                pass = result.getString(col);
            } while (result.moveToNext());
        }
        result.close();
        this.close();

        return pass;
    }

    public String getnoprofesional(String name){
        int id= user_getID(name);
        String noprofesional = null;
        this.open_to_read();
        Cursor result = database.rawQuery("Select noprofesional from usuario where id = " + '"' + id + '"',null);

        if(result.moveToFirst()){
            do {
                int col= result.getColumnIndex(User_no_profesion);
                noprofesional = result.getString(col);
            } while (result.moveToNext());
        }
        result.close();
        this.close();

        return noprofesional;
    }

    public String getnombreRol(Long rol){
        String nombre = null;
        Bd_manager_Rol manager_rol = new Bd_manager_Rol(mycontext);
        nombre = manager_rol.getnombreRol(rol);
        return nombre;
    }

    public String getnombreRolbyUser(String name){
        int roles = -1;
        String nombre = null;

        this.open_to_read();
        String [] whereArgs = new String[]{name};
        Cursor result = database.rawQuery("Select idrol from usuario where nombre = " + '"' + name + '"'+ " or username = "+ '"' + name + '"',null);

        if(result.moveToFirst()){
            do {
                roles = result.getColumnIndex(User_rol);
                int rol = result.getInt(roles);

                Bd_manager_Rol manager_rol = new Bd_manager_Rol(mycontext);
                nombre = manager_rol.getnombreRol((long) rol);
            } while (result.moveToNext());
        }
        result.close();
        this.close();

        return nombre;
    }

    public bd_connection getReadableDatabase() {
        return connection; // Llama al método de la superclase
    }
}
