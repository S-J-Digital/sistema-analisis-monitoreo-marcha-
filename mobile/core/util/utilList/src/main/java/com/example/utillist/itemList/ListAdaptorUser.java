package com.example.utillist.itemList;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.User;
import com.example.model.Usuario;

import java.util.ArrayList;

public class ListAdaptorUser extends BaseAdapter {
    private Context context;
    private ArrayList<User> users;
    SparseBooleanArray itemselect;


    public ListAdaptorUser(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
        itemselect = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        User persona = (User) getItem(i);

               view = LayoutInflater.from(context).inflate(com.example.utillist.R.layout.item,null);
               TextView textViewnombre = (TextView) view.findViewById(com.example.utillist.R.id.textViewName);
               TextView textViewci = (TextView) view.findViewById(com.example.utillist.R.id.textViewCarnet);
               TextView textViewtelefono = (TextView) view.findViewById(com.example.utillist.R.id.textViewTelefonol);
               ImageView foto = (ImageView) view.findViewById(com.example.utillist.R.id.imageViewParticipante);

               CharSequence nombre = persona.getName();
               CharSequence ci = persona.getRol().toString();
               textViewnombre.setText(nombre);
               textViewci.setText(ci);
               textViewtelefono.setVisibility(View.INVISIBLE);
               foto.setVisibility(View.VISIBLE);

        return view;
    }

    //--------------------------------------------------------------------------------------
    public void eliminarItem(int position){
        users.remove(position);
    }

    public void toggleSelection(int position){

        selectView(position, !itemselect.get(position));
    }

    public void eliminarSelecion(){
        itemselect = new SparseBooleanArray();
        notifyDataSetChanged();
    }
    public void selectView(int position, boolean value) {
        if (value) {
            itemselect.put(position, value);
        } else {
            itemselect.delete(position);
        }
    }

    public SparseBooleanArray getItemselect(){
        return itemselect;
    }
}
