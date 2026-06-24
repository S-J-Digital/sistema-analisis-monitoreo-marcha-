package com.example.enviodatos.impl;

import android.content.Context;
import android.util.Log;

import com.example.database.Bd_manager_Rol;
import com.example.enviodatos.Dto.UserName;
import com.example.enviodatos.Dto.Userdto;
import com.example.enviodatos.gestionStrategy.GestorEnvioDatos;
import com.example.enviodatos.obtenerdesdeBD.ObtenerDatos;
import com.example.enviodatos.retrofit.RetrofitClient;
import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;
import com.example.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnviarUsuario implements EnvioStrategy {
    private ApiService apiService;
    private Long userId;

    public EnviarUsuario(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void enviarInsert(Context context) {
        Bd_manager_Rol manager_rol = new Bd_manager_Rol(context);
        List<User> userList = ObtenerDatos.getUser(context);
        for (User user : userList) {
            Userdto userdto = new Userdto(user, manager_rol);

            Call<ResponseBody> call = apiService.enviarusuario(userdto);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        envioParticipante(context,user.getName());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("EnvioBatch", "Error al enviar usuario almacenado: " + t.getMessage() + " la respuesta es:" + call.timeout());
                }
            });
        }
    }

    @Override
    public void enviarUpdate(Object object, String name, Long id , Context context) {
        Bd_manager_Rol manager_rol = new Bd_manager_Rol(context);
        User user = null;
        if(object instanceof User){
            user = (User) object;

            Userdto userdto = new Userdto(user,manager_rol);
            Call<ResponseBody> call = apiService.modificarusuario(id,userdto);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("EnvioBatch", "Usuario enviado");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("EnvioBatch", "Error al enviar usuario almacenado: " + t.getMessage());
                }
            });
        }

    }

    @Override
    public void enviarDelete(Long id) {
        Call<ResponseBody> call = apiService.eliminarusuario(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("EnvioBatch", "se eliminó el usuario");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void obtenerIdbynameUpdate(String nombre, Object object, Context context, String operacion) {
        Call<UserName> call = apiService.buscarIdbynombre(nombre);
        call.enqueue(new Callback<UserName>() {
            @Override
            public void onResponse(Call<UserName> call, Response<UserName> response) {
                if (response.isSuccessful()) {
                    userId = response.body().getId();
                    if(object instanceof User){
                        User user = (User) object;
                        enviarUpdate(user,nombre,userId,context);
                    }

                }
            }

            @Override
            public void onFailure(Call<UserName> call, Throwable t) {
                Log.e("EnvioBatch", "Error al enviar usuario almacenado: " + t.getMessage().toString());
            }
        });
    }

    @Override
    public void obtenerIdbynameDelete(String nombre) {
        Call<UserName> call = apiService.buscarIdbynombre(nombre);
        call.enqueue(new Callback<UserName>() {
            @Override
            public void onResponse(Call<UserName> call, Response<UserName> response) {
                if (response.isSuccessful()) {
                    userId = response.body().getId();
                    enviarDelete(userId);
                }
            }

            @Override
            public void onFailure(Call<UserName> call, Throwable t) {
                Log.e("EnvioBatch", "Error al enviar usuario almacenado: " + t.getMessage());
            }
        });
    }

    public void envioParticipante(Context context, String user){
        ApiService apiService = RetrofitClient.getApiService(context);
        EnvioStrategy strategy = new EnviarParticipante(user,apiService);
        GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
        gestorEnvioDatos.enviar(context);
    }
}
