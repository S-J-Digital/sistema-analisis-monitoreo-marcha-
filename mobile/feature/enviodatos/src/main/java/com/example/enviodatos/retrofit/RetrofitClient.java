package com.example.enviodatos.retrofit;

import android.content.Context;

import com.example.enviodatos.service.ApiService;
import com.example.user.GetUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String URL = "http://10.206.240.125:8081";
    private static Retrofit retrofit = null;

    public static ApiService getApiService(Context context){
        if(retrofit == null){

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Muestra el JSON enviado y recibido

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

          retrofit = new Retrofit.Builder()
                  .baseUrl(URL)
                  .addConverterFactory(GsonConverterFactory.create())
                  .build();
        }
        return retrofit.create(ApiService.class);
    }

    public static ApiService getApiServiceSennal(Context contex){
        if(retrofit == null){

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Muestra el JSON enviado y recibido

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(1, TimeUnit.MINUTES) // hasta 3 minutos para conectar
                    .readTimeout(20, TimeUnit.MINUTES)    // hasta 5 minutos para leer
                    .writeTimeout(5, TimeUnit.MINUTES)   // hasta 5 minutos para enviar
                    .build();

            Gson gson = new GsonBuilder()
                    .serializeSpecialFloatingPointValues() // por si hay NaN o Infinity
                    .registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(src)) // No redondea, no trunca, no formatea
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client) // Importante: usar el cliente con timeout extendido
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
