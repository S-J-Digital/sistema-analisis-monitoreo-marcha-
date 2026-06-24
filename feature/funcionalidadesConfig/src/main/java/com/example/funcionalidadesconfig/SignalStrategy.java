package com.example.funcionalidadesconfig;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;

public interface SignalStrategy {
    Bitmap generateSignalChart(Context context, People people, DataPeople dato, Sennales sennales);
}
