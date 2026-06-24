package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;

import java.util.ArrayList;

public class Bd_manager_Sennales {

    private bd_connection connection;
    private SQLiteDatabase database;
    private final Context mycontext;

    private static final String Table_sennal = "sennal";
    private static final String sennal_id = "id";
    private static final String sennal_participante = "idparticipante";
    private static final String sennal_datos = "iddato";
    private static final String fecha ="fecha";
    private static final String sennal_acelX = "acelerometroX";
    private static final String sennal_acelY = "acelerometroY";
    private static final String sennal_acelZ = "acelerometroZ";
    private static final String sennal_girosX = "giroscopioX";
    private static final String sennal_girosY = "giroscopioY";
    private static final String sennal_girosZ = "giroscopioZ";
    private static final String sennal_magneX = "magnetometroX";
    private static final String sennal_magneY = "magnetometroY";
    private static final String sennal_magneZ = "magnetometroZ";
    private static final String sennal_temp = "temperatura";
    private static final String sennal_rangoAcel = "rangoacelerometro";
    private static final String sennal_rangoGiros = "rangogiroscopio";
    private static final String sennal_frecuencia= "frecuenciademuestreo";

    public Bd_manager_Sennales (Context context) {
        connection = new bd_connection(context);
        connection.open();
        mycontext = context;
    }
    public Bd_manager_Sennales open_to_write() throws SQLException {
        database = connection.getWritableDatabase();
        return this;
    }
    public Bd_manager_Sennales open_to_read() throws SQLException {
        database = connection.getReadableDatabase();
        return this;
    }

    public void close(){
        connection.close();
    }

    public void sennal_insert(People people, DataPeople data, double acelx, double acely, double acelz, double girosx, double girosy, double girosz,
                              double magnex, double magney, double magnez, double tempe,int rangoacel,int rangogiros,int frecuencia){
        ContentValues values = new ContentValues();
        Bd_manager_DatosParticipante dato = new Bd_manager_DatosParticipante(mycontext);
        values.put(sennal_datos,dato.data_getID(people.getCi(),data.getDate()));
        values.put(fecha,dato.getDate());
        values.put(sennal_acelX,acelx);
        values.put(sennal_acelY,acely);
        values.put(sennal_acelZ,acelz);
        values.put(sennal_girosX,girosx);
        values.put(sennal_girosY,girosy);
        values.put(sennal_girosZ,girosz);
        values.put(sennal_magneX,magnex);
        values.put(sennal_magneY,magney);
        values.put(sennal_magneZ,magnez);
        values.put(sennal_temp,tempe);
        values.put(sennal_rangoAcel,rangoacel);
        values.put(sennal_rangoGiros,rangogiros);
        values.put(sennal_frecuencia,frecuencia);
        this.open_to_write();
        database.insert(Table_sennal,null,values);
        this.close();
    }
    public void sennal_deleteAll(){
        int colmdelete= 0;
        this.open_to_write();
        colmdelete = database.delete(Table_sennal,String.valueOf(1),null);
        this.close();
    }

    public void sennal_deleteAll_by_Date(DataPeople data){
        int colmdelete= 0;
        String whereClause= fecha + " = ?";
        String [] whereArgs = new String[]{data.getDate()};
        this.open_to_write();
        colmdelete = database.delete(Table_sennal,whereClause,whereArgs);
        this.close();
    }


