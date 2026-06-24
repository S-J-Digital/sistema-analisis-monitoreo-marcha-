package com.example.sennales;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cache.LimpiarCache;
import com.example.database.Bd_manager_Sennales;
import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;
import com.example.retroceso.Retroceso;
import com.example.utillist.itemList.ListSennalesAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class Visualizar_TablaActivity extends AppCompatActivity implements Serializable {
    private ListView listView;
    private ListSennalesAdapter listAdaptador;
    private People people;
    private DataPeople dato;
    private Class clase;
    private Object object;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_tabla);

        Bundle recibirdatos = getIntent().getExtras();
        if (recibirdatos != null) {
            people = (People) recibirdatos.get("persona");
            dato = (DataPeople) recibirdatos.get("datos");
            clase = (Class) recibirdatos.get("clase");
            Retroceso.insertarClass(clase);
        }

        listView = (ListView) findViewById(R.id.lista_sennales);
        actualizar_list();
        atras();
    }

    public void actualizar_list(){
        Bd_manager_Sennales manager = new Bd_manager_Sennales(this);
        ArrayList<Sennales> sennal= manager.sennales_list(people.getCi(), dato);
        ArrayList<Sennales> sennalFecha = obtenerporfecha(sennal);
        listAdaptador = new ListSennalesAdapter(Visualizar_TablaActivity.this, sennalFecha);
        try{
            listView.setAdapter(listAdaptador);
        }catch (Exception e){

        }

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int poss;
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                poss = i;
                listAdaptador.toggleSelection(i);
                actionMode.setTitle(String.valueOf(listView.getCheckedItemCount()));
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(com.example.utillist.R.menu.menu_delete_update,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if(menuItem.getItemId() == com.example.utillist.R.id.eliminar_menu){
                    SparseBooleanArray select = listAdaptador.getItemselect();
                    Bd_manager_Sennales manager = new Bd_manager_Sennales(Visualizar_TablaActivity.this);
                    for(int i= (select.size()-1); i>=0; i--){
                        if(select.valueAt(i)){
                            object = listView.getAdapter().getItem(i);
                            if (object instanceof Sennales){
                                Sennales sennals =(Sennales) object;
                                manager.sennal_deleteAll_by_Date(dato);
                                listAdaptador.eliminarItem(i);
                                Toast.makeText(Visualizar_TablaActivity.this,"la cantidad seleccionada es: "+ sennal.size(), Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                    listAdaptador.notifyDataSetChanged();
                    select.clear();
                    actionMode.finish();

                }

                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                listAdaptador.eliminarSelecion();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                object = listView.getAdapter().getItem(i);
                Sennales sennal = null;
                if (object instanceof Sennales){
                    sennal =(Sennales) object;
                    Intent intent = new Intent(Visualizar_TablaActivity.this, GraficaSennalesActivity.class);
                    Bundle enviardatos = new Bundle();
                    enviardatos.putSerializable("persona", people);
                    enviardatos.putSerializable("datos",dato);
                    enviardatos.putSerializable("sennal",sennal);
                    enviardatos.putSerializable("clase",Visualizar_TablaActivity.class);
                    intent.putExtras(enviardatos);
                    startActivity(intent);
                    LimpiarCache.cleanCache(Visualizar_TablaActivity.this);
                    finish();
                }
            }
        });
    }

    public ArrayList<Sennales> obtenerporfecha(ArrayList<Sennales> sennal){
        ArrayList<Sennales> sennalFecha = new ArrayList<>();

        if(!sennal.isEmpty()){
            for(int i =0; i<sennal.size();i++){
                if(!existDate(sennalFecha,sennal.get(i))){
                    sennalFecha.add(sennal.get(i));
                }
            }
        }
        return sennalFecha;
    }

    public boolean existDate(ArrayList<Sennales> sennalFecha, Sennales sennal){
        boolean exist = false;
        if(!sennalFecha.isEmpty()){
            for (int i =0; i<sennalFecha.size() && !exist ;i++){
                if(sennalFecha.get(i).getFecha().equals(sennal.getFecha())){
                    exist=true;
                }
            }
        }
        return exist;
    }

    public void atras(){

        getOnBackPressedDispatcher().addCallback(Visualizar_TablaActivity.this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Maneja la acción personalizada aquí
                // Por ejemplo, muestra un mensaje de confirmación
                if (shouldAllowBackPress()) {
                    // Permitir retroceso
                    String classname = "com.example.sennales.ComenzarSennalesActivity";
                    Intent intent = new Intent(Visualizar_TablaActivity.this, Retroceso.getClass(classname));
                    Bundle enviardatos = new Bundle();
                    enviardatos.putSerializable("persona", people);
                    enviardatos.putSerializable("dato",dato);
                    enviardatos.putSerializable("clase",Visualizar_TablaActivity.class);
                    intent.putExtras(enviardatos);
                    startActivity(intent);
                    LimpiarCache.cleanCache(Visualizar_TablaActivity.this);
                    finish();
                    finish();  // O usa super.onBackPressed() si es necesario
                } else {
                    // Mostrar un mensaje o hacer otra acción
                    Toast.makeText(Visualizar_TablaActivity.this, "No puedes volver atrás ahora", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton atras = (ImageButton) findViewById(R.id.atras_comenzar);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String classname = "com.example.sennales.ComenzarSennalesActivity";
                Intent intent = new Intent(Visualizar_TablaActivity.this, Retroceso.getClass(classname));
                Bundle enviardatos = new Bundle();
                enviardatos.putSerializable("persona", people);
                enviardatos.putSerializable("dato",dato);
                enviardatos.putSerializable("clase",Visualizar_TablaActivity.class);
                intent.putExtras(enviardatos);
                startActivity(intent);
                LimpiarCache.cleanCache(Visualizar_TablaActivity.this);
                finish();
            }
        });

    }

    // Método que decide si permitir o no el retroceso
    private boolean shouldAllowBackPress() {
        // Lógica para decidir si se permite la navegación hacia atrás
        return true;
    }
}