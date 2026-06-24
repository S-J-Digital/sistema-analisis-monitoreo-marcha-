package com.example.visualizarfuncionalidades.Strategy.Clases;

import android.content.Context;

import com.example.database.Bd_manager_Sennales;
import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;
import com.example.visualizarfuncionalidades.Strategy.SignalDataStrategy;

import java.util.ArrayList;

public class AceZStrategy implements SignalDataStrategy {
    @Override
    public ArrayList<Float> obtenerDatos(Context context, People people, DataPeople dato, Sennales sennales) {
        Bd_manager_Sennales manager = new Bd_manager_Sennales(context);
        return manager.SennalesMismaFechaAceZ(people.getCi(), dato, sennales);
    }
}
