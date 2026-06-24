package com.example.enviodatos.gestionStrategy;

import android.content.Context;

import com.example.enviodatos.service.EnvioStrategy;

public class GestorEnvioDatos {
    private EnvioStrategy strategy;

    public GestorEnvioDatos(EnvioStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(EnvioStrategy strategy) {
        this.strategy = strategy;
    }

    public void enviar(Context context) {
        strategy.enviarInsert(context);
    }

    public void obtenerIdUpdate(String name, Object object, Context context, String operacion) {
        strategy.obtenerIdbynameUpdate(name,object, context,operacion);
    }

    public void obtenerIdDelete(String name) {
        strategy.obtenerIdbynameDelete(name);
    }
}
