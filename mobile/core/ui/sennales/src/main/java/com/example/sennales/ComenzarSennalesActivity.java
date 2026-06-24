package com.example.sennales;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.cache.LimpiarCache;
import com.example.getsensores.CalcularTiempo;
import com.example.getsensores.CorrecionesValores;
import com.example.getsensores.SeleccionSensores;
import com.example.model.DataPeople;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.model.People;
import com.example.retroceso.Retroceso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ComenzarSennalesActivity extends AppCompatActivity implements Serializable {
    private Class<?> clase;
    private ImageButton atras;
    private People people;
    private DataPeople dato;
    int config_frec;
    double config_acel ;
    double config_giros;
    double config_magne;
    private AppCompatButton comenzar, visualizar;
    private Spinner spinnerfrec, spinneracel, spinnergiros, spinnertime, spinnerdisp;
    private int select_frecuencia, select_acel, select_giros, select_time;
    private String select_dispositivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comenzar_sennales);

        procesarBundle(getIntent().getExtras());
        iniciarComponentes();
        configurarListeners();
        bloquearPorFecha(dato);
        Onback();
    }

    private void procesarBundle(Bundle recibirdatos) {
        if(recibirdatos == null) {
            return;
        }

        people = (People) recibirdatos.get("persona");
        dato = (DataPeople) recibirdatos.get("dato");
        clase = (Class<?>) recibirdatos.get("clase");
        Retroceso.insertarClass(clase);
    }

    private void iniciarComponentes() {
        comenzar = findViewById(R.id.buttonnuevasSeñales);
        atras = findViewById(R.id.atrasDatos);
        visualizar = findViewById(R.id.buttonVisualizar);
    }

    private void configurarListeners() {
        try{
            comenzar.setOnClickListener(view-> dialogShow());
        }catch (Exception e){
            e.printStackTrace();
        }
        //iniciarActividad(SennalesActivity.class)
        atras.setOnClickListener(view -> retrocederActividad("com.example.datos.DatosParticipantesActivity"));
        visualizar.setOnClickListener(view -> iniciarActividad(Visualizar_TablaActivity.class));
    }

    private void iniciarActividad(Class<?> cls) {
        iniciarActividad(cls, true);
    }

    private void iniciarActividad(Class<?> cls, boolean incluirDatos) {
        Intent intent = new Intent(ComenzarSennalesActivity.this, cls);
        Bundle enviardatos = new Bundle();

        enviardatos.putSerializable("persona", people);
        if (incluirDatos) {
            enviardatos.putSerializable("datos", dato);
            enviardatos.putSerializable("clase", ComenzarSennalesActivity.class);
            if(cls.getName().equals( "com.example.sennales.SennalesActivity")){
                enviardatos.putInt("frecuencia",config_frec);
                enviardatos.putDouble("acelerometro", config_acel);
                enviardatos.putDouble("giroscopio", config_giros);
                enviardatos.putDouble("magnetometro", config_magne);
                enviardatos.putInt("seleccionfreciencia", select_frecuencia);
                enviardatos.putInt("seleccionAcel", select_acel);
                enviardatos.putInt("seleccionGiros", select_giros);
                enviardatos.putInt("seleccionTiempo", select_time);
                enviardatos.putString("dispositivo", select_dispositivo);
            }
        }

        intent.putExtras(enviardatos);
        startActivity(intent);
        LimpiarCache.cleanCache(ComenzarSennalesActivity.this);
        finish();
    }

    private void retrocederActividad(String className) {
        iniciarActividad(Retroceso.getClass(className), false);
    }

    private void Onback(){
        getOnBackPressedDispatcher().addCallback(ComenzarSennalesActivity.this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Maneja la acción personalizada aquí
                // Por ejemplo, muestra un mensaje de confirmación
                if (shouldAllowBackPress()) {
                    // Permitir retroceso
                    retrocederActividad("com.example.datos.DatosParticipantesActivity");
                } else {
                    // Mostrar un mensaje o hacer otra acción
                    Toast.makeText(ComenzarSennalesActivity.this, "No puedes volver atrás ahora", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean shouldAllowBackPress() {
        return true;
    }

    public void bloquearPorFecha(DataPeople dataPeople) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        if (!currentDate.equals(dataPeople.getDate())) {
            comenzar.setEnabled(false);
        }
    }

    public void dialogShow() {
        // Crear el AlertDialog

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.configurarsensores, null);

// Encuentra el Spinner en la vista inflada
        spinnerfrec = (Spinner) dialogView.findViewById(R.id.spinnerfm);
        spinneracel = (Spinner) dialogView.findViewById(R.id.spinner_accel_range);
        spinnergiros = (Spinner) dialogView.findViewById(R.id.spinner_giro_range);
        spinnertime = (Spinner) dialogView.findViewById(R.id.spinner_time);
        spinnerdisp = (Spinner) dialogView.findViewById(R.id.dispositivo);

// Verifica si el Spinner no es null
        if (spinnerfrec != null) {
            // Configura el adaptador
            ArrayAdapter<CharSequence> adapterdp = ArrayAdapter.createFromResource(this,
                    R.array.dispositivos, android.R.layout.simple_spinner_item);
            adapterdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerdisp.setAdapter(adapterdp);
        } else {
            Log.e("Error", "Spinner no inicializado correctamente en el Dialog");
        }

        if (spinnerfrec != null) {
            // Configura el adaptador
            ArrayAdapter<CharSequence> adapterfm = ArrayAdapter.createFromResource(this,
                    R.array.frecuencia, android.R.layout.simple_spinner_item);
            adapterfm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerfrec.setAdapter(adapterfm);
        } else {
            Log.e("Error", "Spinner no inicializado correctamente en el Dialog");
        }
        if (spinneracel != null) {
            // Configura el adaptador
            ArrayAdapter<CharSequence> adapteracel = ArrayAdapter.createFromResource(this,
                    R.array.acelerometro, android.R.layout.simple_spinner_item);
            adapteracel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinneracel.setAdapter(adapteracel);
        } else {
            Log.e("Error", "Spinner no inicializado correctamente en el Dialog");
        }
        if (spinnergiros != null) {
            // Configura el adaptador
            ArrayAdapter<CharSequence> adaptergiro = ArrayAdapter.createFromResource(this,
                    R.array.giroscopio, android.R.layout.simple_spinner_item);
            adaptergiro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnergiros.setAdapter(adaptergiro);
        } else {
            Log.e("Error", "Spinner no inicializado correctamente en el Dialog");
        }
        if(spinnertime != null){
            //Configurar el adaptador
            ArrayAdapter<CharSequence> adaptertime = ArrayAdapter.createFromResource(this,
                    R.array.time, android.R.layout.simple_spinner_item);
            adaptertime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnertime.setAdapter(adaptertime);
        } else {
            Log.e("Error", "Spinner no inicializado correctamente en el Dialog");
        }

// Configura el diálogo con la vista inflada
        builder.setView(dialogView)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    // Acciones al confirmar
                    select_frecuencia  = SeleccionSensores.seleccionFrecuencia(spinnerfrec);
                    select_acel = SeleccionSensores.seleccionAcele(spinneracel);
                    select_giros = SeleccionSensores.seleccionGiros(spinnergiros);
                    select_time = SeleccionSensores.seleccionTiempo(spinnertime);
                    select_dispositivo = SeleccionSensores.seleccionDispositivo(spinnerdisp, ComenzarSennalesActivity.this); // nombre del joycon

                    config_frec = CalcularTiempo.calcularFrecuenciaDeMuestreo(spinnerfrec);
                    config_acel = CorrecionesValores.AcelerometroEscala(SeleccionSensores.seleccionAcele(spinneracel));
                    config_giros = CorrecionesValores.GiroscopioEscala(SeleccionSensores.seleccionGiros(spinnergiros));
                    config_magne= CorrecionesValores.MagnetometroEscala(128);
                    Log.d("VAlor","El valor del config_magne es: " + config_magne);
                    iniciarActividad(SennalesActivity.class);
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