package com.example.utillist.itemList;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.DataPeople;
import com.example.utillist.R;

import java.util.ArrayList;

public class ListDatosAdaptador extends BaseAdapter {
    private Context contex;
    private ArrayList<DataPeople> datos;
    SparseBooleanArray itemselect;


    public ListDatosAdaptador(Context context, ArrayList<DataPeople> datos) {
        this.contex = context;
        this.datos = datos;
        itemselect = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int i) {
        return datos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DataPeople datos = (DataPeople) getItem(i);
        view = LayoutInflater.from(contex).inflate(com.example.utillist.R.layout.item,null);
        TextView textViewnombre = (TextView) view.findViewById(com.example.utillist.R.id.textViewName);
        TextView textViewci = (TextView) view.findViewById(com.example.utillist.R.id.textViewCarnet);
        TextView textViewtelefono = (TextView) view.findViewById(com.example.utillist.R.id.textViewTelefonol);
        ImageView foto = (ImageView) view.findViewById(com.example.utillist.R.id.imageViewParticipante);
        CharSequence patologia;
        CharSequence telefono;

        CharSequence nombre = datos.getDate();
        CharSequence ci = datos.getHour();
        if(datos.getObservation().size() != 0){
            patologia = datos.getObservation().get(0);
            telefono = "Patologia : " + patologia;
        }else {
            telefono = "Patologia : ";
        }
        textViewnombre.setText(nombre);
        textViewci.setText(ci);
        textViewtelefono.setText(telefono);
        foto.setImageResource(R.drawable.datos_icono);
        foto.setVisibility(View.VISIBLE);


        return view;
    }

    //--------------------------------------------------------------------------------------
    public void eliminarItem(int position){
        datos.remove(position);
    }

    public void toggleSelection(int position){

        selectView(position, !itemselect.get(position));
    }

    public void eliminarSelecion(){
        itemselect = new SparseBooleanArray();
        notifyDataSetChanged();
    }
    public void selectView(int position, boolean value){
        if (value){
            itemselect.put(position, value);
        }else{
            itemselect.delete(position);
        }
    }

    public SparseBooleanArray getItemselect(){
        return itemselect;
    }
}
