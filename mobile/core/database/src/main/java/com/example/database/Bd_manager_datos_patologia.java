package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Bd_manager_datos_patologia {

    private bd_connection connection;
    private SQLiteDatabase database;
    private final Context mycontext;

    /// Tabla patologia
    private static final String Table_datos_patologia="datopatologia";
    private static final String datos_patologia_fecha="fechaevaluacion";
    private static final String datos_patologia_id_datos="iddatos";
    private static final String datos_patologia_id_patologia="idpatologias";

    public Bd_manager_datos_patologia(Context context) {
        connection = new bd_connection(context);
        connection.open();
        mycontext = context;
    }

    public Bd_manager_datos_patologia open_to_write() throws SQLException {
        database = connection.getWritableDatabase();
        return this;
    }
    public Bd_manager_datos_patologia open_to_read() throws SQLException {
        database = connection.getReadableDatabase();
        return this;
    }

    public void close(){
        connection.close();
    }

    public void insert_datos_patologia(String fecha, int id_datos, int id_patologia){
        ContentValues values = new ContentValues();
        this.open_to_write();
        values.put(datos_patologia_fecha, fecha);
        values.put(datos_patologia_id_datos, id_datos);
        values.put(datos_patologia_id_patologia, id_patologia);
        database.insert(Table_datos_patologia,null,values);
        this.close();
    }
    public void remove_patologia_from_dato(int id_datos, int id_patologia) {
        this.open_to_write();

        // Eliminar la relación entre el dato y la patología
        String whereClause = datos_patologia_id_datos + " = ? AND " + datos_patologia_id_patologia + " = ?";
        String[] whereArgs = new String[] { String.valueOf(id_datos), String.valueOf(id_patologia) };

        database.delete(Table_datos_patologia, whereClause, whereArgs);

        this.close();
    }

    public int get_Patologia(String fecha, int id_dato){
        int id=-1;
        this.open_to_read();
        String [] whereArgs = new String[]{fecha};
        try{
            Cursor result = database.rawQuery("Select idpatologias from datopatologia where fechaevaluacion = " + '"' + fecha + '"' + " and iddatos = " + id_dato,null);

            if(result.moveToFirst()){
                do {
                    int col= result.getColumnIndex(datos_patologia_id_patologia);
                    id = result.getInt(col);
                } while (result.moveToNext());
            }
            result.close();
            this.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return id;
    }

    public List<String> getListaPatologias(String fecha, int id_dato) {
        List<String> listaPatologias = new ArrayList<>();  // Lista para almacenar nombres de patologías
        this.open_to_read();  // Abre la base de datos en modo lectura

        // Definir la consulta SQL con un JOIN para obtener los nombres de las patologías
        String query = "SELECT p.nombre FROM datopatologia dp " +
                "INNER JOIN patologia p ON dp.idpatologias = p.id " +
                "WHERE dp.fechaevaluacion = " + '"' + fecha + '"'+ " AND dp.iddatos = " + id_dato;

        try {
            // Ejecutar la consulta usando parámetros
            Cursor result = database.rawQuery(query, null);

            // Recorrer el cursor y agregar cada nombre de patología a la lista
            if (result.moveToFirst()) {
                do {
                    String nombrePatologia = result.getString(result.getColumnIndexOrThrow("nombre"));
                    listaPatologias.add(nombrePatologia);
                } while (result.moveToNext());
            }

            result.close();  // Cierra el cursor
        } catch (Exception e) {
            e.printStackTrace();  // Manejo de excepciones
        } finally {
            this.close();  // Cierra la base de datos
        }

        return listaPatologias;  // Retorna la lista de patologías
    }


    public int getCantidadPatologias(String fecha, int id_dato) {
        int cantidad = 0;  // Variable para almacenar la cantidad de patologías
        this.open_to_read();  // Abre la base de datos en modo lectura

        // Definir la consulta SQL con parámetros
        String query = "SELECT COUNT(*) AS total_patologias FROM datopatologia " +
                "WHERE fechaevaluacion = ? AND iddatos = ?";

        try {
            // Ejecutar la consulta usando parámetros
            Cursor result = database.rawQuery(query, new String[]{fecha, String.valueOf(id_dato)});

            // Obtener el valor de la columna COUNT
            if (result.moveToFirst()) {
                cantidad = result.getInt(result.getColumnIndexOrThrow("total_patologias"));
            }

            result.close();  // Cierra el cursor
        } catch (Exception e) {
            e.printStackTrace();  // Manejo de excepciones
        } finally {
            this.close();  // Cierra la base de datos
        }

        return cantidad;  // Retorna la cantidad de patologías
    }

}
