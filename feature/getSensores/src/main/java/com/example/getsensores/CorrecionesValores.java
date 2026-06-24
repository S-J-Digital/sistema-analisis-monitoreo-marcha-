package com.example.getsensores;

public class CorrecionesValores {

    public static double AcelerometroEscala(int acel_config){
        return (acel_config * 9.807)/32767.5;
    }

    public static double GiroscopioEscala(int giros_config){
        return (giros_config * Math.PI)/(180 * 32767.5);
    }

    public static double MagnetometroEscala(int mag_config) {
        // Ajuste para asegurar que el valor no sea 0 para la configuración 128
        return (((mag_config - 128) / 256 )+ 1) * (4912.0 / 32760.0);
    }

}
