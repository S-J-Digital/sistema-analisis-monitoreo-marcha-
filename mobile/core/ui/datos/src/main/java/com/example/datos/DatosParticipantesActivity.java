package com.example.datos;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cache.LimpiarCache;
import com.example.database.Bd_manager_DatosParticipante;
import com.example.enviodatos.gestionStrategy.GestorEnvioDatos;
import com.example.enviodatos.impl.EnviarParticipante;
import com.example.enviodatos.retrofit.RetrofitClient;
import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;
import com.example.interfazinsertar.ParticipanteActivity;
import com.example.model.People;
import com.example.model.DataPeople;
import com.example.retroceso.Retroceso;
import com.example.sennales.ComenzarSennalesActivity;
import com.example.utillist.itemList.ListDatosAdaptador;
import com.example.utillist.R;


import java.io.Serializable;
import java.util.ArrayList;

public class DatosParticipantesActivity extends AppCompatActivity implements Serializable {
    private Object object;
    private ArrayList<DataPeople> dataArrayList = new ArrayList<>();
    private ListView listView;
    private ListDatosAdaptador listAdaptador;
    private People people;
    private Class clase;
    private InternetConnectionService internetConnectionService = new InternetConnectionService();
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.datos.R.layout.activity_datos_participantes);

        listView = (ListView) findViewById(com.example.datos.R.id.lista_datos);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            people = (People) bundle.get("persona");
            clase = (Class) bundle.get("clase");
            Retroceso.insertarClass(clase);
        }

        serviceIntent = new Intent(this, InternetConnectionService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

        atras_participante();
        actualizar_lista();
        nueva_insertar();

    }

    private void atras_participante(){
        getOnBackPressedDispatcher().addCallback(DatosParticipantesActivity.this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Maneja la acción personalizada aquí
                // Por ejemplo, muestra un mensaje de confirmación
                if (shouldAllowBackPress()) {
                    // Permitir retroceso
                    String classname = "com.example.participante.TodosLosParticipantesActivity";
                    Intent intent = new Intent(DatosParticipantesActivity.this, Retroceso.getClass(classname));
                    LimpiarCache.cleanCache(DatosParticipantesActivity.this);
                    startActivity(intent);
                    finish();
                } else {
                    // Mostrar un mensaje o hacer otra acción
                    Toast.makeText(DatosParticipantesActivity.this, "No puedes volver atrás ahora", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(com.example.datos.R.id.atras_participante);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String classname = "com.example.participante.TodosLosParticipantesActivity";
               Intent intent = new Intent(DatosParticipantesActivity.this, Retroceso.getClass(classname));
                LimpiarCache.cleanCache(DatosParticipantesActivity.this);
               startActivity(intent);
               finish();
            }
        });

    }

    private boolean shouldAllowBackPress() {
        return true;
    }

    public void actualizar_lista() {
        Bd_manager_DatosParticipante manager = new Bd_manager_DatosParticipante(this);
        ArrayList<DataPeople> datos= manager.data_list(people);
        listAdaptador = new ListDatosAdaptador(this,datos);
        listView.setAdapter(listAdaptador);

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
                actionMode.getMenuInflater().inflate(R.menu.menu_delete_update,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.eliminar_menu){
                    SparseBooleanArray select = listAdaptador.getItemselect();
                    Bd_manager_DatosParticipante manager = new Bd_manager_DatosParticipante(DatosParticipantesActivity.this);
                    for(int i= (select.size()-1); i>=0; i--){
                        if(select.valueAt(i)){
                            object = listView.getAdapter().getItem(i);
                            if (object instanceof DataPeople){
                                DataPeople dato =(DataPeople) object;
                                if(internetConnectionService.isConnected(DatosParticipantesActivity.this)){
                                    ApiService apiService = RetrofitClient.getApiService(DatosParticipantesActivity.this);
                                    EnvioStrategy strategy = new EnviarParticipante(null,apiService);
                                    GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
                                    gestorEnvioDatos.obtenerIdUpdate(people.getCi(),dato,DatosParticipantesActivity.this,"eliminardato");


                                    manager.data_delete(people.getCi(),dato.getDate());
                                    listAdaptador.eliminarItem(i);
                                    Toast.makeText(DatosParticipantesActivity.this,"la cantidad seleccionada es: "+ datos.size(), Toast.LENGTH_LONG).show();
                                }
                                showNoInternetAlertDialog("Necesita estar conectado para eliminar este dato");
                            }

                        }
                    }
                    listAdaptador.notifyDataSetChanged();
                    select.clear();
                    actionMode.finish();

                }else if(menuItem.getItemId() == R.id.modificar_menu){
                    SparseBooleanArray select = listAdaptador.getItemselect();
                    if(select.size() == 1){
                        object = listView.getAdapter().getItem(poss);
                        if (object instanceof DataPeople){
                            DataPeople datos =(DataPeople) object;
                            if(internetConnectionService.isConnected(DatosParticipantesActivity.this)){
                                Intent intent = new Intent(DatosParticipantesActivity.this, ParticipanteActivity.class);
                                Bundle enviardatos = new Bundle();
                                enviardatos.putSerializable("datos",datos);
                                enviardatos.putSerializable("persona", people);
                                enviardatos.putSerializable("clase",DatosParticipantesActivity.class);
                                intent.putExtras(enviardatos);
                                startActivity(intent);
                                LimpiarCache.cleanCache(DatosParticipantesActivity.this);
                                finish();
                            }else{
                                showNoInternetAlertDialog("Para modificar necesita estar conectado a internet");
                            }

                        }
                        listAdaptador.notifyDataSetChanged();
                        select.clear();
                        actionMode.finish();

                    }else{
                        Toast.makeText(DatosParticipantesActivity.this, "Solo se puede modificar 1 participante a la ves",
                                Toast.LENGTH_LONG).show();
                    }

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
                DataPeople dataPeople = null;
                if(object instanceof DataPeople){
                    dataPeople = (DataPeople) object;
                    Intent intent = new Intent(DatosParticipantesActivity.this, ComenzarSennalesActivity.class);
                    Bundle enviardatos = new Bundle();
                    enviardatos.putSerializable("persona", people);
                    enviardatos.putSerializable("dato", dataPeople);
                    enviardatos.putSerializable("clase",DatosParticipantesActivity.class);
                    intent.putExtras(enviardatos);
                    startActivity(intent);
                    LimpiarCache.cleanCache(DatosParticipantesActivity.this);
                    finish();
                }
            }
        });
    }


    public void nueva_insertar() {
        ImageView annadirdatos = (ImageView) findViewById(com.example.datos.R.id.imageViewAnnadirDatos);

        annadirdatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DatosParticipantesActivity.this, ParticipanteActivity.class);
                Bundle enviardatos = new Bundle();
                enviardatos.putSerializable("persona", people);
                enviardatos.putSerializable("clase",DatosParticipantesActivity.class);
                intent.putExtras(enviardatos);
                intent.putExtra("crear", "Crear_datos");
                startActivity(intent);
                LimpiarCache.cleanCache(DatosParticipantesActivity.this);
                finish();
            }
        });

    }

    private void showNoInternetAlertDialog(String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle("Sin Conexión")
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> dialog.dismiss())
                .show();
    }
}