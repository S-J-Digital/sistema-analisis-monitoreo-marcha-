package com.example.getsensores;

import android.widget.Spinner;

public class CalcularTiempo {
    private CalcularTiempo() {
        throw new IllegalStateException("Utility class");
    }
//divisor de la frecuencia de muestreo es un valor que se utiliza
// para ajustar la frecuencia de muestreo en sistemas de adquisición
// de datos o dispositivos de sensores.

    public static int calcularFrecuenciaDeMuestreo(Spinner frecuencia) {
        String config= (String)frecuencia.getSelectedItem();
        int srd = 0;

        switch (config){
            case "0":
                srd =0;
                break;
            case "1":
                srd =1;
                break;
            case "2":
                srd =2;
                break;
            case "3":
                srd =3;
                break;
            case "4":
                srd =4;
                break;
            default:
                break;
        }

        // Retornar período en milisegundos como un valor de tipo double
        return  1000 / (1 + srd);
    }

}
