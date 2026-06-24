package com.example.interfazinsertar;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.database.Bd_manager_DatosParticipante;
import com.example.database.Bd_manager_datos_patologia;
import com.example.database.Bd_manager_patologias;
import com.example.enviodatos.gestionStrategy.GestorEnvioDatos;
import com.example.enviodatos.impl.EnviarParticipante;
import com.example.enviodatos.retrofit.RetrofitClient;
import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;
import com.example.model.DataPeople;
import com.example.model.People;
import com.example.user.GetUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModificarDatos {

    public static void Modificar_datos(Context context, EditText editTextEdad, EditText editTextCalzado,
                                EditText editTextMedicion, EditText editTextPierna, EditText editTextAltura, EditText editTextObservacion,
                                RadioButton radioButtonsi, RadioButton radioButtonno, EditText ci, DataPeople datos){

       try{
           if(radioButtonsi.isChecked()){
               Bd_manager_DatosParticipante manager = new Bd_manager_DatosParticipante(context);
               manager.data_update(ci.getText().toString(),datos.getDate(),Integer.valueOf(editTextEdad.getText().toString()),
                       1,Double.valueOf(editTextCalzado.getText().toString()),
                       Double.valueOf(editTextMedicion.getText().toString()),Double.valueOf(editTextPierna.getText().toString()),
                       Double.valueOf(editTextAltura.getText().toString()));
               DataPeople dataPeople = new DataPeople(datos.getDate(), datos.getHour(),Integer.valueOf(editTextEdad.getText().toString()),1,Double.valueOf(editTextCalzado.getText().toString()),
                       Double.valueOf(editTextMedicion.getText().toString()),Double.valueOf(editTextPierna.getText().toString()),
                       Double.valueOf(editTextAltura.getText().toString()), Collections.singletonList(editTextObservacion.getText().toString()));
               enviarmodificacion(context,dataPeople,ci.getText().toString());
           }else{
               Bd_manager_DatosParticipante manager = new Bd_manager_DatosParticipante(context);
               Bd_manager_datos_patologia manager_datos_patologia = new Bd_manager_datos_patologia(context);
               manager.data_update(ci.getText().toString(),datos.getDate(),Integer.valueOf(editTextEdad.getText().toString()),
                       0,Double.valueOf(editTextCalzado.getText().toString()),
                       Double.valueOf(editTextMedicion.getText().toString()),Double.valueOf(editTextPierna.getText().toString()),
                       Double.valueOf(editTextAltura.getText().toString()));
               eliminarRelacion( context, ci.getText().toString(), datos.getDate(),  manager_datos_patologia,  manager);
               DataPeople dataPeople = new DataPeople(datos.getDate(), datos.getHour(),Integer.valueOf(editTextEdad.getText().toString()),1,Double.valueOf(editTextCalzado.getText().toString()),
                       Double.valueOf(editTextMedicion.getText().toString()),Double.valueOf(editTextPierna.getText().toString()),
                       Double.valueOf(editTextAltura.getText().toString()), Collections.singletonList(editTextObservacion.getText().toString()));
               enviarmodificacion(context,dataPeople,ci.getText().toString());

           }
       }catch(Exception e){
           Log.d("Modificar datos", e.getMessage());
       }
    }

    public static void eliminarRelacion(Context context, String ci, String date, Bd_manager_datos_patologia manager_datos_patologia, Bd_manager_DatosParticipante manager){
        Bd_manager_patologias manager_patologias = new Bd_manager_patologias(context);
        int dato = manager.data_getID(ci,date);
        List<String> enfermedades = manager_datos_patologia.getListaPatologias(date,dato);
        for (String e: enfermedades) {
            int idenfermedad= manager_patologias.getpatologiabyname(e);
            manager_datos_patologia.remove_patologia_from_dato(dato,idenfermedad);
        }

    }

    public static void enviarmodificacion(Context context, DataPeople people, String ci){
        try{
            ApiService apiService = RetrofitClient.getApiService(context);
            String user = GetUser.leerValor(context, "user");
            EnvioStrategy strategy = new EnviarParticipante(user,apiService);
            GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
            String operacion = "modificardato";
            gestorEnvioDatos.obtenerIdUpdate(ci,people,context,user);
        }catch (Exception e){
            Toast.makeText(context, people.getDate(), Toast.LENGTH_SHORT).show();
        }

    }

}
