package com.example.visualizarfuncionalidades;

import android.content.Context;
import android.widget.TextView;

import com.example.database.Bd_manager_Sennales;
import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;

import java.util.ArrayList;

public class TemperaturaPromedio {

    public static void LlenarTemp(Context context, TextView temperatura, People people, DataPeople dato, Sennales sennales){
        Bd_manager_Sennales manager = new Bd_manager_Sennales(context);
        ArrayList<Float> sennalesArrayList = manager.SennalesMismaFechaTemp(people.getCi(),dato,sennales);
        float sum =0;
        for(int i = 0; i<sennalesArrayList.size();i++){
            sum+=sennalesArrayList.get(i);
        }
        Float tempProm = sum/sennalesArrayList.size();
        temperatura.setText(String.valueOf(tempProm).substring(0,5));
    }
}
