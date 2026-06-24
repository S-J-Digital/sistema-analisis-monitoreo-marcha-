package com.example.interfazinsertar;

import android.content.Context;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.database.Bd_manager_Participante;
import com.example.enviodatos.gestionStrategy.GestorEnvioDatos;
import com.example.enviodatos.impl.EnviarParticipante;
import com.example.enviodatos.retrofit.RetrofitClient;
import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;
import com.example.model.People;
import com.example.user.GetUser;


public class ModificarParticipante {
    public static void modificar_participante(Context context, EditText nombre, EditText ci, EditText telefono, RadioButton femenino,
                                              RadioButton masculino, People people){

        if(femenino.isChecked()){
            People p = null;
            try {
                Bd_manager_Participante manager = new Bd_manager_Participante(context);
                manager.person_update(people.getCi(), nombre.getText().toString(), ci.getText().toString(),
                        femenino.getText().toString(), telefono.getText().toString());
                p = new People(nombre.getText().toString(),ci.getText().toString(),femenino.getText().toString(),telefono.getText().toString());
                enviarmodificacion(context, p, people.getCi());
            }catch (Exception e){
                Toast.makeText(context, p.getCi(), Toast.LENGTH_SHORT).show();
            }

        }else{
            People p = null;
            Bd_manager_Participante manager = new Bd_manager_Participante(context);
            manager.person_update(people.getCi(),nombre.getText().toString(),ci.getText().toString(),
                    masculino.getText().toString(), telefono.getText().toString());
            p = new People(nombre.getText().toString(),ci.getText().toString(),masculino.getText().toString(),telefono.getText().toString());
            enviarmodificacion(context, p, people.getCi());
        }


    }


    public static void enviarmodificacion(Context context, People people, String ci_antiguo){
        try{
            ApiService apiService = RetrofitClient.getApiService(context);
            String user = GetUser.leerValor(context, "user");
            EnvioStrategy strategy = new EnviarParticipante(user,apiService);
            GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
            String operacion = "modificar";
            gestorEnvioDatos.obtenerIdUpdate(ci_antiguo,people,context,operacion);
        }catch (Exception e){
            Toast.makeText(context, people.getCi(), Toast.LENGTH_SHORT).show();
        }

    }
}