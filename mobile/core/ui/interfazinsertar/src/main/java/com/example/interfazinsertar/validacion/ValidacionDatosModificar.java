package com.example.interfazinsertar.validacion;

import android.content.Context;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interfazinsertar.R;

public class ValidacionDatosModificar extends AppCompatActivity {

    public boolean datos_correctos_datosparticipante(Context context,EditText editTextEdad,EditText editTextCalzado,
                                                     EditText editTextMedicion,EditText editTextPierna,EditText editTextAltura,
                                                     EditText editTextObservacion,RadioButton radioButtonsi,RadioButton radioButtonno){
        boolean datosCorrectos=false;

        if(!editTextEdad.getText().toString().equals("")){
            if(Integer.valueOf(editTextEdad.getText().toString())<115){
                if(!editTextCalzado.getText().toString().equals("")){
                    if(!editTextMedicion.getText().toString().equals("")){
                        if(!editTextPierna.getText().toString().equals("")){
                            if(!editTextAltura.getText().toString().equals("")){
                                if(radioButtonsi.isChecked()){
                                    if(!editTextObservacion.getText().toString().equals("")){
                                        datosCorrectos = true;
                                    }else{
                                        Toast.makeText(context, "Por favor ingrese la patologia " +
                                                        "que presenta",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }else if (radioButtonno.isChecked()){
                                    datosCorrectos = true;
                                }else{
                                    Toast.makeText(context, "Por favor seleccione si presenta una patologia ",
                                            Toast.LENGTH_LONG).show();
                                }

                            }else{
                                Toast.makeText(context, "Por favor ingrese la altura " +
                                                "a la que se encuentra el sensor",
                                        Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(context, "Por favor ingrese " +
                                    "el largo de la pierna", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context, "Por favor ingrese la medición" +
                                " de la cintura al tobillo", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context, "Por favor ingrese el " +
                            "número de calzado", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(context, "Por favor ingrese " +
                        "una edad correcta", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context, "Por favor ingrese " +
                    "una edad ", Toast.LENGTH_LONG).show();
        }
        return  datosCorrectos;
    }
}
