package com.example.interfazinsertar.validacion;

public class ValidacionTelefono {
    public static boolean telefono_iscorrect(String telefono){
        boolean iscorrect = false;
        if(telefono.equals("")){
            iscorrect = true;
        }else if(telefono.length() == 8){
            iscorrect = true;
        }
        return iscorrect;
    }
}
