package com.example.enviodatos.impl;

import android.util.Log;

import com.example.enviodatos.Callback.LoginCallback;
import com.example.enviodatos.Dto.UserLogin;
import com.example.enviodatos.service.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login {
    private ApiService apiService;

    public Login(ApiService apiService) {
        this.apiService = apiService;
    }

    public void login(UserLogin userLogin, LoginCallback callback){
        apiService.login(userLogin).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null){
                   try{
                       String token = response.body().string();
                       callback.onSuccess(token);
                       Log.e("Token:", token);
                   }catch (Exception e){
                       callback.onError(e.getMessage());
                   }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });

    }
}
