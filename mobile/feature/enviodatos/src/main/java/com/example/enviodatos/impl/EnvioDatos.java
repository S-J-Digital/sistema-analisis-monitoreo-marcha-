package com.example.enviodatos.impl;

import android.content.Context;
import android.util.Log;

import com.example.enviodatos.Dto.Datos.DatoParticipanteDto;
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

public class EnvioDatos implements EnvioStrategy {
    private ApiService apiService;
    private List<DataPeople> dataPeopleList;
    private People people;

    private Long idparticipante;
    private Long iddato;
    private Long idpatologia;
    private Context mycontext;


    public EnvioDatos(ApiService apiService, People people, Long idparticipante, Context mycontext) {
        this.apiService = apiService;
        this.people = people;
        this.idparticipante = idparticipante;
        this.mycontext = mycontext;
    }



    public EnvioDatos(ApiService apiService, Long idparticipante) {
        this.apiService = apiService;
        this.idparticipante = idparticipante;
    }

    @Override
    public void enviarInsert(Context context) {
        dataPeopleList = ObtenerDatos.getDataPeople(context, people);
        for (DataPeople dp : dataPeopleList) {
            Call<ResponseBody> call = apiService.enviardatoparticipante(new DatoParticipanteDto(dp,idparticipante));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                   if(response.isSuccessful()){
                        obtenerIdDato(dp,idparticipante,context);
                        Log.d("EnvioDato","Se envio el dato");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void enviarUpdate(Object object, String fecha, Long id, Context context) {
        obtenerIdbynameUpdate(fecha,object,null,null);
    }

    @Override
    public void enviarDelete(Long id) {

    }

    @Override
    public void obtenerIdbynameUpdate(String fecha, Object object, Context context, String operacion) {
        if(object instanceof DataPeople){
            DataPeople dpupdate = (DataPeople) object;
            Call<Integer> call = apiService.obtenerIdbyparticipanteAndFecha(idparticipante,fecha);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.isSuccessful()){
                        iddato = Long.valueOf(response.body());
                        if(operacion.equals("modificar")){
                            updatedato(iddato,new DatoParticipanteDto(dpupdate,idparticipante), dpupdate);
                        }else{
                            enviarDelete(iddato);
                        }

                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.e("EnvioDato", "Error al obtener id del dato: " + t.getMessage());
                }
            });
        }
    }

    @Override
    public void obtenerIdbynameDelete(String nombre) {

    }


    private void obtenerIdDato(DataPeople dp, Long idparticipante, Context context){
        Call<Integer> call = apiService.obtenerIdbyparticipanteAndFecha(idparticipante,dp.getDate());
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    iddato = Long.valueOf(response.body());
                    Log.e("EnvioDato", "El id es:"+iddato);

                    envioPatologia(dp, iddato,context);
                    envioSennales(dp, iddato,context);


                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("EnvioDato", "Error al obtener id del dato: " + t.getMessage());
            }
        });
    }

    private void envioPatologia(DataPeople dp, Long dato, Context context){
        ApiService apiService = RetrofitClient.getApiService(context);
        EnvioStrategy strategy = new EnviarPatologia(dp,people,apiService,dato);
        GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
        gestorEnvioDatos.enviar(context);
    }

    private void envioSennales(DataPeople dp, Long dato, Context context){
        ApiService apiService = RetrofitClient.getApiServiceSennal(context);
        EnvioStrategy strategy = new EnviarSennales(apiService,dp,people,dato);
        GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
        gestorEnvioDatos.enviar(context);
    }

    private void updatedato(Long iddato, DatoParticipanteDto datoParticipanteDto,DataPeople dp){
        Call<ResponseBody> call = apiService.modificarDatoParticipante(iddato, datoParticipanteDto);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    if(datoParticipanteDto.getPatologia() == false){
                        envioRelacion(dp,iddato,mycontext);
                    }

                    Log.e("EnvioDato", "Se modificó el dato ");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void envioRelacion(DataPeople dp, Long dato, Context context){
        ApiService apiService = RetrofitClient.getApiService(context);
        EnvioStrategy strategy = new CrearRelacion(dp,apiService,iddato);
        GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
        gestorEnvioDatos.obtenerIdDelete(null);
    }
}
