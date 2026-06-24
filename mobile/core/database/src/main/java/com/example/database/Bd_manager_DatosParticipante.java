package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.model.DataPeople;
import com.example.model.People;

import java.sql.Time;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Locale;

public class Bd_manager_DatosParticipante {
    private bd_connection connection;
    private SQLiteDatabase database;
    private final Context mycontext;


    private static final String Table_data_person = "datoparticipante";
    private static final String data_id  = "id";
    private static final String id_person = "idparticipante";
    private static final String data_date = "fecha";
    private static final String data_hour = "hora";
    private static final String data_age = "edad";
    private static final String data_patologia = "patologia";
    private static final String data_shoes = "numerodecalzado";
    private static final String data_medicion = "medicioncinturatobillo";
    private static final String data_long_leg = "largopierna";
    private static final String data_height = "alturadelsensor";

    public Bd_manager_DatosParticipante (Context context) {
        connection = new bd_connection(context);
        connection.open();
        mycontext = context;
    }
    public Bd_manager_DatosParticipante open_to_write() throws SQLException {
        database = connection.getWritableDatabase();
        return this;
    }
    public Bd_manager_DatosParticipante open_to_read() throws SQLException {
        database = connection.getReadableDatabase();
        return this;
    }

    public void close(){
        connection.close();
    }

    public void data_insert(String ci, int edad, int patologia, double calzado, double cintura_tobillo,
                            double largo_pierna, double altura_sensor){
        // 0 = false 1 = true
        ContentValues values = new ContentValues();
        Bd_manager_Participante participante = new Bd_manager_Participante(mycontext);
        values.put(id_person, participante.person_getID(ci));
        values.put(data_date,getDate());
        values.put(data_hour,gethour());
        values.put(data_age,edad);
        values.put(data_patologia,patologia);
        values.put(data_shoes,calzado);
        values.put(data_medicion,cintura_tobillo);
        values.put(data_long_leg,largo_pierna);
        values.put(data_height,altura_sensor);
        this.open_to_write();
        database.insert(Table_data_person,null,values);
        this.close();
    }

    public void data_update(String ci, String fecha, int edad, int patologia, double calzado, double cintura_tobillo,
                            double largo_pierna, double altura_sensor){
        int id = data_getID(ci,fecha);
        Bd_manager_Participante participante = new Bd_manager_Participante(mycontext);
        String whereClause = data_id + " = ?";
        String [] whereArgs = new String[]{String.valueOf(id)};
        ContentValues values = new ContentValues();

        values.put(id_person, participante.person_getID(ci));
        values.put(data_age,edad);
        values.put(data_patologia,patologia);
        values.put(data_shoes,calzado);
        values.put(data_medicion,cintura_tobillo);
        values.put(data_long_leg,largo_pierna);
        values.put(data_height,altura_sensor);

        this.open_to_write();
        database.update(Table_data_person,values,whereClause,whereArgs);
        this.close();
    }

    public void data_delete(String ci, String fecha){
        int colmdelete= 0;
        String whereClause = data_id + " = ?";
        int id = data_getID(ci,fecha);
        String [] whereArgs = new String[]{String.valueOf(id)};
        this.open_to_write();
        database.execSQL("PRAGMA foreign_keys=ON;");
        colmdelete = database.delete(Table_data_person,whereClause,whereArgs);
        this.close();
    }

    public void data_deleteAll(){
        int colmdelete= 0;
        this.open_to_write();
        colmdelete = database.delete(Table_data_person,String.valueOf(1),null);
        this.close();
    }

