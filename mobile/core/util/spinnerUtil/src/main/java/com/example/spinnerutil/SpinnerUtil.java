package com.example.spinnerutil;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerUtil {

    // Método para configurar el Spinner con un recurso de array
    public static void setupSpinneracelerometro(Spinner spinner, Context context, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                arrayResourceId, android.R.layout.simple_spinner_item);

        // Especifica el diseño para las opciones desplegables
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Asigna el adaptador al Spinner
        spinner.setAdapter(adapter);
    }
    public static void setupSpinnerfrecuencia(Spinner spinner, Context context, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                arrayResourceId, android.R.layout.simple_spinner_item);

        // Especifica el diseño para las opciones desplegables
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Asigna el adaptador al Spinner
        spinner.setAdapter(adapter);
    }
    public static void setupSpinnergiroscopio(Spinner spinner, Context context, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                arrayResourceId, android.R.layout.simple_spinner_item);

        // Especifica el diseño para las opciones desplegables
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Asigna el adaptador al Spinner
        spinner.setAdapter(adapter);
    }

}
