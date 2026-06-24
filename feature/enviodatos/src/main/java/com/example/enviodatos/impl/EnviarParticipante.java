package com.example.enviodatos.impl;

import android.content.Context;
import android.util.Log;

import com.example.enviodatos.Dto.Participante.ParticipanteUpdate;
import com.example.enviodatos.Dto.Participante.Participantedto;
import com.example.enviodatos.Dto.UserName;
import com.example.enviodatos.gestionStrategy.GestorEnvioDatos;
import com.example.enviodatos.obtenerdesdeBD.ObtenerDatos;
import com.example.enviodatos.retrofit.RetrofitClient;
import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;
import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnviarParticipante implements EnvioStrategy {
    private String usuario;
    private Long idusuario;
    private Long participanteID;
    private ApiService apiService;

    public EnviarParticipante(String usuario, ApiService apiService) {
        this.usuario = usuario;
        this.apiService = apiService;
    }

    @Override
    public void enviarInsert(Context context) {
        Call<UserName> call = apiService.buscarIdbynombre(usuario);
        call.enqueue(new Callback<UserName>() {
            @Override
            public void onResponse(Call<UserName> call, Response<UserName> response) {
                if (response.isSuccessful()) {
                   idusuario = response.body().getId();
                    participanteInsert(idusuario, context);
                }
            }

            @Override
            public void onFailure(Call<UserName> call, Throwable t) {
                Log.e("EnvioBatch", "Error al enviar usuario almacenado: " + t.getMessage());
            }
        });

    }

    @Override
    public void enviarUpdate(Object object, String name, Long id, Context context) {
        if(object instanceof People){
            ParticipanteUpdate participanteUpdate = new ParticipanteUpdate((People) object);
            Call<ResponseBody> call = apiService.modificarParticipante(id,participanteUpdate);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("EnvioParticipante", "Participante modificado");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

    }

    @Override
    public void enviarDelete(Long id) {
        Call<ResponseBody> call = apiService.eliminarparticipante(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("EnvioParticipante", "se eliminó el participante");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    @Override
    public void obtenerIdbynameUpdate(String ci, Object object, Context context, String operacion) {
     Call<Integer> call = apiService.buscarIdbyCi(ci);
     call.enqueue(new Callback<Integer>() {
         @Override
         public void onResponse(Call<Integer> call, Response<Integer> response) {
            if(response.isSuccessful()){
                participanteID = Long.valueOf(response.body());
                if(operacion.equals("modificar")) {
                    enviarUpdate(object, null, participanteID, context);
                }else if(operacion.equals("eliminar")){
                    enviarDelete(participanteID);
                }else if(operacion.equals("eliminardato")){
                    if(object instanceof DataPeople){
                        envioDatoDelete( participanteID,(DataPeople) object,context);
                    }

                }else{
                    if (object instanceof DataPeople){
                        envioDatoUpdate((DataPeople) object,participanteID,context);
                    }
                }
            }
         }

         @Override
         public void onFailure(Call<Integer> call, Throwable t) {

         }
     });
    }

    public void participanteInsert(Long iduser, Context context){
        List<People> peopleList = ObtenerDatos.getPeople(context,usuario);
        for (People p: peopleList) {
            Participantedto participantedto = new Participantedto(p, iduser);

            Call<ResponseBody> call = apiService.enviarparticipante(participantedto);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        obtenerId(p.getCi(),p,context);
                        Log.d("EnvioParticipante", "Participante enviado");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("EnvioParticipante", "Error al enviar el participante almacenado: " + t.getMessage());
                }
            });
        }
    }

    @Override
    public void obtenerIdbynameDelete(String ci) {
        Call<Integer> call = apiService.buscarIdbyCi(ci);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    participanteID = Long.valueOf(response.body());
                    enviarDelete(participanteID);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    public void obtenerId(String ci, People people, Context context){
        Call<Integer> call = apiService.buscarIdbyCi(ci);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    participanteID = Long.valueOf(response.body());
                    envioDato(people, participanteID,context);


                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    public void envioDato(People people, Long idparticipante, Context context){
        ApiService apiService = RetrofitClient.getApiService(context);
        EnvioStrategy strategy = new EnvioDatos(apiService, people, idparticipante,context);
        GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
        gestorEnvioDatos.enviar(context);
    }

    public void envioDatoUpdate(DataPeople dato, Long idparticipante, Context context){
        ApiService apiService = RetrofitClient.getApiService(context);
        EnvioStrategy strategy = new EnvioDatos(apiService, idparticipante);
        GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
        gestorEnvioDatos.obtenerIdUpdate(dato.getDate(), dato,context,"modificar");
    }

    public void envioDatoDelete(Long idparticipante, DataPeople dato,Context context){
        ApiService apiService = RetrofitClient.getApiService(context);
        EnvioStrategy strategy = new EnvioDatos(apiService, idparticipante);
        GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
        gestorEnvioDatos.obtenerIdUpdate(dato.getDate(), dato,context,null);
    }
}
