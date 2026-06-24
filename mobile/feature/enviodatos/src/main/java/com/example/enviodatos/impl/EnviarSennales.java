package com.example.enviodatos.impl;

import android.content.Context;
import android.util.Log;

import com.example.enviodatos.Dto.Datos.DatoParticipanteDto;
import com.example.enviodatos.Dto.Sennal.ListaSenalesDTO;
import com.example.enviodatos.Dto.Sennal.SennalDto;
import com.example.enviodatos.obtenerdesdeBD.ObtenerDatos;
import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;
import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnviarSennales implements EnvioStrategy {
    private ApiService apiService;
    private List<Sennales> dataSennalList;
    private DataPeople dataPeople;
    private People people;
    private Long iddato;

    public EnviarSennales(ApiService apiService, DataPeople dataPeople, People people, Long iddato) {
        this.apiService = apiService;
        this.dataPeople = dataPeople;
        this.people = people;
        this.iddato = iddato;
    }

    @Override
    public void enviarInsert(Context context) {
        dataSennalList = ObtenerDatos.getSennales(context,dataPeople,people);
        List<SennalDto> listaDtos = new ArrayList<>();


        for (Sennales s : dataSennalList) {
            listaDtos.add(new SennalDto(s, iddato));
        }
        int iddato_senal= ObtenerDatos.getIdDato(context,dataPeople,people);
        ListaSenalesDTO listaSennalesDto = new ListaSenalesDTO(listaDtos,ObtenerDatos.getPatologia(context,dataPeople,iddato_senal));

        Gson gson = new GsonBuilder()
                .serializeSpecialFloatingPointValues()
                .registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context3) ->
                        new JsonPrimitive(src))
                .setPrettyPrinting()
                .create();

        String json = gson.toJson(listaSennalesDto);
        Log.d("JSON_FINAL", json);

        Call<ResponseBody> call = apiService.enviarSennales(listaSennalesDto);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("EnvioSeñales", "Se enviaron las señales");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                int iddato_senal= ObtenerDatos.getIdDato(context,dataPeople,people);
                List<String> enferm = ObtenerDatos.getPatologia(context,dataPeople,iddato_senal);
                Log.e("EnvioSeñales", "Las enfermedades son: "+enferm + "El id es:"+iddato_senal);
            }
        });

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
}
