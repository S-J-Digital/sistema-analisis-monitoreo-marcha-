package com.example.utillist.itemList;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.model.People;

import java.util.ArrayList;

public class ListAdaptador extends BaseAdapter {
    private Context contex;
    private ArrayList<People> people;
    SparseBooleanArray itemselect;

    public ListAdaptador(Context context, ArrayList<People> persona) {
        this.contex = context;
        this.people = persona;
        itemselect = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int i) {
        return people.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        People persona = (People) getItem(i);
        view = LayoutInflater.from(contex).inflate(com.example.utillist.R.layout.item,null);
        TextView textViewnombre = (TextView) view.findViewById(com.example.utillist.R.id.textViewName);
        TextView textViewci = (TextView) view.findViewById(com.example.utillist.R.id.textViewCarnet);
        TextView textViewtelefono = (TextView) view.findViewById(com.example.utillist.R.id.textViewTelefonol);
        ImageView foto = (ImageView) view.findViewById(com.example.utillist.R.id.imageViewParticipante);

        CharSequence nombre = persona.getName();
        CharSequence ci = persona.getCi();
        CharSequence telefono = persona.getTelefono();
        textViewnombre.setText(nombre);
        textViewci.setText(ci);
        textViewtelefono.setText(telefono);
        foto.setVisibility(View.VISIBLE);

        return view;
    }

    //--------------------------------------------------------------------------------------
    public void eliminarItem(int position){
        people.remove(position);
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
//-------------------------------------------------------------------------------------


}
