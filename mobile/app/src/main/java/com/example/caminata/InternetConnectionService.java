package com.example.caminata;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.enviodatos.gestionStrategy.GestorEnvioDatos;
import com.example.enviodatos.impl.EnviarUsuario;
import com.example.enviodatos.retrofit.RetrofitClient;
import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InternetConnectionService extends Service {
    private BroadcastReceiver networkReceiver;
    private Boolean lastConnectionState = null;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "internet_status_channel";
    private Context mycontext;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate() {
        super.onCreate();

        // Crear y activar la notificación de foreground
        Notification notification = createNotification("Servicio Activo", "Monitoreando la conexión a Internet.");
        startForeground(NOTIFICATION_ID, notification);

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkInternetConnection(context);
                mycontext = context;
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;

        if (connectivityManager != null) {
            Network network = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                network = connectivityManager.getActiveNetwork();
            }
            if (network != null) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                isConnected = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }

        if (lastConnectionState == null || lastConnectionState != isConnected) {
            lastConnectionState = isConnected;
            if (isConnected) {
                showNotification(context, "Conexión a Internet", "Estás conectado.");
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            ApiService apiService = RetrofitClient.getApiService(mycontext);
                            EnvioStrategy strategy = new EnviarUsuario(apiService);
                            GestorEnvioDatos gestorEnvioDatos = new GestorEnvioDatos(strategy);
                            gestorEnvioDatos.enviar(mycontext);
                            executor.shutdown(); // cerrar el executor
                        }catch (Exception e){
                            Toast.makeText(mycontext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                showNotification(context, "Sin Conexión a Internet", "No tienes acceso a Internet.");
            }
        }
    }

    private Notification createNotification(String title, String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Internet Service Channel", NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    private void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "internet_status_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Estado de Internet", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(2, builder.build());
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;

        if (connectivityManager != null) {
            Network network = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                network = connectivityManager.getActiveNetwork();
            }
            if (network != null) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                isConnected = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }
       return isConnected;
    }
}
