package com.example.interfazinsertar;


import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cache.LimpiarCache;
import com.example.interfazinsertar.validacion.ValidacionDatosModificar;
import com.example.interfazinsertar.validacion.ValidacionParticipanteModificar;
import com.example.interfazinsertar.validacion.ValidacionTodosLosDatos;

import com.example.model.People;
import com.example.model.DataPeople;

import java.util.List;

public class ParticipanteActivity extends AppCompatActivity {

    private ValidacionDatosModificar validacionDatosModificar = new ValidacionDatosModificar();
    private DataPeople datos;
    private People people;
    private Class<?> clase;
    private int bandera = -1;

    private EditText editTextEdad, editTextCalzado, editTextMedicion, editTextPierna, editTextAltura, editTextObservacion;
    private EditText nombre, ci, telefono;
    private RadioButton radioButtonSi, radioButtonNo, femenino, masculino;
    private TextView textViewObservaciones;
    private Button botonGuardar;
    private ImageButton botonAtras;
    private Bundle recibirdato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participante);

        initializeViews();
        handleIntentData(getIntent().getExtras(), getIntent().getStringExtra("crear"));

        selecionados();
        setupListeners();
        OnBack();
    }

    private void initializeViews() {
        editTextEdad = findViewById(R.id.editTextNumberEdad);
        editTextCalzado = findViewById(R.id.editTextNumberCalzado);
        editTextMedicion = findViewById(R.id.editTextNumberMedicion);
        editTextPierna = findViewById(R.id.editTextNumberPierna);
        editTextAltura = findViewById(R.id.editTextNumberAltura);
        editTextObservacion = findViewById(R.id.editObservacion);

        nombre = findViewById(R.id.editTextNombre);
        ci = findViewById(R.id.editTextCI);
        telefono = findViewById(R.id.editTextTelefono);

        radioButtonSi = findViewById(R.id.radioButtonSi);
        radioButtonNo = findViewById(R.id.radioButtonNo);
        femenino = findViewById(R.id.radioButtonfemenino);
        masculino = findViewById(R.id.radioButtonMasculino);

        textViewObservaciones = findViewById(R.id.textObservacion);

        botonGuardar = findViewById(R.id.buttonGuardar);
        botonAtras = findViewById(R.id.atrasLista);
    }

    private void handleIntentData(Bundle recibirdatos, String crear) {
        if (recibirdatos == null) {
            return;
        }
        recibirdato = recibirdatos;
        if(recibirdatos.size() == 1){
            clase = (Class<?>) recibirdatos.get("clase");
        }else if (recibirdatos.size() == 2) {
            clase = (Class<?>) recibirdatos.get("clase");
            people = (People) recibirdatos.get("persona");

            isnotEnable_datos();
            llenar_datos();
        } else if (crear == null) {
            clase = (Class<?>) recibirdatos.get("clase");
            people = (People) recibirdatos.get("persona");
            datos = (DataPeople) recibirdatos.get("datos");
            llenar_datos();
            isnotEnable_participante();
        } else {
            clase = (Class<?>) recibirdatos.get("clase");
            people = (People) recibirdatos.get("persona");
            bandera = recibirdatos.size();
            llenar_datos();
            isnotEnable_participante();
        }
    }

    private void setupListeners() {
        botonGuardar.setOnClickListener(view -> handleSave());

        botonAtras.setOnClickListener(view -> handleBack());
    }

    private void handleSave() {
        try {
            if (people == null) {
                if (ValidacionTodosLosDatos.datosCorrectos(this, people, nombre, ci, telefono, femenino, masculino, editTextEdad, editTextCalzado,
                        editTextMedicion, editTextPierna, editTextAltura, editTextObservacion, radioButtonSi, radioButtonNo)) {
                    saveNewParticipant();
                }
            }else if(recibirdato.size() == 2){
                if (ValidacionParticipanteModificar.datos_modificados_correctos(this, nombre, ci, telefono, femenino, masculino)) {
                    modifyParticipant();
                }

            }else if (datos == null) {
                if (validacionDatosModificar.datos_correctos_datosparticipante(this, editTextEdad, editTextCalzado,
                        editTextMedicion, editTextPierna, editTextAltura, editTextObservacion, radioButtonSi, radioButtonNo)) {
                    saveParticipantData();
                }
            } else {
                if (validacionDatosModificar.datos_correctos_datosparticipante(this, editTextEdad, editTextCalzado,
                        editTextMedicion, editTextPierna, editTextAltura, editTextObservacion, radioButtonSi, radioButtonNo)) {
                    modifyParticipantData();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Modificar datos", e.getMessage());
        }
    }

    private void handleBack() {
        Intent intent = new Intent(this, clase);
        if (datos == null && bandera == -1) {
            startActivity(intent);
        } else {
            Bundle enviardatos = new Bundle();
            enviardatos.putSerializable("persona", people);
            intent.putExtras(enviardatos);
            startActivity(intent);
        }
        LimpiarCache.cleanCache(this);
        finish();
    }

    private void saveNewParticipant() {
        try{
            InsertarParticipante.Guardar_participante(this, nombre, ci, telefono, femenino, masculino);
            InsertarDatos.Guardar_datos_participante(this, editTextEdad, editTextCalzado, editTextMedicion, editTextPierna, editTextAltura,
                    editTextObservacion, radioButtonSi, radioButtonNo, ci);
            Toast.makeText(this, "Datos Insertados", Toast.LENGTH_SHORT).show();
            startNextActivity();
        }catch (Exception e){
            Toast.makeText(this, "Hay un error", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveParticipantData() {
        InsertarDatos.Guardar_datos_participante(this, editTextEdad, editTextCalzado, editTextMedicion, editTextPierna, editTextAltura,
                editTextObservacion, radioButtonSi, radioButtonNo, ci);
        Toast.makeText(this, "Datos Guardados", Toast.LENGTH_SHORT).show();
        startNextActivity();
    }

    private void modifyParticipant() {
        ModificarParticipante.modificar_participante(this, nombre, ci, telefono, femenino, masculino, people);
        people = null;
        Toast.makeText(this, "Datos modificados", Toast.LENGTH_SHORT).show();
        startNextActivity();
    }

    private void modifyParticipantData() {
        ModificarDatos.Modificar_datos(this, editTextEdad, editTextCalzado, editTextMedicion, editTextPierna, editTextAltura,
                editTextObservacion, radioButtonSi, radioButtonNo, ci, datos);
        Toast.makeText(this, "Datos modificados", Toast.LENGTH_SHORT).show();
        startNextActivity();
    }

    private void startNextActivity() {
        Intent intent = new Intent(this, clase);
        Bundle enviardatos = new Bundle();
        enviardatos.putSerializable("persona", people);
        intent.putExtras(enviardatos);
        startActivity(intent);
        LimpiarCache.cleanCache(this);
        finish();
    }

    public void selecionados() {
        setupRadioButton(femenino, masculino);
        setupRadioButton(masculino, femenino);
        setupRadioButtonWithVisibility(radioButtonSi, radioButtonNo, textViewObservaciones, editTextObservacion);
        setupRadioButtonWithVisibility(radioButtonNo, radioButtonSi, textViewObservaciones, editTextObservacion);
    }

    private void setupRadioButton(RadioButton rb1, RadioButton rb2) {
        rb1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rb2.setChecked(false);
            }
        });
    }

    private void setupRadioButtonWithVisibility(RadioButton rb1, RadioButton rb2, TextView tv, EditText et) {
        rb1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rb2.setChecked(false);
                tv.setVisibility(rb1.getText().equals("Si") ? View.VISIBLE : View.INVISIBLE);
                et.setVisibility(rb1.getText().equals("Si") ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    public void llenar_datos() {
        nombre.setText(people.getName());
        ci.setText(people.getCi());
        telefono.setText(people.getTelefono());

        if (people.getSexo().equals("Femenino")) {
            femenino.setChecked(true);
        } else {
            masculino.setChecked(true);
        }

        if (datos != null) {
            editTextEdad.setText(String.valueOf(datos.getAge()));
            editTextCalzado.setText(String.valueOf(datos.getShoes()));
            editTextMedicion.setText(String.valueOf(datos.getCinturaTobillo()));
            editTextPierna.setText(String.valueOf(datos.getLeg()));
            editTextAltura.setText(String.valueOf(datos.getHeight()));

            if (datos.getAffection() == 1) {
                radioButtonSi.setChecked(true);
                editTextObservacion.setVisibility(View.VISIBLE);
                List<String> observaciones = datos.getObservation();  // Supongamos que es una lista
                String observacionConcatenada = TextUtils.join(", ", observaciones);  // Une elementos con comas
                editTextObservacion.setText(observacionConcatenada);// Establece el texto sin corchetes
                editTextObservacion.setEnabled(false);
            } else {
                radioButtonNo.setChecked(true);
            }
        }
    }

    public void isnotEnable_datos() {
        disableView(editTextEdad, editTextCalzado, editTextMedicion, editTextPierna, editTextAltura, editTextObservacion);
        radioButtonNo.setEnabled(false);
        radioButtonSi.setEnabled(false);
    }

    public void isnotEnable_participante() {
        disableView(nombre, ci, telefono);
        radioButtonNo.setEnabled(true);
        radioButtonSi.setEnabled(true);
        femenino.setEnabled(false);
        masculino.setEnabled(false);
    }

    private void disableView(View... views) {
        for (View view : views) {
            view.setEnabled(false);
        }
    }

    private void OnBack(){
        getOnBackPressedDispatcher().addCallback(ParticipanteActivity.this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Maneja la acción personalizada aquí
                // Por ejemplo, muestra un mensaje de confirmación
                if (shouldAllowBackPress()) {
                    // Permitir retroceso
                    handleBack();
                } else {
                    // Mostrar un mensaje o hacer otra acción
                    Toast.makeText(ParticipanteActivity.this, "No puedes volver atrás ahora", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean shouldAllowBackPress() {
        return true;
    }
}