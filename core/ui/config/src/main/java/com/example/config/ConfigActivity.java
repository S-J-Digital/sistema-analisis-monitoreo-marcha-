package com.example.config;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cache.LimpiarCache;
import com.example.database.Bd_manager_Usuario;
import com.example.encriptacion.Encriptacion;
import com.example.enviodatos.gestionStrategy.GestorEnvioDatos;
import com.example.enviodatos.impl.EnviarUsuario;
import com.example.enviodatos.retrofit.RetrofitClient;
import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;
import com.example.funcionalidadesconfig.ExportarPDF;
import com.example.funcionalidadesconfig.ReporteManager;
import com.example.retroceso.Retroceso;
import com.example.user.GetUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigActivity extends AppCompatActivity {
    private Class<?> clase;
    private boolean isVisible = false;
    private Intent serviceIntent;
    private InternetConnectionService connectionService = new InternetConnectionService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        //Iniciar servicio
        serviceIntent = new Intent(this, InternetConnectionService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

        if (getIntent().hasExtra("clase")) {
            clase = (Class<?>) getIntent().getSerializableExtra("clase");
            Retroceso.insertarClass(clase);
        }

        setupUI();
    }

    private void setupUI() {
        setupAtrasPrincipal();
        setupCerrarSesion();
        setupEliminarCuenta();
        llenarText();
        setupVerPass();
        setupSave();
        setupExportarPdf();
    }

    private void setupAtrasPrincipal() {
        ImageButton imageButton = findViewById(R.id.atras_principal);
        imageButton.setOnClickListener(view -> {
            Intent intent = new Intent(ConfigActivity.this, Retroceso.getClass("com.example.principal.PrincipalActivity"));
            startActivity(intent);
            LimpiarCache.cleanCache(ConfigActivity.this);
            finish();
        });
    }

    private void setupCerrarSesion() {
        AppCompatButton cerrar = findViewById(R.id.cerrar);
        cerrar.setOnClickListener(view -> showAlertDialog(
                "¿Estás seguro que desea cerrar la sesión?",
                (dialog, id) -> {
                    Intent intent = new Intent(ConfigActivity.this, Retroceso.getClass("com.example.user.LoginActivity"));
                    GetUser.guardarValor(ConfigActivity.this, "user", "");
                    GetUser.guardarValor(ConfigActivity.this, "password", "");
                    startActivity(intent);
                    finish();
                }
        ));
    }

    private void setupEliminarCuenta() {
        AppCompatButton eliminar = findViewById(R.id.eliminar);
        eliminar.setOnClickListener(view -> showAlertDialog(
                "¿Estás seguro que desea eliminar la cuenta?",
                (dialog, id) -> {
                   if(connectionService.isConnected(ConfigActivity.this)){
                       Bd_manager_Usuario manager = new Bd_manager_Usuario(ConfigActivity.this);

                       ApiService apiService = RetrofitClient.getApiService(ConfigActivity.this);
                       EnvioStrategy strategy = new EnviarUsuario(apiService);
                       GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
                       gestorEnvioDatos.obtenerIdDelete(GetUser.leerValor(ConfigActivity.this, "user"));

                       manager.user_delete(GetUser.leerValor(ConfigActivity.this, "user"));
                       GetUser.guardarValor(ConfigActivity.this, "user", "");
                       GetUser.guardarValor(ConfigActivity.this, "password", "");

                       Intent intent = new Intent(ConfigActivity.this, Retroceso.getClass("com.example.user.LoginActivity"));
                       startActivity(intent);
                       finish();
                   }else{
                       showNoInternetAlertDialog("Necesita estar conectado para eliminar la cuenta");
                   }
                }
        ));
    }

    private void llenarText() {
        EditText userName = findViewById(R.id.userName);
        EditText password = findViewById(R.id.password);
        EditText noprofesional = findViewById(R.id.noprofesional);
        String np=null;
        userName.setText(GetUser.leerValor(ConfigActivity.this, "user"));
        password.setText(Encriptacion.getdesencriptacion(GetUser.leerValor(ConfigActivity.this, "password")));

        Bd_manager_Usuario manager = new Bd_manager_Usuario(ConfigActivity.this);
        np =manager.getnoprofesional(userName.getText().toString());
        noprofesional.setText(np);

    }

    private void setupVerPass() {
        ImageView verpass = findViewById(R.id.verpass);
        EditText password = findViewById(R.id.password);
        verpass.setOnClickListener(view -> {
            int inputType = isVisible ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT;
            password.setInputType(inputType);
            isVisible = !isVisible;
            password.setSelection(password.length());
        });
    }

    private void setupSave() {
       try{
           AppCompatButton save = findViewById(R.id.Modificar);
           EditText userName = findViewById(R.id.userName);
           EditText password = findViewById(R.id.password);
           EditText nprofesional = findViewById(R.id.noprofesional);

           save.setOnClickListener(view -> {
               Bd_manager_Usuario manager = new Bd_manager_Usuario(ConfigActivity.this);
               String currentUserName = GetUser.leerValor(ConfigActivity.this, "user");
               String currentPassword = GetUser.leerValor(ConfigActivity.this, "password");
               String newUserName = userName.getText().toString();
               String newPassword = Encriptacion.getencriptacion(password.getText().toString());
               String nprofe = nprofesional.getText().toString();
               String last = manager.getnoprofesional(currentUserName);


               if (connectionService.isConnected(ConfigActivity.this)) {
                   if (!currentUserName.equals(newUserName) || !currentPassword.equals(newPassword) || !nprofe.equals(last)) {

                       manager.user_update(currentUserName, currentPassword, newUserName, newPassword, nprofe, last);
                       GetUser.guardarValor(ConfigActivity.this, "user", newUserName);
                       GetUser.guardarValor(ConfigActivity.this, "password", newPassword);

                       ApiService apiService = RetrofitClient.getApiService(ConfigActivity.this);
                       EnvioStrategy strategy = new EnviarUsuario(apiService);
                       GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
                       gestorEnvioDatos.obtenerIdUpdate(currentUserName,manager.getUser(newUserName),ConfigActivity.this,"modificar");

                       Toast.makeText(ConfigActivity.this, "Usuario actualizado exitosamente", Toast.LENGTH_LONG).show();
                   }
               } else {
                   showNoInternetAlertDialog("Necesita estar conectado para modificar la cuenta");
               }

           });
       }catch (Exception e){
           Toast.makeText(ConfigActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
       }

    }

    private void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "internet_status_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Estado de Internet", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(2, builder.build());
    }

    private void setupExportarPdf() {
        AppCompatButton exportar = findViewById(R.id.exportar);
        if(GetUser.leerValor(ConfigActivity.this,"rol").equals("Investigador")){
            exportar.setVisibility(View.GONE);
        }else{
            ReporteManager reporteManager = new ReporteManager(ConfigActivity.this);
            exportar.setOnClickListener(v -> reporteManager.generarReporte(ConfigActivity.this));
        }

    }

    private void showAlertDialog(String message, DialogInterface.OnClickListener positiveAction) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Sí", positiveAction)
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss())
                .create()
                .show();
    }

    private void showNoInternetAlertDialog(String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle("Sin Conexión")
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> dialog.dismiss())
                .show();
    }
}