    public ArrayList<Sennales> sennales_list(String ci, DataPeople dataPeople) {
        Bd_manager_DatosParticipante datosparticipante = new Bd_manager_DatosParticipante(mycontext);
        int iddatos = datosparticipante.data_getID(ci, dataPeople.getDate());
        ArrayList<Sennales> list = new ArrayList<>();
        int  personId;
        int  datoId;
        String getfecha;
        double acelerometroX;
        double acelerometroY;
        double acelerometroZ;
        double giroscopioX;
        double giroscopioY;
        double giroscopioZ;
        double magnetometroX;
        double magnetometroY;
        double magnetometroZ;
        double temperatura;
        int rangoacel;
        int rangogiros;
        int frec;


        this.open_to_read();
        Cursor result = database.rawQuery("Select * FROM sennal where iddato = " + iddatos, null);
        if (result != null && result.getCount() >0 ) {
            result.moveToFirst();
            int dato = result.getColumnIndex(sennal_datos);
            int fechas = result.getColumnIndex(fecha);
            int acelX = result.getColumnIndex(sennal_acelX);
            int acelY = result.getColumnIndex(sennal_acelY);
            int acelZ = result.getColumnIndex(sennal_acelZ);
            int giroX = result.getColumnIndex(sennal_girosX);
            int giroY = result.getColumnIndex(sennal_girosY);
            int giroZ = result.getColumnIndex(sennal_girosZ);
            int magneX = result.getColumnIndex(sennal_magneX);
            int magneY = result.getColumnIndex(sennal_magneY);
            int magneZ= result.getColumnIndex(sennal_magneZ);
            int temp= result.getColumnIndex(sennal_temp);
            int rangoA= result.getColumnIndex(sennal_rangoAcel);
            int rangoG= result.getColumnIndex(sennal_rangoGiros);
            int config_frec = result.getColumnIndex(sennal_frecuencia);
            try {
                do {
                    datoId = result.getInt(dato);
                    getfecha = result.getString(fechas);
                    acelerometroX = result.getDouble(acelX);
                    acelerometroY = result.getDouble(acelY);
                    acelerometroZ = result.getDouble(acelZ);
                    giroscopioX = result.getDouble(giroX);
                    giroscopioY = result.getDouble(giroY);
                    giroscopioZ = result.getDouble(giroZ);
                    magnetometroX = result.getDouble(magneX);
                    magnetometroY = result.getDouble(magneY);
                    magnetometroZ = result.getDouble(magneZ);
                    temperatura = result.getDouble(temp);
                    rangoacel= result.getInt(rangoA);
                    rangogiros= result.getInt(rangoG);
                    frec = result.getInt(config_frec);

                    Sennales s = new Sennales(datoId,getfecha,acelerometroX,acelerometroY,acelerometroZ,
                            giroscopioX,giroscopioY,giroscopioZ,magnetometroX,magnetometroY,magnetometroZ,
                            temperatura,rangoacel,rangogiros,frec);
                    list.add(s);
                } while (result.moveToNext());

            } finally {
                result.close();
                this.close();
            }
        }
        return list;
    }

    public ArrayList<Float> SennalesMismaFechaAceX(String ci, DataPeople dataPeople, Sennales sennal) {
        Bd_manager_DatosParticipante datosparticipante = new Bd_manager_DatosParticipante(mycontext);
        int iddatos = datosparticipante.data_getID(ci,dataPeople.getDate());
        ArrayList<Float> list = new ArrayList<>();

        Float acelerometroX;

        this.open_to_read();
        Cursor result = database.rawQuery("Select acelerometroX FROM sennal where iddato = " + iddatos + " and fecha = "+ '"' +sennal.getFecha() + '"', null);
        if (result != null && result.getCount() >0 ) {
            result.moveToFirst();

            int acelX = result.getColumnIndex(sennal_acelX);

            try {
                do {
                    acelerometroX = result.getFloat(acelX);
                    list.add(acelerometroX);
                } while (result.moveToNext());

            } finally {
                result.close();
                this.close();
            }
        }
        return list;
    }

    public ArrayList<Float> SennalesMismaFechaAceY(String ci, DataPeople dataPeople, Sennales sennal) {
        Bd_manager_DatosParticipante datosparticipante = new Bd_manager_DatosParticipante(mycontext);
        int iddatos = datosparticipante.data_getID(ci, dataPeople.getDate());
        ArrayList<Float> list = new ArrayList<>();

        Float acelerometroY;


        this.open_to_read();
        Cursor result = database.rawQuery("Select acelerometroY FROM sennal where iddato = " + iddatos + " and fecha = "+ '"' +sennal.getFecha() + '"', null);
        if (result != null && result.getCount() >0 ) {
            result.moveToFirst();

            int acelY = result.getColumnIndex(sennal_acelY);

            try {
                do {
                    acelerometroY = result.getFloat(acelY);
                    list.add(acelerometroY);
                } while (result.moveToNext());

            } finally {
                result.close();
                this.close();
            }
        }
        return list;
    }

