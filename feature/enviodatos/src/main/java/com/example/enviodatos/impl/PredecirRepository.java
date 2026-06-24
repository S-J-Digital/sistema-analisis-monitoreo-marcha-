package com.example.enviodatos.impl;

import android.util.Log;

import com.example.enviodatos.Dto.RedNeuronal.ModeloRequest;
import com.example.enviodatos.Dto.RedNeuronal.Prediccion;
import com.example.enviodatos.Dto.RedNeuronal.PredictionCallback;
import com.example.enviodatos.service.ApiService;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PredecirRepository {
    private ApiService apiService;

    public PredecirRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void predecir(ModeloRequest request,
                         PredictionCallback callback) {

        apiService.predecir(request).enqueue(new Callback<List<String>>() {

            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<String> rawList = response.body();
                   Prediccion resultado = new Prediccion();

                    try {
                        // Procesamos hasta 3 resultados si existen
                        for (int i = 0; i < rawList.size(); i++) {
                            String item = rawList.get(i).replace("%", "").trim();

                            // Buscamos el último espacio para separar nombre de número
                            int spaceIdx = item.lastIndexOf(" ");
                            String nombre = item.substring(0, spaceIdx);
                            double prob = Double.parseDouble(item.substring(spaceIdx + 1).replace(",","."));

                            if (i == 0) {
                                resultado.setEnfermedad1(nombre);
                                resultado.setProb1(prob);
                            } else if (i == 1) {
                                resultado.setEnfermedad2(nombre);
                                resultado.setProb2(prob);
                            } else if (i == 2) {
                                resultado.setEnfermedad3(nombre);
                                resultado.setProb3(prob);
                            }
                        }

                        // Rellenar valores por defecto para evitar Nulls en el PDF
                        if (rawList.size() < 2) { resultado.setEnfermedad2("N/A"); resultado.setProb2(0.0); }
                        if (rawList.size() < 3) { resultado.setEnfermedad3("N/A"); resultado.setProb3(0.0); }

                        callback.onSuccess(resultado);

                    } catch (Exception e) {
                        callback.onError("Error de formato en: " + e.getMessage() );
                    }
                } else {
                    callback.onError("Error en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call,
                                  Throwable t) {

                callback.onError(t.getMessage());
            }
        });
    }

}
