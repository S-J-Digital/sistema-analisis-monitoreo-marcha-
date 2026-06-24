package com.example.enviodatos.impl;

import android.content.Context;
import android.util.Log;

import com.example.enviodatos.Dto.Patologia.PatologiaDto;
import com.example.enviodatos.Dto.Patologia.RelaciónDatoPatologiaDto;
import com.example.enviodatos.obtenerdesdeBD.ObtenerDatos;
import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;
import com.example.model.DataPeople;
import com.example.model.People;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearRelacion implements EnvioStrategy {
    private String patologia;
    private DataPeople dataPeople;
    private ApiService apiService;
    private Long iddato;
    private Long idpatologia;

    public CrearRelacion(String patologia, DataPeople dataPeople, ApiService apiService, Long iddato, Long idpatologia) {
        this.patologia = patologia;
        this.dataPeople = dataPeople;
        this.apiService = apiService;
        this.iddato = iddato;
        this.idpatologia = idpatologia;
    }
    public CrearRelacion(DataPeople dataPeople, ApiService apiService, Long iddato) {
        this.dataPeople = dataPeople;
        this.apiService = apiService;
        this.iddato = iddato;
    }
    @Override
    public void enviarInsert(Context context) {
        crearrelacion(dataPeople,iddato,idpatologia);
    }

    @Override
    public void enviarUpdate(Object object, String name, Long id, Context context) {

    }

    @Override
    public void enviarDelete(Long id) {
        eliminarrelación();
    }

    @Override
    public void obtenerIdbynameUpdate(String nombre, Object object, Context context, String operacion) {

    }

    @Override
    public void obtenerIdbynameDelete(String nombre) {

    }

    private void crearrelacion(DataPeople dp, Long iddato, Long idpatologia){
        Call<ResponseBody> call = apiService.crearrelacionpatologiadato(new RelaciónDatoPatologiaDto(iddato,idpatologia,dp.getDate()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("EnvioPatología","Se creo la relacion");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void eliminarrelación(){
        Call<ResponseBody> call = apiService.eliminarRelacion(iddato, dataPeople.getDate());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("EnvioPatología","Se eliminó la relacion");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