    public ArrayList<Float> SennalesMismaFechaAceZ(String ci, DataPeople dataPeople, Sennales sennal) {
        Bd_manager_DatosParticipante datosparticipante = new Bd_manager_DatosParticipante(mycontext);
        int iddatos = datosparticipante.data_getID(ci, dataPeople.getDate());
        ArrayList<Float> list = new ArrayList<>();

        Float acelerometroZ;


        this.open_to_read();
        Cursor result = database.rawQuery("Select acelerometroZ FROM sennal where iddato = " + iddatos + " and fecha = "+ '"' +sennal.getFecha() + '"', null);
        if (result != null && result.getCount() >0 ) {
            result.moveToFirst();

            int acelZ = result.getColumnIndex(sennal_acelZ);

            try {
                do {
                    acelerometroZ = result.getFloat(acelZ);
                    list.add(acelerometroZ);
                } while (result.moveToNext());

            } finally {
                result.close();
                this.close();
            }
        }
        return list;
    }

    //Giroscopio
    public ArrayList<Float> SennalesMismaFechaGirosX(String ci, DataPeople dataPeople, Sennales sennal) {
        Bd_manager_DatosParticipante datosparticipante = new Bd_manager_DatosParticipante(mycontext);
        int iddatos = datosparticipante.data_getID(ci, dataPeople.getDate());
        ArrayList<Float> list = new ArrayList<>();

        Float giroscopioX;


        this.open_to_read();
        Cursor result = database.rawQuery("Select giroscopioX FROM sennal where iddato = " + iddatos + " and fecha = "+ '"' +sennal.getFecha() + '"', null);
        if (result != null && result.getCount() >0 ) {
            result.moveToFirst();

            int giro = result.getColumnIndex(sennal_girosX);

            try {
                do {
                    giroscopioX = result.getFloat(giro);
                    list.add(giroscopioX);
                } while (result.moveToNext());

            } finally {
                result.close();
                this.close();
            }
        }
        return list;
    }

    public ArrayList<Float> SennalesMismaFechaGirosY(String ci, DataPeople dataPeople, Sennales sennal) {
        Bd_manager_DatosParticipante datosparticipante = new Bd_manager_DatosParticipante(mycontext);
        int iddatos = datosparticipante.data_getID(ci, dataPeople.getDate());
        ArrayList<Float> list = new ArrayList<>();

        Float giroscopioY;


        this.open_to_read();
        Cursor result = database.rawQuery("Select giroscopioY FROM sennal where iddato = " + iddatos + " and fecha = "+ '"' +sennal.getFecha() + '"', null);
        if (result != null && result.getCount() >0 ) {
            result.moveToFirst();

            int giro = result.getColumnIndex(sennal_girosY);

            try {
                do {
                    giroscopioY = result.getFloat(giro);
                    list.add(giroscopioY);
                } while (result.moveToNext());

            } finally {
                result.close();
                this.close();
            }
        }
        return list;
    }

    public ArrayList<Float> SennalesMismaFechaGirosZ(String ci, DataPeople dataPeople, Sennales sennal) {
        Bd_manager_DatosParticipante datosparticipante = new Bd_manager_DatosParticipante(mycontext);
        int iddatos = datosparticipante.data_getID(ci, dataPeople.getDate());
        ArrayList<Float> list = new ArrayList<>();

        Float giroscopioZ;


        this.open_to_read();
        Cursor result = database.rawQuery("Select giroscopioZ FROM sennal where iddato = " + iddatos + " and fecha = "+ '"' +sennal.getFecha() + '"', null);
        if (result != null && result.getCount() >0 ) {
            result.moveToFirst();

            int giro = result.getColumnIndex(sennal_girosZ);

            try {
                do {
                    giroscopioZ = result.getFloat(giro);
                    list.add(giroscopioZ);
                } while (result.moveToNext());

            } finally {
                result.close();
                this.close();
            }
        }
        return list;
    }

