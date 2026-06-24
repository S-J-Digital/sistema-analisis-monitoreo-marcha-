package com.example.interfazinsertar;

import android.content.Context;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.database.Bd_manager_Participante;
import com.example.user.GetUser;

public class InsertarParticipante {

    public static void Guardar_participante(Context context, EditText nombre, EditText ci, EditText telefono, RadioButton femenino,
                                            RadioButton masculino){

        String user = GetUser.leerValor(context, "user");
        //Guardar en la base de datos

        if(femenino.isChecked()){
            Bd_manager_Participante manager = new Bd_manager_Participante(context);
            manager.person_insert(user,nombre.getText().toString(),ci.getText().toString(),
                    femenino.getText().toString(), telefono.getText().toString());
        }else{
            Bd_manager_Participante manager = new Bd_manager_Participante(context);
            manager.person_insert(user,nombre.getText().toString(),ci.getText().toString(),
                    masculino.getText().toString(), telefono.getText().toString());
        }
    }

}
