package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Bd_manager_usuario_participante {
    private bd_connection connection;
    private SQLiteDatabase database;
    private final Context mycontext;

    /// Tabla patologia
    private static final String Table_usuario_paciente="usuarioparticipante";
    private static final String usuario_participante_idusuario="id_usuario";
    private static final String usuario_participante_idparticipante="id_participante";

    public Bd_manager_usuario_participante(Context context) {
        connection = new bd_connection(context);
        connection.open();
        mycontext = context;
    }

    public Bd_manager_usuario_participante open_to_write() throws SQLException {
        database = connection.getWritableDatabase();
        return this;
    }
    public Bd_manager_usuario_participante open_to_read() throws SQLException {
        database = connection.getReadableDatabase();
        return this;
    }

    public void close(){
        connection.close();
    }

    public void insert_usuario_paciente(int id_usuario, int id_participante){
        ContentValues values = new ContentValues();
        this.open_to_write();
        values.put(usuario_participante_idusuario, id_usuario);
        values.put(usuario_participante_idparticipante, id_participante);
        database.insert(Table_usuario_paciente,null,values);
        this.close();
    }

}
