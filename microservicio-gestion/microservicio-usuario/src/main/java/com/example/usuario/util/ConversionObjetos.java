package com.example.usuario.util;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ConversionObjetos{

    private ConversionObjetos() {}

    public static <T> T convertirHashMapaObject(HashMap<String,Object> lista, Class<T> clase) throws Exception {
        T objeto = clase.getDeclaredConstructor().newInstance();
        for (Field f : clase.getDeclaredFields()){
            if(lista.containsKey(f.getName())){
                f.setAccessible(true);
                if ((f.getType().equals(long.class) || f.getType().equals(Long.class)) && lista.get(f.getName()) instanceof Integer) {
                    f.set(objeto,Long.valueOf((String) lista.get(f.getName())));
                }else if ((f.getType().equals(double.class) || f.getType().equals(Double.class)) && lista.get(f.getName()) instanceof Float) {
                    f.set(objeto, Double.valueOf((String) lista.get(f.getName())));
                }else {
                    f.set(objeto, lista.get(f.getName()));
                }
            }
        }
        return objeto;
    }
}
