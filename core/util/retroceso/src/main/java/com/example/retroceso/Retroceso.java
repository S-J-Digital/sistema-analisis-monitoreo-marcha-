package com.example.retroceso;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Retroceso {

    private static List<Class> retroceso = new ArrayList<>();

    public static List<Class> getRetorceso() {
        return retroceso;
    }

    public static void insertarClass(Class clase){
      if(!retroceso.contains(clase)){
          retroceso.add(clase);
      }
    }

    public static Class getClass(String classname){
        Class clase = null;
        boolean find = false;
        for (int i =0; i< retroceso.size() && !find;i++){
            String clases = retroceso.get(i).getName();
            try{
                if(retroceso.get(i).getName().equals(classname)){
                    clase = retroceso.get(i);
                    find = true;
                }
            }catch (Exception e){
                Log.d("Tag", e.getMessage());
            }

        }
        return clase;
    }

}
