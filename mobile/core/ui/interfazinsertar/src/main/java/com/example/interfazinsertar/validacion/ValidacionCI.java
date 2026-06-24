package com.example.interfazinsertar.validacion;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ValidacionCI {

    public static boolean ci_iscorrect(String carnet){
        boolean iscorrect = true;
        // Extraer la fecha de nacimiento y el siglo
        String fechaNacimiento = carnet.substring(0, 6);
        char siglo = carnet.charAt(6);

        // Verificar la longitud del carnet
        if (carnet == null || carnet.length() != 11) {
            iscorrect = false;
        }else{

            // Verificar la fecha de nacimiento
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
            sdf.setLenient(false);
            try {
                sdf.parse(fechaNacimiento);
            } catch (ParseException e) {
                iscorrect = false;
            }

            // Verificar el siglo
            if (siglo < '0' || siglo > '8') {
                iscorrect = false;
            }

            // Verificar el sexo
            int sexo = Character.getNumericValue(carnet.charAt(9));
            if (sexo < 0 || sexo > 9) {
                iscorrect = false;
            }

        }

        // Si todas las verificaciones son correctas
        return iscorrect;
    }
}
