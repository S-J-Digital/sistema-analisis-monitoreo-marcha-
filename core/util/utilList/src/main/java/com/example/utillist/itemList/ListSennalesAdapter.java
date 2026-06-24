package com.example.utillist.itemList;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.Sennales;
import com.example.utillist.R;

import java.util.ArrayList;

public class ListSennalesAdapter extends BaseAdapter {
    private Context contex;
    private ArrayList<Sennales> sennal;
    SparseBooleanArray itemselect;
    public ListSennalesAdapter(Context context, ArrayList<Sennales> sennal) {
        this.contex = context;
        this.sennal = sennal;
        itemselect = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return sennal.size();
    }

    @Override
    public Object getItem(int i) {
        return sennal.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Sennales sennales = (Sennales) getItem(i);
        view = LayoutInflater.from(contex).inflate(com.example.utillist.R.layout.item,null);
        TextView textViewnombre = (TextView) view.findViewById(com.example.utillist.R.id.textViewName);
        TextView textViewci = (TextView) view.findViewById(com.example.utillist.R.id.textViewCarnet);
        TextView textViewtelefono = (TextView) view.findViewById(com.example.utillist.R.id.textViewTelefonol);
        ImageView foto = (ImageView) view.findViewById(com.example.utillist.R.id.imageViewParticipante);

        CharSequence nombre = sennales.getFecha();

        textViewnombre.setText(nombre);
        textViewci.setVisibility(View.INVISIBLE);
        textViewtelefono.setVisibility(View.INVISIBLE);
        foto.setImageResource(R.drawable.grafica_icono);
        foto.setVisibility(View.VISIBLE);


        return view;
    }

    public void eliminarItem(int position){
        sennal.remove(position);
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
