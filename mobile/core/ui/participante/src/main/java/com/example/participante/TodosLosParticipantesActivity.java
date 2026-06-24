package com.example.participante;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.example.database.Bd_manager_Participante;
import com.example.datos.DatosParticipantesActivity;
import com.example.enviodatos.gestionStrategy.GestorEnvioDatos;
import com.example.enviodatos.impl.EnviarParticipante;
import com.example.enviodatos.retrofit.RetrofitClient;
import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;
import com.example.interfazinsertar.ParticipanteActivity;
import com.example.model.People;

import com.example.retroceso.Retroceso;
import com.example.user.GetUser;
import com.example.utillist.itemList.ListAdaptador;

import java.io.Serializable;
import java.util.ArrayList;

public class TodosLosParticipantesActivity extends AppCompatActivity  implements Serializable {

    private Object object;
    private ArrayList<People> peopleArrayList = new ArrayList<>();
    private ListView listView;
    private ListAdaptador listAdaptador;
    private Class clase;
    private InternetConnectionService internetConnectionService = new InternetConnectionService();
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos_los_participantes);

        Bundle recibirdatos = getIntent().getExtras();
        if (recibirdatos != null) {
            clase = (Class) recibirdatos.get("clase");
            Retroceso.insertarClass(clase);
        }

        serviceIntent = new Intent(this, InternetConnectionService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

        listView = (ListView) findViewById(R.id.lista_participantes);
        actualizar_lista();
        nueva_insertar();
        atras_principal();

    }


    protected void actualizar_lista(){
        String user = GetUser.leerValor(TodosLosParticipantesActivity.this,"user");
        Bd_manager_Participante manager = new Bd_manager_Participante(this);
        ArrayList<People> persona= manager.person_list(user);
        listAdaptador = new ListAdaptador(this,persona);
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
                    Bd_manager_Participante manager = new Bd_manager_Participante(TodosLosParticipantesActivity.this);
                    for(int i= (select.size()-1); i>=0; i--){
                        if(select.valueAt(i)){
                            object = listView.getAdapter().getItem(i);
                            if (object instanceof People){
                                People people =(People) object;
                               if(internetConnectionService.isConnected(TodosLosParticipantesActivity.this)){

                                   ApiService apiService = RetrofitClient.getApiService(TodosLosParticipantesActivity.this);
                                   EnvioStrategy strategy = new EnviarParticipante(null,apiService);
                                   GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
                                   gestorEnvioDatos.obtenerIdUpdate(people.getCi(),null,TodosLosParticipantesActivity.this,"eliminar");

                                   manager.person_delete(people.getCi());
                                   listAdaptador.eliminarItem(i);
                                   Toast.makeText(TodosLosParticipantesActivity.this,"la cantidad seleccionada es: "+ persona.size(), Toast.LENGTH_LONG).show();
                               }else{
                                   showNoInternetAlertDialog("Necesita estar conectado para eliminar este participante");
                               }
                            }

                        }
                    }
                    listAdaptador.notifyDataSetChanged();
                    select.clear();
                    actionMode.finish();

                }else if(menuItem.getItemId() == com.example.utillist.R.id.modificar_menu){
                    SparseBooleanArray select = listAdaptador.getItemselect();
                    if(select.size() == 1){
                        object = listView.getAdapter().getItem(poss);
                        if (object instanceof People){
                            People people =(People) object;
                               if(internetConnectionService.isConnected(TodosLosParticipantesActivity.this)){
                                   Intent intent = new Intent(TodosLosParticipantesActivity.this, ParticipanteActivity.class);
                                   Bundle enviardatos = new Bundle();
                                   try{
                                       enviardatos.putSerializable("persona", people);
                                       enviardatos.putSerializable("clase",TodosLosParticipantesActivity.class);
                                       intent.putExtras(enviardatos);
                                       startActivity(intent);
                                       LimpiarCache.cleanCache(TodosLosParticipantesActivity.this);
                                       finish();
                                   }catch (Exception e){
                                       Log.d("Tag", e.getMessage());
                                   }
                               }else{
                                   showNoInternetAlertDialog("Para modificar necesita estar conectado a internet");
                               }

                        }

                        listAdaptador.notifyDataSetChanged();
                        select.clear();
                        actionMode.finish();

                    }else{
                        Toast.makeText(TodosLosParticipantesActivity.this, "Solo se puede modificar 1 participante a la ves",
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
                People people = null;
                if (object instanceof People){
                    people =(People) object;
                    Intent intent = new Intent(TodosLosParticipantesActivity.this, DatosParticipantesActivity.class);
                    Bundle enviardatos = new Bundle();
                    enviardatos.putSerializable("persona", people);
                    enviardatos.putSerializable("clase",TodosLosParticipantesActivity.class);
                    intent.putExtras(enviardatos);
                    LimpiarCache.cleanCache(TodosLosParticipantesActivity.this);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    protected void nueva_insertar(){
        ImageView insert= (ImageView) findViewById(R.id.imageViewAnnadir);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle enviardatos = new Bundle();
                enviardatos.putSerializable("clase", TodosLosParticipantesActivity.class);
                Intent intent = new Intent(TodosLosParticipantesActivity.this, ParticipanteActivity.class);
                intent.putExtras(enviardatos);
                startActivity(intent);
                LimpiarCache.cleanCache(TodosLosParticipantesActivity.this);
                finish();
            }
        });
    }

    private void atras_principal(){
        getOnBackPressedDispatcher().addCallback(TodosLosParticipantesActivity.this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Maneja la acción personalizada aquí
                // Por ejemplo, muestra un mensaje de confirmación
                if (shouldAllowBackPress()) {
                    // Permitir retroceso
                    String classname = "com.example.principal.PrincipalActivity";
                    Intent intent = new Intent(TodosLosParticipantesActivity.this,Retroceso.getClass(classname));
                    startActivity(intent);
                    LimpiarCache.cleanCache(TodosLosParticipantesActivity.this);
                    finish();
                } else {
                    // Mostrar un mensaje o hacer otra acción
                    Toast.makeText(TodosLosParticipantesActivity.this, "No puedes volver atrás ahora", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.atras_principal);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String classname = "com.example.principal.PrincipalActivity";
                Intent intent = new Intent(TodosLosParticipantesActivity.this,Retroceso.getClass(classname));
                startActivity(intent);
                LimpiarCache.cleanCache(TodosLosParticipantesActivity.this);
                finish();
            }
        });
    }

    private boolean shouldAllowBackPress() {
        return true;
    }

    private void showNoInternetAlertDialog(String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle("Sin Conexión")
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> dialog.dismiss())
                .show();
    }

}