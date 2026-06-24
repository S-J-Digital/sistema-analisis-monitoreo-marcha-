package com.example.interfazinsertar;

import android.content.Context;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.database.Bd_manager_DatosParticipante;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertarDatos {

    public static void Guardar_datos_participante(Context context,EditText editTextEdad, EditText editTextCalzado, EditText editTextMedicion,
                                                  EditText editTextPierna, EditText editTextAltura,EditText editTextObservacion,
                                                  RadioButton radioButtonsi, RadioButton radioButtonno, EditText ci) {

        if (radioButtonsi.isChecked()) {
            Bd_manager_DatosParticipante manager = new Bd_manager_DatosParticipante(context);
            manager.insert_en_tablas(ci.getText().toString(), Integer.valueOf(editTextEdad.getText().toString()),
                    1, Double.valueOf(editTextCalzado.getText().toString()),
                    Double.valueOf(editTextMedicion.getText().toString()), Double.valueOf(editTextPierna.getText().toString()),
                    Double.valueOf(editTextAltura.getText().toString()),getEnfermedades(editTextObservacion.getText().toString()) );
        } else {
            Bd_manager_DatosParticipante manager = new Bd_manager_DatosParticipante(context);
            manager.insert_en_tablas(ci.getText().toString(), Integer.valueOf(editTextEdad.getText().toString()),
                    0, Double.valueOf(editTextCalzado.getText().toString()),
                    Double.valueOf(editTextMedicion.getText().toString()), Double.valueOf(editTextPierna.getText().toString()),
                    Double.valueOf(editTextAltura.getText().toString()), getEnfermedades(null));
        }
    }

    public static List<String> getEnfermedades(String enfermedades){
        List<String> enfermedadesList=new ArrayList<>();
        if(enfermedades != null){
            enfermedadesList = Arrays.asList(enfermedades.split("\\s*,\\s*"));
        }
        return enfermedadesList;
    }
}
