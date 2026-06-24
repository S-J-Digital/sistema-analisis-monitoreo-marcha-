package com.example.interfazinsertar.validacion;

import android.content.Context;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.database.Bd_manager_Participante;
import com.example.model.People;

import androidx.appcompat.app.AppCompatActivity;

public class ValidacionTodosLosDatos  extends AppCompatActivity {


    public static boolean datosCorrectos(Context context, People people, EditText nombre, EditText ci, EditText telefono ,
                                         RadioButton femenino, RadioButton masculino,
                                         EditText editTextEdad, EditText editTextCalzado, EditText editTextMedicion,
                                         EditText editTextPierna, EditText editTextAltura, EditText editTextObservacion,
                                         RadioButton radioButtonsi, RadioButton radioButtonno) {

        boolean datosCorrectos = false;
        //-------------------------------------------------------------------

        if(!nombre.getText().toString().equals("") ){
            if(ValidacionCI.ci_iscorrect(ci.getText().toString())){
                if(id_baseDatos(context, people,ci)==-1){
                    if(ValidacionTelefono.telefono_iscorrect(telefono.getText().toString())){
                        if(femenino.isChecked()|| masculino.isChecked()){
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
                        }else{
                            Toast.makeText(context, "Por favor seleccione un sexo", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(context, "El número de telèfono es: "+ telefono.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context, "El participante ya ha sido registrado antes", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(context,"Por favor inserte un numero de carnet valido", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(context, "Por favor inserte el nombre", Toast.LENGTH_LONG).show();
        }

        return datosCorrectos;
    }

    public static int id_baseDatos(Context context, People people, EditText ci){
        int id=0;
        if(people == null){
            Bd_manager_Participante manager = new Bd_manager_Participante(context);
            id = manager.person_getID(ci.getText().toString());
        }
        return id;
    }
}
