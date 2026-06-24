package com.example.enviodatos.impl;

import android.content.Context;
import android.provider.ContactsContract;

import com.example.enviodatos.Dto.Patologia.PatologiaDto;
import com.example.enviodatos.gestionStrategy.GestorEnvioDatos;
import com.example.enviodatos.obtenerdesdeBD.ObtenerDatos;
import com.example.enviodatos.retrofit.RetrofitClient;
import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;
import com.example.model.DataPeople;
import com.example.model.People;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnviarPatologia implements EnvioStrategy {
    private DataPeople dataPeople;
    private People people;
    private ApiService apiService;
    private Long iddato;
    private Long idpatologia;

    public EnviarPatologia(DataPeople dataPeople, People people, ApiService apiService,Long iddato) {
        this.dataPeople = dataPeople;
        this.people = people;
        this.apiService = apiService;
        this.iddato = iddato;
    }

    @Override
    public void enviarInsert(Context context) {
        enviarpatologia(context,dataPeople,people);
    }

    @Override
    public void enviarUpdate(Object object, String name, Long id, Context context) {

    }

    @Override
    public void enviarDelete(Long id) {

    }

    @Override
    public void obtenerIdbynameUpdate(String nombre, Object object, Context context, String operacion) {

    }

    @Override
    public void obtenerIdbynameDelete(String nombre) {

    }

    public void enviarpatologia(Context context,DataPeople dp, People people){
        List<String> patologia = ObtenerDatos.getPatologia(context,dp,ObtenerDatos.getIdDato(context, dp, people));
        for (String d: patologia) {
            Call<ResponseBody> call = apiService.enviarpatologia(new PatologiaDto(d));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        obtenerIdPatologia(d,dp,context);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    public void obtenerIdPatologia(String nombre,DataPeople dataPeople, Context context){
        Call<Integer> call = apiService.obtenerIdbynombre(nombre);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    idpatologia = Long.valueOf(response.body());
                    crearrelacion(nombre,dataPeople,iddato,idpatologia,context);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    public void crearrelacion(String d, DataPeople dp, Long iddato, Long idpatologia, Context context){
        ApiService apiService = RetrofitClient.getApiService(context);
        EnvioStrategy strategy = new CrearRelacion(d,dp,apiService,iddato,idpatologia);
        GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
        gestorEnvioDatos.enviar(context);
    }
}
