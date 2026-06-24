package com.example.enviodatos.service;

import android.content.Context;

public interface EnvioStrategy {
    void enviarInsert(Context context);
    void enviarUpdate(Object object, String name, Long id, Context context);
    void enviarDelete(Long id);
    void obtenerIdbynameUpdate(String nombre, Object object, Context context, String operacion);
    void obtenerIdbynameDelete(String nombre);
}
