package com.example.enviodatos.Callback;

import com.example.enviodatos.Dto.RedNeuronal.Prediccion;

public interface LoginCallback {
    void onSuccess(String token);

    void onError(String error);
}
