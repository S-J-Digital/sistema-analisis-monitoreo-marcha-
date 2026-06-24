package com.example.visualizarfuncionalidades;

import android.content.Context;
import android.graphics.Color;

import com.example.database.Bd_manager_Sennales;
import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class VisualizarAceZ {

    public static void LlenarGraficaAceZ(Context context, LineChart acelerometro, People people, DataPeople dato, Sennales sennales){
        Bd_manager_Sennales manager = new Bd_manager_Sennales(context);
        ArrayList<Float> sennalesArrayList = manager.SennalesMismaFechaAceZ(people.getCi(),dato,sennales);
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < sennalesArrayList.size(); i++) {
            // Añade los datos del acelerómetro al gráfico.
            values.add(new Entry(i, sennalesArrayList.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(values, "");
        dataSet.setColor(Color.RED);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        acelerometro.setData(lineData);
        acelerometro.invalidate(); // Refresca el gráfico
    }
}
