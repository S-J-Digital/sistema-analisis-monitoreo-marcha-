package com.example.interfazinsertar.validacion;

import android.content.Context;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interfazinsertar.R;

public class ValidacionParticipanteModificar extends AppCompatActivity {

    public static boolean datos_modificados_correctos(Context context, EditText nombre, EditText ci, EditText telefono, RadioButton femenino,
                                                      RadioButton masculino){
        boolean datosCorrectos = false;

        if(!nombre.getText().toString().equals("") ){
            if(ValidacionCI.ci_iscorrect(ci.getText().toString())){
                if(ValidacionTelefono.telefono_iscorrect(telefono.getText().toString())){
                    if(femenino.isChecked()|| masculino.isChecked()){
                        datosCorrectos = true;
                    }else{
                        Toast.makeText(context, "Por favor seleccione un sexo", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(context, "El número de telèfono es: "+ telefono.getText().toString(), Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(context,"Por favor inserte un numero de carnet valido", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(context, "Por favor inserte el nombre", Toast.LENGTH_LONG).show();
        }

        return datosCorrectos;
    }
}