    // Magnetometro

    public ArrayList<Float> SennalesMismaFechaMagneX(String ci, DataPeople dataPeople, Sennales sennal) {
        Bd_manager_DatosParticipante datosparticipante = new Bd_manager_DatosParticipante(mycontext);
        int iddatos = datosparticipante.data_getID(ci, dataPeople.getDate());
        ArrayList<Float> list = new ArrayList<>();

        Float magnetometroX;


        this.open_to_read();
        Cursor result = database.rawQuery("Select magnetometroX FROM sennal where iddato = " + iddatos + " and fecha = "+ '"' +sennal.getFecha() + '"', null);
        if (result != null && result.getCount() >0 ) {
            result.moveToFirst();

            int magne = result.getColumnIndex(sennal_magneX);

            try {
                do {
                    magnetometroX = result.getFloat(magne);
                    list.add(magnetometroX);
                } while (result.moveToNext());

            } finally {
                result.close();
                this.close();
            }
        }
        return list;
    }

    public ArrayList<Float> SennalesMismaFechaMagneY(String ci, DataPeople dataPeople, Sennales sennal) {
        Bd_manager_DatosParticipante datosparticipante = new Bd_manager_DatosParticipante(mycontext);
        int iddatos = datosparticipante.data_getID(ci, dataPeople.getDate());
        ArrayList<Float> list = new ArrayList<>();

        Float magnetometroY;


        this.open_to_read();
        Cursor result = database.rawQuery("Select magnetometroY FROM sennal where iddato = " + iddatos + " and fecha = "+ '"' +sennal.getFecha() + '"', null);
        if (result != null && result.getCount() >0 ) {
            result.moveToFirst();

            int magne = result.getColumnIndex(sennal_magneY);

            try {
                do {
                    magnetometroY = result.getFloat(magne);
                    list.add(magnetometroY);
                } while (result.moveToNext());

            } finally {
                result.close();
                this.close();
            }
        }
        return list;
    }

    public ArrayList<Float> SennalesMismaFechaMagneZ(String ci, DataPeople dataPeople, Sennales sennal) {
        Bd_manager_DatosParticipante datosparticipante = new Bd_manager_DatosParticipante(mycontext);
        int iddatos = datosparticipante.data_getID(ci, dataPeople.getDate());
        ArrayList<Float> list = new ArrayList<>();

        Float magnetometroZ;


        this.open_to_read();
        Cursor result = database.rawQuery("Select magnetometroZ FROM sennal where iddato = " + iddatos + " and fecha = "+ '"' +sennal.getFecha() + '"', null);
        if (result != null && result.getCount() >0 ) {
            result.moveToFirst();

            int magne = result.getColumnIndex(sennal_magneZ);

            try {
                do {
                    magnetometroZ = result.getFloat(magne);
                    list.add(magnetometroZ);
                } while (result.moveToNext());

            } finally {
                result.close();
                this.close();
            }
        }
        return list;
    }

    public ArrayList<Float> SennalesMismaFechaTemp(String ci, DataPeople dataPeople, Sennales sennal) {
        Bd_manager_DatosParticipante datosparticipante = new Bd_manager_DatosParticipante(mycontext);
        int iddatos = datosparticipante.data_getID(ci, dataPeople.getDate());
        ArrayList<Float> list = new ArrayList<>();

        Float temperatura;


        this.open_to_read();
        Cursor result = database.rawQuery("Select temperatura FROM sennal where iddato = " + iddatos + " and fecha = "+ '"' +sennal.getFecha() + '"', null);
        if (result != null && result.getCount() >0 ) {
            result.moveToFirst();

            int temp= result.getColumnIndex(sennal_temp);;
            try {
                do {
                    temperatura = result.getFloat(temp);
                    list.add(temperatura);
                } while (result.moveToNext());

            } finally {
                result.close();
                this.close();
            }
        }
        return list;
    }
}