    public ArrayList<DataPeople> data_list(People personas) {
        Bd_manager_Participante participante = new Bd_manager_Participante(mycontext);
        int idpersona = participante.person_getID(personas.getCi());
        ArrayList<DataPeople> list = new ArrayList<DataPeople>();
        int person = -1;
        int date = -1;
        int hour = -1;
        int ege = -1;
        int patologia = -1;
        int shoes = -1;
        int medicion = -1;
        int leg = -1;
        int height = -1;
        int id = -1;

        this.open_to_read();
        Cursor result = database.rawQuery("Select * FROM datoparticipante where idparticipante = " + idpersona , null);
        if (result != null && result.getCount() >0 ) {
            result.moveToFirst();
            id = result.getColumnIndex(data_id);
            person = result.getColumnIndex(id_person);
            date = result.getColumnIndex(data_date);
            hour = result.getColumnIndex(data_hour);
            ege = result.getColumnIndex(data_age);
            patologia = result.getColumnIndex(data_patologia);
            shoes = result.getColumnIndex(data_shoes);
            medicion = result.getColumnIndex(data_medicion);
            leg = result.getColumnIndex(data_long_leg);
            height = result.getColumnIndex(data_height);
            try {
                do {
                    int id_dato = result.getInt(id);
                    int persona = result.getInt(person);
                    String fecha = result.getString(date);
                    String hora = result.getString(hour);
                    int edad = result.getInt(ege);
                    int patol = result.getInt(patologia);
                    double calzado = result.getDouble(shoes);
                    double cintura_tobillo = result.getDouble(medicion);
                    double pierna = result.getDouble(leg);
                    double altura = result.getDouble(height);
                    List<String> observacion = obtener_patolog(fecha, id_dato);

                    DataPeople p = new DataPeople(fecha, hora,edad,patol,calzado,cintura_tobillo,pierna,altura,observacion);
                    list.add(p);
                } while (result.moveToNext());

            }catch (Exception e){
                Log.e("Error",e.getMessage());
            }finally {
                result.close();
                this.close();
            }
        }
        return list;
    }

    public int data_getID(String ci, String date){
        int id=-1;
        Bd_manager_Participante participante = new Bd_manager_Participante(mycontext);
        int id_part= participante.person_getID(ci);
        // String [] whereArgs = new String[]{String.valueOf(id_part), fecha};

        this.open_to_read();
        Cursor result = database.rawQuery("Select id from datoparticipante where idparticipante = " + id_part + " and  fecha = " + '"'+date + '"',null);

        if(result.moveToFirst()){
            do {
                int col= result.getColumnIndex(data_id);
                id = result.getInt(col);
            } while (result.moveToNext());
        }
        this.close();
        result.close();
        return id;
    }

    public String getDate(){
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        java.util.Date date = new java.util.Date();
        return dateFormat.format(date);
    }
    public String gethour(){
        SimpleDateFormat dateFormat= new SimpleDateFormat("hh:mm", Locale.getDefault());
        java.util.Date date = new java.util.Date();
        return dateFormat.format(date);
    }

    public void insert_en_tablas(String ci, int edad, int patologia, double calzado, double cintura_tobillo,
                 double largo_pierna, double altura_sensor, List<String> obsercion){
        data_insert(ci,edad,patologia,calzado,cintura_tobillo,largo_pierna,altura_sensor);
        Bd_manager_patologias manager_patologias = new Bd_manager_patologias(mycontext);
        if(obsercion.size()!=0){
            for (String enfermedad: obsercion) {
                manager_patologias.insert_patologia(enfermedad);
                Bd_manager_datos_patologia manager_datos_patologia = new Bd_manager_datos_patologia(mycontext);
                manager_datos_patologia.insert_datos_patologia(getDate(),data_getID(ci,getDate()),manager_patologias.getpatologiabyname(enfermedad));
            }
        }

    }

    public  List<String> obtener_patolog(String fecha, int id_dato){
        List<String> patologias = new ArrayList<>();
        Bd_manager_datos_patologia manager_datos_patologia = new Bd_manager_datos_patologia(mycontext);
        patologias = manager_datos_patologia.getListaPatologias(fecha, id_dato);

        return patologias;
    }

}
