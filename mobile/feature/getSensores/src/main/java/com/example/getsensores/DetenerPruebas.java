package com.example.getsensores;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

public class DetenerPruebas {

    public static void Detenerpruebas(Context context, AppCompatButton detener, ImageButton atras){
        detener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detener.getText().toString().equals("Detener")) {
                    // Mostrar el diálogo de confirmación
                    new AlertDialog.Builder(context)
                            .setTitle("Confirmación")
                            .setMessage("¿Deseas terminar las pruebas?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Si el usuario elige "Sí", no hacer nada (no se ejecuta lo de dentro)
                                    detener.setText("Continuar");
                                    Iniciar.setComenzar(false);
                                    detener.setVisibility(View.GONE);
                                    atras.setVisibility(View.VISIBLE);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Si elige "No", ejecutar la lógica original
                                    detener.setText("Continuar");
                                    Iniciar.setComenzar(false);
                                }
                            })
                            .show();
                }else if(detener.getText().toString().equals("Continuar")){
                    detener.setText("Detener");
                    Iniciar.setComenzar(true);
                }
            }
        });
    }
}
