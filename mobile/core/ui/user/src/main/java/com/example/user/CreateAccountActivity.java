package com.example.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.database.Bd_manager_Usuario;
import com.example.encriptacion.Encriptacion;
import com.example.enviodatos.gestionStrategy.GestorEnvioDatos;
import com.example.enviodatos.impl.EnviarUsuario;
import com.example.enviodatos.retrofit.RetrofitClient;
import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText newName;
    private EditText newUserName;
    private EditText newPass;
    private EditText repeatNewPass;
    private EditText numeroprofecional;
    private CheckBox pregunta;
    private LinearLayout layoutnoprofesional;
    private Bd_manager_Usuario manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount);

        initializeUI();
        setupCheckBox();
        setupCreateAccountButton();
    }

    private void initializeUI() {
        newName = findViewById(R.id.newaccountUser);
        newUserName = findViewById(R.id.newaccountUsername);
        newPass = findViewById(R.id.newaccountpassword);
        repeatNewPass = findViewById(R.id.repetircontrasenna);
        numeroprofecional = findViewById(R.id.newaccountNoprofesional);
        pregunta = findViewById(R.id.checkBoxpregunta);
        layoutnoprofesional = findViewById(R.id.no_profesional);
        manager = new Bd_manager_Usuario(this);
    }

    private void setupCreateAccountButton() {
        Button signIn = findViewById(R.id.buttonsigin);
        signIn.setOnClickListener(view -> createNewAccount());
    }

    private void createNewAccount() {
        String name = newName.getText().toString().trim();
        String username = newUserName.getText().toString().trim();
        String password = newPass.getText().toString().trim();
        String repeatPassword = repeatNewPass.getText().toString().trim();
        String noprofecional = numeroprofecional.getText().toString().trim();

        if (name.isEmpty() || username.isEmpty() ||password.isEmpty() || repeatPassword.isEmpty()) {
            showToast("Campos obligatorios vacios");
            return;
        }

        if(pregunta.isChecked()){
            if(noprofecional.isEmpty()){
                showToast("Ingrese el número profesional");
                return;
            }

            if(noprofecional.matches("\\d{4}")){
                showToast("El número profesional deben ser 5 números");
                return;
            }
        }

        if (!password.equals(repeatPassword)) {
            showToast("Las contraseñas no coinciden");
            return;
        }

        if(manager.user_getID(username) != -1){
            showToast("Nombre de usuario en uso");
            return;
        }

        manager.user_insert(name,username ,Encriptacion.getencriptacion(password), noprofecional,"Administrador");
        int i = manager.user_getID(name);

    }

    private void setupCheckBox(){
        pregunta.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(pregunta.isChecked()){
                layoutnoprofesional.setVisibility(View.VISIBLE);
            }else{
                layoutnoprofesional.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}