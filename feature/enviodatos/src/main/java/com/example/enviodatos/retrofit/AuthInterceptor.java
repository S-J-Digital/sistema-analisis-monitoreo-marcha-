package com.example.enviodatos.retrofit;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.user.GetUser;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private String token;

    public AuthInterceptor(String token) {
        this.token = token;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();
        return chain.proceed(request);
    }
}
