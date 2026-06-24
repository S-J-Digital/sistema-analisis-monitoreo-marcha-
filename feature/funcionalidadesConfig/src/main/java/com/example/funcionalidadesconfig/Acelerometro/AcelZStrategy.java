package com.example.funcionalidadesconfig.Acelerometro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

import com.example.database.Bd_manager_Sennales;
import com.example.funcionalidadesconfig.SignalStrategy;
import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class AcelZStrategy implements SignalStrategy {
    @Override
    public Bitmap generateSignalChart(Context context, People people, DataPeople dato, Sennales sennales) {
        LineChart acelerometro = new LineChart(context);
        Bd_manager_Sennales manager = new Bd_manager_Sennales(context);
        ArrayList<Float> sennalesArrayList = manager.SennalesMismaFechaAceZ(people.getCi(), dato, sennales);
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < sennalesArrayList.size(); i++) {
            values.add(new Entry(i, sennalesArrayList.get(i)));
        }
        LineDataSet dataSet = new LineDataSet(values, "Acelerometro en Z");
        dataSet.setColor(Color.RED);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setValueTextColor(Color.BLACK);
        LineData lineData = new LineData(dataSet);
        acelerometro.setData(lineData);

        acelerometro.getXAxis().setTextSize(6f);
        acelerometro.getAxisLeft().setTextSize(6f);
        acelerometro.getAxisRight().setTextSize(6f);
        acelerometro.getLegend().setTextSize(6f);

        return getChartBitmap(acelerometro);
    }

    private Bitmap getChartBitmap(LineChart chart) {
        chart.measure(View.MeasureSpec.makeMeasureSpec(530, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(250, View.MeasureSpec.EXACTLY));
        chart.layout(0, 0, chart.getMeasuredWidth(), chart.getMeasuredHeight());
        chart.setDrawingCacheEnabled(true);
        chart.buildDrawingCache();
        Bitmap chartBitmap = Bitmap.createBitmap(chart.getDrawingCache());
        chart.setDrawingCacheEnabled(false);
        chart.invalidate();
        return chartBitmap;
    }
}

