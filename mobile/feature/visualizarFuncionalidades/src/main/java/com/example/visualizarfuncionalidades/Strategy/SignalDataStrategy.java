package com.example.visualizarfuncionalidades.Strategy;

import android.content.Context;

import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;

import java.util.ArrayList;

public interface SignalDataStrategy {
    ArrayList<Float> obtenerDatos(Context context, People people, DataPeople dato, Sennales sennales);
}
