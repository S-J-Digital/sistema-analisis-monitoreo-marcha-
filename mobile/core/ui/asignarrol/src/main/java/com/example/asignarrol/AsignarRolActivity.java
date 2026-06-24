package com.example.asignarrol;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cache.LimpiarCache;
import com.example.database.Bd_manager_Usuario;
import com.example.model.User;
import com.example.model.Usuario;
import com.example.retroceso.Retroceso;
import com.example.user.GetUser;
import com.example.utillist.itemList.ListAdaptorUser;

import java.util.ArrayList;

public class AsignarRolActivity extends AppCompatActivity {
    private ListView listView;
    private ListAdaptorUser listAdaptador;
    private Object object;
    private Spinner spinnerrol;
    private Class clase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_rol);

        Bundle recibirdatos = getIntent().getExtras();
        if (recibirdatos != null) {
            clase = (Class) recibirdatos.get("clase");
            Retroceso.insertarClass(clase);
        }

        listView = (ListView) findViewById(R.id.list_user);

        atras_principal();
        actualizar_lista();
    }

    private void atras_principal(){
        getOnBackPressedDispatcher().addCallback(AsignarRolActivity.this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Maneja la acción personalizada aquí
                // Por ejemplo, muestra un mensaje de confirmación
                if (shouldAllowBackPress()) {
                    // Permitir retroceso
                    String classname = "com.example.principal.PrincipalActivity";
                    Intent intent = new Intent(AsignarRolActivity.this, Retroceso.getClass(classname));
                    startActivity(intent);
                    LimpiarCache.cleanCache(AsignarRolActivity.this);
                    finish();
                } else {
                    // Mostrar un mensaje o hacer otra acción
                    Toast.makeText(AsignarRolActivity.this, "No puedes volver atrás ahora", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.atras_principal);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String classname = "com.example.principal.PrincipalActivity";
                Intent intent = new Intent(AsignarRolActivity.this,Retroceso.getClass(classname));
                startActivity(intent);
                LimpiarCache.cleanCache(AsignarRolActivity.this);
                finish();
            }
        });
    }

    private boolean shouldAllowBackPress() {
        return true;
    }

    protected void actualizar_lista(){
        Bd_manager_Usuario manager = new Bd_manager_Usuario(this);
        int userId = manager.user_getID(GetUser.leerValor(AsignarRolActivity.this,"user"));
        ArrayList<User> persona= manager.user_list(userId);
        listAdaptador = new ListAdaptorUser(this,persona);
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
                actionMode.getMenuInflater().inflate(com.example.utillist.R.menu.menu_delete_update, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == com.example.utillist.R.id.eliminar_menu) {
                    SparseBooleanArray select = listAdaptador.getItemselect();
                    Bd_manager_Usuario manager = new Bd_manager_Usuario(AsignarRolActivity.this);
                    for (int i = (select.size() - 1); i >= 0; i--) {
                        if (select.valueAt(i)) {
                            object = listView.getAdapter().getItem(i);
                            if (object instanceof User) {
                                User people = (User) object;
                                manager.user_delete(people.getName());
                                listAdaptador.eliminarItem(i);
                                Toast.makeText(AsignarRolActivity.this, "la cantidad seleccionada es: " + persona.size(), Toast.LENGTH_LONG).show();
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
                if (object instanceof User) {
                    User people = (User) object;
                    dialogShow(people);
                }
            }
        });
    }

    public void dialogShow(User user) {
        // Crear el AlertDialog

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.rol_dialog, null);

// Encuentra el Spinner en la vista inflada
        spinnerrol = (Spinner) dialogView.findViewById(R.id.roles);

// Verifica si el Spinner no es null
        if (spinnerrol != null) {
            // Configura el adaptador
            ArrayAdapter<CharSequence> adapterfm = ArrayAdapter.createFromResource(this,
                    R.array.Roles, android.R.layout.simple_spinner_item);
            adapterfm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerrol.setAdapter(adapterfm);
        } else {
            Log.e("Error", "Spinner no inicializado correctamente en el Dialog");
        }

// Configura el diálogo con la vista inflada
        builder.setView(dialogView)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    // Acciones al confirmar
                    Bd_manager_Usuario manager_usuario = new Bd_manager_Usuario(AsignarRolActivity.this);
                    manager_usuario.user_updateRol(user.getRol(), (String) spinnerrol.getSelectedItem(),user.getName());
                    actualizar_lista();

                })
                .setNegativeButton("Cancelar", null);

// Muestra el diálogo
        AlertDialog dialog = builder.create();
        try{
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}