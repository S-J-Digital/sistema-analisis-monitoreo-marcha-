package com.example.funcionalidadesbluetooth;

import com.example.model.Sennales;

public interface JoyConListener {
    void onConectado();
    void onDesconectado();
    void onSennalRecibida(Sennales sennales);
    void onError(String mensaje);

}
