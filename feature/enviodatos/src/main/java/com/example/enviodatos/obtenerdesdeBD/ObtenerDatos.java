package com.example.enviodatos.obtenerdesdeBD;

import android.content.Context;
import android.view.contentcapture.DataRemovalRequest;

import com.example.database.Bd_manager_DatosParticipante;
import com.example.database.Bd_manager_Participante;
import com.example.database.Bd_manager_Sennales;
import com.example.database.Bd_manager_Usuario;
import com.example.database.Bd_manager_datos_patologia;
import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;
import com.example.model.User;

import java.util.ArrayList;
import java.util.List;

public class ObtenerDatos {
    static List<User> userList = new ArrayList<>();
    static List<People> peopleList = new ArrayList<>();
    static List<String> patologiaList = new ArrayList<>();
    static List<DataPeople> dataPeopleList = new ArrayList<>();

    public static  List<User> getUser(Context context){
        Bd_manager_Usuario manager_usuario = new Bd_manager_Usuario(context);

        userList = manager_usuario.user_listAll();

        return userList;
    }

    public static List<People> getPeople(Context context, String user){
        Bd_manager_Participante manager_participante = new Bd_manager_Participante(context);

        peopleList = manager_participante.person_list(user);

        return peopleList;
    }

    public static List<String> getPatologia(Context context, DataPeople dataPeople, int iddato){
        Bd_manager_datos_patologia patologias = new Bd_manager_datos_patologia(context);

        patologiaList = patologias.getListaPatologias(dataPeople.getDate(),iddato);

        return patologiaList;
    }

    public static List<DataPeople> getDataPeople(Context context, People people){
        Bd_manager_DatosParticipante manager_datosParticipante = new Bd_manager_DatosParticipante(context);

        dataPeopleList = manager_datosParticipante.data_list(people);

        return dataPeopleList;
    }

    public static int getIdDato(Context context,DataPeople dp,People people){
        Bd_manager_DatosParticipante manager_datosParticipante = new Bd_manager_DatosParticipante(context);

        return manager_datosParticipante.data_getID(people.getCi(),dp.getDate());
    }

    public static int getIdParticipante(Context context,People people){
        Bd_manager_Participante manager_Participante = new Bd_manager_Participante(context);

        return manager_Participante.person_getID(people.getCi());
    }

    public static int getIdUsuario(Context context,User user){
        Bd_manager_Usuario manager_usuario = new Bd_manager_Usuario(context);

        return manager_usuario.user_getID(user.getName());
    }

    public static List<Sennales> getSennales(Context context, DataPeople dataPeople, People people){
        Bd_manager_Sennales manager_sennales = new Bd_manager_Sennales(context);

        return manager_sennales.sennales_list(people.getCi(), dataPeople);
    }
}

