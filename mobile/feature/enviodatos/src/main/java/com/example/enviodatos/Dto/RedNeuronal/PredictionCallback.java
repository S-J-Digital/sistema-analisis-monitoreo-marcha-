package com.example.enviodatos.Dto.RedNeuronal;

public interface PredictionCallback {
    void onSuccess(Prediccion resultado);

    void onError(String error);
}
