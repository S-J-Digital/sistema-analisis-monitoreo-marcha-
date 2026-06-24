package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.People;

import java.util.ArrayList;
import java.util.List;

public class Bd_manager_Participante {

    private bd_connection connection;
    private SQLiteDatabase database;
    private final Context mycontext;

    private static final String Table_person = "participante";
    private static final String Person_id = "id";
    private static final String Person_name = "nombre";
    private static final String Person_ci = "ci";
    private static final String Person_sex = "sexo";
    private static final String Person_phone = "telefono";

    public Bd_manager_Participante (Context context) {
        connection = new bd_connection(context);
        connection.open();
        mycontext = context;
    }
    public Bd_manager_Participante open_to_write() throws SQLException {
        database = connection.getWritableDatabase();
        return this;
    }
    public Bd_manager_Participante open_to_read() throws SQLException {
        database = connection.getReadableDatabase();
        return this;
    }

    public void close(){
        connection.close();
    }

    public void person_insert(String usuario,String nombre, String ci,String sexo, String telefono){
        ContentValues values = new ContentValues();
        Bd_manager_Usuario user = new Bd_manager_Usuario(mycontext);
        Bd_manager_usuario_participante manager_usuario_participante = new Bd_manager_usuario_participante(mycontext);
        int id_user = user.user_getID(usuario);
        this.open_to_write();
        if(database.isOpen()){
            values.put(Person_name, nombre);
            values.put(Person_ci,ci);
            values.put(Person_sex,sexo);
            values.put(Person_phone,telefono);
            database.insert(Table_person,null,values);
        }
        this.close();

        manager_usuario_participante.insert_usuario_paciente(id_user,person_getID(ci));
    }

    public void person_update(String ci_antiguo,String nombre, String ci, String sexo, String telefono){
        int id = person_getID(ci_antiguo);
        String whereClause = Person_id + " = ?";
        String [] whereArgs = new String[]{String.valueOf(id)};
        ContentValues values = new ContentValues();

        values.put(Person_name, nombre);
        values.put(Person_ci,ci);
        values.put(Person_sex, sexo);
        values.put(Person_phone,telefono);

        this.open_to_write();
        database.update(Table_person,values,whereClause,whereArgs);
        this.close();
    }

    public void person_delete(String ci){
        int colmdelete= 0;
        String whereClause = Person_id + " = ?";
        int id = person_getID(ci);
        String [] whereArgs = new String[]{String.valueOf(id)};
        this.open_to_write();
        database.execSQL("PRAGMA foreign_keys=ON;");
        colmdelete = database.delete(Table_person,whereClause,whereArgs);
        this.close();
    }

    public void person_deleteAll(){
        int colmdelete= 0;
        this.open_to_write();
        colmdelete = database.delete(Table_person,String.valueOf(1),null);
        this.close();
    }

    public ArrayList<People> person_list(String user) {
        Bd_manager_Usuario manager_usuario = new Bd_manager_Usuario(mycontext);
        int id_user = manager_usuario.user_getID(user);
        ArrayList<People> list = new ArrayList<People>();
        int name = -1;
        int ci = -1;
        int sexo = -1;
        int telef = -1;
        int cant = -1;

        this.open_to_read();
        Cursor result = database.rawQuery("SELECT p.* " + "FROM participante p " +
                        "INNER JOIN usuarioparticipante up ON p.id = up.id_participante " +
                        "WHERE up.id_usuario = "+id_user, null);
        if (result != null && result.getCount() >0 ) {
            result.moveToFirst();
            try {
                do {
                    name = result.getColumnIndex(Person_name);
                    ci = result.getColumnIndex(Person_ci);
                    sexo = result.getColumnIndex(Person_sex);
                    telef = result.getColumnIndex(Person_phone);

                    String p_nombre = result.getString(name);
                    String p_ci = result.getString(ci);
                    String p_sexo = result.getString(sexo);
                    String p_telef = result.getString(telef);
                    People newPeople = new People(p_nombre,p_ci,p_sexo,p_telef);
                    list.add(newPeople);
                } while (result.moveToNext());

            } finally {
                result.close();
                this.close();
            }
        }
        return list;
    }

    public int person_getID(String ci_person){
        int id=-1;
        this.open_to_read();
        Cursor result = database.rawQuery("Select id from  participante where ci =   "  + '"' +ci_person + '"',null);

        if(result.moveToFirst()){
            // do {
            int col= result.getColumnIndex(Person_id);
            id = result.getInt(col);
            // } while (result.moveToNext());
        }
        result.close();
        this.close();

        return id;
    }

    public People getperson_byname(String name, String user){
        Bd_manager_Usuario manager_usuario = new Bd_manager_Usuario(mycontext);
        int id_user = manager_usuario.user_getID(user);
        int id=-1;
        this.open_to_read();
        People people = null;
        Cursor result = database.rawQuery("SELECT p.* " +
                "FROM participante p " +
                "INNER JOIN usuarioparticipante up ON p.id = up.id_participante " +
                "WHERE p.nombre = ? AND up.id_usuario =" +id_user  ,null);

        if(result.moveToFirst()){
            // do {
            int nombre= result.getColumnIndex(Person_name);
            int ci= result.getColumnIndex(Person_ci);
            int sexo= result.getColumnIndex(Person_sex);
            int tele= result.getColumnIndex(Person_phone);
            people = new People(result.getString(nombre),result.getString(ci),result.getString(sexo),result.getString(tele));
            // } while (result.moveToNext());
        }
        result.close();
        this.close();

        return people;
    }


    public List<String> getMatchingParticipants(String query, String user) {
        List<String> participants = new ArrayList<>();
        Bd_manager_Usuario manager_usuario = new Bd_manager_Usuario(mycontext);
        int id_user = manager_usuario.user_getID(user);
        this.open_to_read();
        String sqlQuery = "SELECT p." + Person_name +
                " FROM " + Table_person + " p " +
                "INNER JOIN usuario_participante up ON p.id = up.id_participante " +
                "WHERE p." + Person_name + " LIKE ? COLLATE NOCASE AND up.id_usuario = ?" + id_user;
        Cursor cursor = database.rawQuery(sqlQuery, new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                participants.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return participants;
    }
}
