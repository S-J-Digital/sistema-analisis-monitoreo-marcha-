package com.example.principal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asignarrol.AsignarRolActivity;
import com.example.cache.LimpiarCache;
import com.example.config.ConfigActivity;
import com.example.config.InternetConnectionService;
import com.example.database.Bd_manager_Participante;

import com.example.database.Bd_manager_Usuario;
import com.example.datos.DatosParticipantesActivity;
import com.example.enviodatos.gestionStrategy.GestorEnvioDatos;
import com.example.enviodatos.impl.ObtenerPDF;
import com.example.enviodatos.retrofit.RetrofitClient;
import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;
import com.example.funcionalidadesbluetooth.BluetoothService;
import com.example.funcionalidadesconfig.ReporteManager;
import com.example.funcionalidadrecordatorio.Recordatorio;
import com.example.model.People;
import com.example.participante.TodosLosParticipantesActivity;
import com.example.retroceso.Retroceso;
import com.example.user.GetUser;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity implements Serializable {
    private Class clase;
    
    private LinearLayout recordatorio;
    private LinearLayout bluetooth;
    private LinearLayout usuarios;
    private LinearLayout participantes;
    private LinearLayout configuracion;
    private LinearLayout descargar;
    private static final int REQUEST_BLUETOOTH_CONNECT_Code = 1;
    private static BluetoothAdapter blue;
    private TextView textViewAddBluetooth;
    private Intent serviceIntent;
    private InternetConnectionService connectionService = new InternetConnectionService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Bundle recibirdatos = getIntent().getExtras();
        if (recibirdatos != null) {
            clase = (Class) recibirdatos.get("clase");
            Retroceso.insertarClass(clase);
        }

        //Iniciar servicio
        serviceIntent = new Intent(this, InternetConnectionService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

        recordatorio = (LinearLayout) findViewById(R.id.recordatorio);
        bluetooth = (LinearLayout) findViewById(R.id.AddBluetooth);
        textViewAddBluetooth = (TextView)findViewById(R.id.textViewAddBluetooth);
        usuarios = (LinearLayout) findViewById(R.id.Moduser);
        participantes = (LinearLayout) findViewById(R.id.AddParticipante);
        configuracion = (LinearLayout) findViewById(R.id.idconfiguracion);
        descargar = (LinearLayout) findViewById(R.id.descargarPdf);

        getRol();
        visibleporRol();
        Buscar();
        Mensaje();
        visibles();
        NavegacionhaciaParticipante();
        NavegacionhaciaUsuarios();
        Recordatorio(this);
        Bluetooth(this);
        Config();
        verTodos();
        exportarPDF();

    }

    public void NavegacionhaciaParticipante(){
        LinearLayout linearLayoutParticipante = (LinearLayout) findViewById(R.id.AddParticipante);

        linearLayoutParticipante.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, TodosLosParticipantesActivity.class);
                Bundle enviardatos = new Bundle();
                try{
                    enviardatos.putSerializable("clase",PrincipalActivity.class);
                    intent.putExtras(enviardatos);
                    startActivity(intent);
                    LimpiarCache.cleanCache(PrincipalActivity.this);
                    finish();
                }catch (Exception e){
                    Log.d("Tag", e.getMessage());
                }
            }
        });
    }

    public void NavegacionhaciaUsuarios(){
        usuarios.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, AsignarRolActivity.class);
                Bundle enviardatos = new Bundle();
                try{
                    enviardatos.putSerializable("clase",PrincipalActivity.class);
                    intent.putExtras(enviardatos);
                    startActivity(intent);
                    LimpiarCache.cleanCache(PrincipalActivity.this);
                    finish();
                }catch (Exception e){
                    Log.d("Tag", e.getMessage());
                }
            }
        });
    }

    public void Mensaje(){
        TextView mensaje = (TextView) findViewById(R.id.textViewPrincipalUsuario);
        CharSequence frase = mensaje.getText().toString() +" "+ GetUser.leerValor(this,"user");
        mensaje.setText(frase);
    }

    public void visibles(){
        LinearLayout linearLayoutparticipante1 = (LinearLayout) findViewById(R.id.ParticipanteName);
        TextView textViewParticipanteName = (TextView) findViewById(R.id.textViewParticipanteName);
        TextView textViewCarnetPrincipal = (TextView) findViewById(R.id.textViewCarnetPrincipal);
        TextView textViewTelefonoPrincipal = (TextView) findViewById(R.id.textViewTelefonol);

        LinearLayout linearLayoutparticipante2 = (LinearLayout) findViewById(R.id.ParticipanteName2);
        TextView textViewname2 = (TextView) findViewById(R.id.textViewParticipanteName2);
        TextView textViewCarnetPrincipal2= (TextView) findViewById(R.id.textViewCarnetPrincipal2);
        TextView textViewTelefonoPrincipal2 = (TextView) findViewById(R.id.textViewTelefonoPrincipal2);
        TextView textViewAll = (TextView) findViewById(R.id.textViewAll);
        TextView textViewparticipante = (TextView) findViewById(R.id.textView9);

        Bd_manager_Participante manager = new Bd_manager_Participante(this);
        String user = GetUser.leerValor(PrincipalActivity.this,"user");
        ArrayList<People> nameList= manager.person_list(user);

        if(!nameList.isEmpty()){
            if(nameList.size()==1){
                textViewparticipante.setVisibility(View.VISIBLE);
                linearLayoutparticipante1.setVisibility(View.VISIBLE);
                CharSequence name = nameList.get(0).getName();
                CharSequence ci = nameList.get(0).getCi();
                CharSequence telef = nameList.get(0).getTelefono();
                textViewParticipanteName.setText(name);
                textViewCarnetPrincipal.setText(ci);
                textViewTelefonoPrincipal.setText(telef);
            }else if(nameList.size()==2){
                textViewparticipante.setVisibility(View.VISIBLE);

                CharSequence name = nameList.get(0).getName();
                CharSequence ci = nameList.get(0).getCi();
                CharSequence telef = nameList.get(0).getTelefono();
                linearLayoutparticipante1.setVisibility(View.VISIBLE);
                textViewParticipanteName.setText(name);
                textViewCarnetPrincipal.setText(ci);
                textViewTelefonoPrincipal.setText(telef);

                CharSequence name2 = nameList.get(1).getName();
                CharSequence ci2 = nameList.get(1).getCi();
                CharSequence telef2 = nameList.get(1).getTelefono();
                linearLayoutparticipante2.setVisibility(View.VISIBLE);
                textViewname2.setText(name2);
                textViewCarnetPrincipal2.setText(ci2);
                textViewTelefonoPrincipal2.setText(telef2);
            }else {
                textViewAll.setVisibility(View.VISIBLE);
                textViewparticipante.setVisibility(View.VISIBLE);

                linearLayoutparticipante1.setVisibility(View.VISIBLE);
                CharSequence name = nameList.get(0).getName();
                CharSequence ci = nameList.get(0).getCi();
                CharSequence telef = nameList.get(0).getTelefono();
                textViewParticipanteName.setText(name);
                textViewCarnetPrincipal.setText(ci);
                textViewTelefonoPrincipal.setText(telef);

                linearLayoutparticipante2.setVisibility(View.VISIBLE);
                CharSequence name2 = nameList.get(1).getName();
                CharSequence ci2 = nameList.get(1).getCi();
                CharSequence telef2 = nameList.get(1).getTelefono();
                textViewname2.setText(name2);
                textViewCarnetPrincipal2.setText(ci2);
                textViewTelefonoPrincipal2.setText(telef2);

            }
        }
    }

   protected void Recordatorio(Context context){
        Recordatorio calendar = new Recordatorio();
       recordatorio.setOnClickListener(v -> Recordatorio.showDatePickerDialog(context));
   }

   protected void Bluetooth(Context context){

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           permisos();
       }
       bluetooth.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               BluetoothService bluetoothService = new BluetoothService();
               bluetoothService.bluetoothEncendido(context);
               textViewAddBluetooth.setText("Conectado");
           }
       });

   }

   protected void Config(){
       LinearLayout linearLayoutConfig = (LinearLayout) findViewById(R.id.idconfiguracion);

       linearLayoutConfig.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View view) {
               Intent intent = new Intent(PrincipalActivity.this, ConfigActivity.class);
               Bundle enviardatos = new Bundle();
               try{
                   enviardatos.putSerializable("clase",PrincipalActivity.class);
                   intent.putExtras(enviardatos);
                   startActivity(intent);
                   LimpiarCache.cleanCache(PrincipalActivity.this);
                   finish();
               }catch (Exception e){
                   Log.d("Tag", e.getMessage());
               }
           }
       });
   }

   public void verTodos(){
        TextView textView = (TextView) findViewById(R.id.textViewAll);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, TodosLosParticipantesActivity.class);
                Bundle enviardatos = new Bundle();
                try{
                    enviardatos.putSerializable("clase",PrincipalActivity.class);
                    intent.putExtras(enviardatos);
                    startActivity(intent);
                    LimpiarCache.cleanCache(PrincipalActivity.this);
                    finish();
                }catch (Exception e){
                    Log.d("Tag", e.getMessage());
                }
            }
        });
   }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_BLUETOOTH_CONNECT_Code:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Gracias por conceder los permisos", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)//android 8 o superior
    public void permisos(){
        if (ActivityCompat.checkSelfPermission(PrincipalActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PrincipalActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT_Code);
         return;
        }
    }

    //Prueba Buscar
    public void Buscar(){
        SearchView searchView = (SearchView) findViewById(R.id.buscar);
        ListView suggestionsListView = (ListView) findViewById(R.id.ListView);
        String name = GetUser.leerValor(PrincipalActivity.this, "user");
        ArrayAdapter<String> adapter;
        Bd_manager_Participante manager_participante = new Bd_manager_Participante(PrincipalActivity.this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        suggestionsListView.setAdapter(adapter);
        try{
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // Puedes manejar la acción de búsqueda si es necesario
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Filtrar la lista según el texto ingresado

                    List<String> filteredList = manager_participante.getMatchingParticipants(newText, name);
                    if (!filteredList.isEmpty()) {
                        adapter.clear();
                        adapter.addAll(filteredList);
                        adapter.notifyDataSetChanged();
                        suggestionsListView.setVisibility(View.VISIBLE);
                        suggestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Object object = suggestionsListView.getAdapter().getItem(i);
                                People people = null;
                                if (object instanceof String){
                                    String nombre = (String) object;
                                    people =manager_participante.getperson_byname(nombre,GetUser.leerValor(PrincipalActivity.this,"user"));
                                    Intent intent = new Intent(PrincipalActivity.this, DatosParticipantesActivity.class);
                                    Bundle enviardatos = new Bundle();
                                    enviardatos.putSerializable("persona", people);
                                    enviardatos.putSerializable("clase",PrincipalActivity.class);
                                    intent.putExtras(enviardatos);
                                    LimpiarCache.cleanCache(PrincipalActivity.this);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                    } else {
                        suggestionsListView.setVisibility(View.GONE);
                    }
                    return true;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                suggestionsListView.setVisibility(View.GONE);
                return true;
            }
        });
    }

    public void visibleporRol(){
        String rol = GetUser.leerValor(PrincipalActivity.this,"rol");
        switch (rol){
            case "Administrador":
                bluetooth.setVisibility(View.VISIBLE);
                usuarios.setVisibility(View.VISIBLE);
                recordatorio.setVisibility(View.VISIBLE);
                participantes.setVisibility(View.VISIBLE);
                configuracion.setVisibility(View.VISIBLE);
                descargar.setVisibility(View.VISIBLE);
                break;
            case "Médico":
                bluetooth.setVisibility(View.VISIBLE);
                usuarios.setVisibility(View.GONE);
                recordatorio.setVisibility(View.VISIBLE);
                participantes.setVisibility(View.VISIBLE);
                configuracion.setVisibility(View.VISIBLE);
                descargar.setVisibility(View.GONE);
                break;
            case "Paciente":
                bluetooth.setVisibility(View.GONE);
                usuarios.setVisibility(View.GONE);
                recordatorio.setVisibility(View.VISIBLE);
                participantes.setVisibility(View.VISIBLE);
                configuracion.setVisibility(View.VISIBLE);
                descargar.setVisibility(View.GONE);
                break;
            case "Investigador":
                descargar.setVisibility(View.VISIBLE);
                bluetooth.setVisibility(View.GONE);
                usuarios.setVisibility(View.GONE);
                recordatorio.setVisibility(View.GONE);
                participantes.setVisibility(View.GONE);
                configuracion.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }

    public void getRol(){
        Bd_manager_Usuario manager_usuario = new Bd_manager_Usuario(PrincipalActivity.this);
        String user= GetUser.leerValor(PrincipalActivity.this,"user");
        String rol = manager_usuario.getUser(user).getRol();
        GetUser.guardarValor(PrincipalActivity.this,"rol",rol);
    }

    public void exportarPDF(){
        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionService.isConnected(PrincipalActivity.this)){
                    ApiService apiService = RetrofitClient.getApiService(PrincipalActivity.this);
                    EnvioStrategy envioStrategy = new ObtenerPDF(apiService, GetUser.leerValor(PrincipalActivity.this,"user"));
                    GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(envioStrategy);
                    gestorEnvioDatos.enviar(PrincipalActivity.this);
                }else{
                    ReporteManager reporteManager = new ReporteManager(PrincipalActivity.this);
                    reporteManager.generarReporte(PrincipalActivity.this);
                }
            }
        });
    }
}