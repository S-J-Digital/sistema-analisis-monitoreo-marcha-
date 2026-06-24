package com.example.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.Bd_manager_Usuario;
import com.example.encriptacion.Encriptacion;
import com.example.enviodatos.Callback.LoginCallback;
import com.example.enviodatos.Dto.UserLogin;
import com.example.enviodatos.impl.Login;
import com.example.enviodatos.impl.PredecirRepository;
import com.example.enviodatos.retrofit.RetrofitClient;
import com.example.enviodatos.service.ApiService;
import com.example.principal.PrincipalActivity;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity implements Serializable {

    private EditText accountUser;
    private EditText accountPassword;
    private Button buttonLogin;
    private TextView newAccount;
    private Login repository;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        apiService = RetrofitClient.getApiService(LoginActivity.this);
        repository = new Login(apiService);
        String contraseña = Encriptacion.getdesencriptacion("6X/L7989MpPKKLZ/YDz7Qw==");
        setupListeners();
    }

    private void initViews() {
        accountUser = findViewById(R.id.accountUser);
        accountPassword = findViewById(R.id.accountpassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        newAccount = findViewById(R.id.crearCuenta);
    }

    private void setupListeners() {
        buttonLogin.setOnClickListener(view -> login());
        newAccount.setOnClickListener(view -> createNewAccount());
    }

    private void createNewAccount() {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    private void login() {
        String name = accountUser.getText().toString().trim();
        String password = accountPassword.getText().toString().trim();

        if (name.isEmpty() || password.isEmpty()) {
            showToast("Campos obligatorios vacíos");
            return;
        }

        Bd_manager_Usuario manager = new Bd_manager_Usuario(this);
        String encryptedPassword = Encriptacion.getencriptacion(password);

        try {
            if (manager.getpassword(name).equals(encryptedPassword)) {
               repository.login(new UserLogin(name, "123"), new LoginCallback() {
                   @Override
                   public void onSuccess(String token) {
                       if(!token.isEmpty()){
                           GetUser.guardarValor(LoginActivity.this, "user", name);
                           GetUser.guardarValor(LoginActivity.this, "password", encryptedPassword);
                           GetUser.guardarValor(LoginActivity.this, "rol", manager.getnombreRolbyUser(name));
                           GetUser.guardarValor(LoginActivity.this, "token", token);

                           Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                           intent.putExtra("clase", LoginActivity.class);
                           startActivity(intent);
                           finish(); // Este finish es opcional dependiendo de si quiero mantener la actividad en la pila de retroceso
                       }
                   }

                   @Override
                   public void onError(String error) {

                   }
               });
            } else {
                showToast("Nombre de usuario o contraseña incorrectos");
            }
        } catch (Exception e) {
            showToast("Error al iniciar sesión: " + e.getMessage());
        }
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }
}