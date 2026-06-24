package com.example.visualizarfuncionalidades.Strategy.Control;

import android.content.Context;
import android.graphics.Color;

import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;
import com.example.visualizarfuncionalidades.Strategy.SignalDataStrategy;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class VisualizadorGrafica {

    public static void llenarGrafica(
            Context context,
            LineChart chart,
            People people,
            DataPeople dato,
            Sennales sennales,
            SignalDataStrategy strategy
    ) {
        ArrayList<Float> datos = strategy.obtenerDatos(context, people, dato, sennales);
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < datos.size(); i++) {
            values.add(new Entry(i, datos.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(values, "");
        dataSet.setColor(Color.RED);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // Refresca el gráfico
    }
}
