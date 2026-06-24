package com.example.sennales;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cache.LimpiarCache;
import com.example.database.Bd_manager_Sennales;
import com.example.funcionalidadesbluetooth.BluetoothJoyCon;
import com.example.funcionalidadesbluetooth.JoyConListener;
import com.example.getsensores.CleanCache;
import com.example.getsensores.DetenerPruebas;
import com.example.getsensores.Iniciar;
import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;
import com.example.retroceso.Retroceso;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SennalesActivity extends AppCompatActivity implements SensorEventListener {


    // Gráficos del acelerómetro
    private LineChart acelerometroX;
    private LineChart acelerometroY;
    private LineChart acelerometroZ;
    private List<Entry> accelerometerDataX = new ArrayList<>();
    private List<Entry> accelerometerDataY = new ArrayList<>();
    private List<Entry> accelerometerDataZ = new ArrayList<>();

    // Gráficos del giroscopio
    private LineChart giroscopioX;
    private LineChart giroscopioY;
    private LineChart giroscopioZ;
    private List<Entry> gyroscopeDataX = new ArrayList<>();
    private List<Entry> gyroscopeDataY = new ArrayList<>();
    private List<Entry> gyroscopeDataZ = new ArrayList<>();

    // Gráficos del magnetómetro
    private LineChart magnetometroX;
    private LineChart magnetometroY;
    private LineChart magnetometroZ;
    private List<Entry> magnetometerDataX = new ArrayList<>();
    private List<Entry> magnetometerDataY = new ArrayList<>();
    private List<Entry> magnetometerDataZ = new ArrayList<>();

    //Variables
    private double tempe;
    private BroadcastReceiver bateria;
    private SensorManager sensorManager;
    private Sensor  accelerometerSensor, gyroscopeSensor, magnetometerSensor;;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private People people;
    private DataPeople dato;
    private double config_acel;
    private double config_giros;
    private double config_magne;
    private int config_frecuencia;
    private int selec_frecuencia;
    private int selec_acel;
    private int selec_giros;
    private int selec_time;
    private String select_dispo;
    private Bd_manager_Sennales manager;

    // Elementos de la interfaz
    private LinearLayout acelerometro;
    private LinearLayout giroscopio;
    private LinearLayout magnetometro;
    private TextView temperatura;
    private TextView countdownText;
    private AppCompatButton detener;
    private ImageButton atras;
    //Valores sensor
    private double aceX;
    private double aceY;
    private double aceZ;
    private double giroX;
    private double giroY;
    private double giroZ;
    private double magX;
    private double magY;
    private double magZ;

    //Solucion al error
    private long lastChartUpdateTime = 0;
    private  long CHART_UPDATE_INTERVAL = 50; // Actualiza gráficos cada 500ms
    private final int MAX_ENTRIES = 200; // Máximo de puntos por gráfico
    float ax = 0, ay = 0, az = 0;
    float gx = 0, gy = 0, gz = 0;
    float mx = 0, my = 0, mz = 0;

    // Crear un ExecutorService para manejar los hilos
    ExecutorService executor = Executors.newSingleThreadExecutor();

    // Detectar Joycon
    private boolean usandoJoyCon;
    private BluetoothJoyCon joyConManager;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (Iniciar.isComenzar()) {
            try {


                if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                    ax = (float) (sensorEvent.values[0] * config_acel);
                    ay = (float) (sensorEvent.values[1] * config_acel);
                    az = (float) (sensorEvent.values[2] * config_acel * -1);


                    addLimitedEntry(accelerometerDataX, ax);
                    addLimitedEntry(accelerometerDataY, ay);
                    addLimitedEntry(accelerometerDataZ, az);

                    updateChart(acelerometroX, accelerometerDataX, "");
                    updateChart(acelerometroY, accelerometerDataY, "");
                    updateChart(acelerometroZ, accelerometerDataZ, "");

                }

                if(sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE){
                     gx = (float) (sensorEvent.values[0] * config_giros);
                     gy = (float) (sensorEvent.values[1] * config_giros);
                     gz = (float) (sensorEvent.values[2] * config_giros * -1);


                    addLimitedEntry(gyroscopeDataX, gx);
                    addLimitedEntry(gyroscopeDataY, gy);
                    addLimitedEntry(gyroscopeDataZ, gz);

                    updateChart(giroscopioX, gyroscopeDataX, "");
                    updateChart(giroscopioY, gyroscopeDataY, "");
                    updateChart(giroscopioZ, gyroscopeDataZ, "");

                }

                if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                     mx = (float) (sensorEvent.values[0] * config_magne);
                     my = (float) (sensorEvent.values[1] * config_magne);
                     mz = (float) (sensorEvent.values[2] * config_magne);


                    addLimitedEntry(magnetometerDataX, mx);
                    addLimitedEntry(magnetometerDataY, my);
                    addLimitedEntry(magnetometerDataZ, mz);

                    updateChart(magnetometroX, magnetometerDataX, "");
                    updateChart(magnetometroY, magnetometerDataY, "");
                    updateChart(magnetometroZ, magnetometerDataZ, "");

                }

                // Ejecutar el guardado en segundo plano para no congelar la UI
                // Ejecutar el guardado en segundo plano utilizando ExecutorService
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Asegúrate de que la base de datos esté abierta cuando realices operaciones
                            insertarSennal(ax, ay, az, gx, gy, gz, mx, my, mz);
                        } catch (Exception e) {
                            Log.e("DatabaseError", "Error al insertar en la base de datos", e);
                        }
                    }
                });

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sennales);

        // Inicializar elementos de la interfaz
        countdownText = findViewById(R.id.countdown_text);
        detener = findViewById(R.id.detener);
        acelerometro = findViewById(R.id.chartAccelerometerContainer);
        giroscopio = findViewById(R.id.chartGiroscopioContainer);
        magnetometro = findViewById(R.id.chartMagnetometroContainer);
        temperatura = findViewById(R.id.tempValor);
        atras = findViewById(R.id.atras_visualizar);

        // Obtener datos del intent
        Bundle recibirdatos = getIntent().getExtras();
        if (recibirdatos != null) {
            people = (People) recibirdatos.get("persona");
            dato = (DataPeople) recibirdatos.get("datos");
            config_frecuencia = (int) recibirdatos.getInt("frecuencia");
            config_acel = (double) recibirdatos.getDouble("acelerometro");
            config_giros = (double) recibirdatos.getDouble("giroscopio");
            config_magne = (double) recibirdatos.getDouble("magnetometro");
            selec_frecuencia = (int) recibirdatos.getInt("seleccionfreciencia");
            selec_acel= (int) recibirdatos.getInt("seleccionAcel");
            selec_giros= (int) recibirdatos.getInt("seleccionGiros");
            selec_time = (int) recibirdatos.getInt("seleccionTiempo");
            select_dispo = (String) recibirdatos.get("dispositivo");//nombre del joycon
            Class<?> clase = (Class<?>) recibirdatos.get("clase");
            Retroceso.insertarClass(clase);
        }

        // Inicializar otros componentes
        manager = new Bd_manager_Sennales(SennalesActivity.this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido2);
        Iniciar.iniciarCuentaRegresiva(countdownText, vibrator, mediaPlayer, selec_time);
        if(!select_dispo.isEmpty()){
            usandoJoyCon = true;
            //iniciar coger los datos del joy-con
            iniciarJoyCon();
        }

        // Configurar el receptor de la batería
        bateria = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
                tempe = temp / 10.0;
                temperatura.setText(String.format("%.2f", tempe));
            }
        };
        registerReceiver(bateria, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        // Configurar el botón de detener
        DetenerPruebas.Detenerpruebas(this,detener, atras);
        atras();
       volver();
    }



    private void updateChart(LineChart chart, List<Entry> data, String label) {
        LineDataSet dataSet = new LineDataSet(data, label);
        dataSet.setColor(Color.RED);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // Refresca el gráfico
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(usandoJoyCon){
            acelerometroX = agregarGrafica("Acelerómetro X", acelerometro);
            acelerometroY = agregarGrafica("Acelerómetro Y", acelerometro);
            acelerometroZ = agregarGrafica("Acelerómetro Z", acelerometro);

            giroscopioX = agregarGrafica("Giroscopio X", giroscopio);
            giroscopioY = agregarGrafica("Giroscopio Y", giroscopio);
            giroscopioZ = agregarGrafica("Giroscopio Z", giroscopio);
        }else{
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

            if (sensorManager != null) {
                accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

                // Registrar el acelerómetro si está disponible
                if (accelerometerSensor != null) {
                    sensorManager.registerListener(this, accelerometerSensor, getSensorDelayFromFrequency(config_frecuencia));
                    acelerometroX = agregarGrafica("Acelerómetro X", acelerometro);
                    acelerometroY = agregarGrafica("Acelerómetro Y", acelerometro);
                    acelerometroZ = agregarGrafica("Acelerómetro Z", acelerometro);
                }
                // Registrar el giroscopio si está disponible
                if (gyroscopeSensor != null) {
                    sensorManager.registerListener(this, gyroscopeSensor, getSensorDelayFromFrequency(config_frecuencia));
                    giroscopioX = agregarGrafica("Giroscopio X", giroscopio);
                    giroscopioY = agregarGrafica("Giroscopio Y", giroscopio);
                    giroscopioZ = agregarGrafica("Giroscopio Z", giroscopio);
                }
                // Registrar el magnetómetro si está disponible
                if (magnetometerSensor != null) {
                    sensorManager.registerListener(this, magnetometerSensor, getSensorDelayFromFrequency(config_frecuencia));
                    magnetometroX = agregarGrafica("Magnetómetro X", magnetometro);
                    magnetometroY = agregarGrafica("Magnetómetro Y", magnetometro);
                    magnetometroZ = agregarGrafica("Magnetómetro Z", magnetometro);
                }
            }
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            if (accelerometerSensor != null) {
                sensorManager.unregisterListener(this, accelerometerSensor);
            }
            if (gyroscopeSensor != null) {
                sensorManager.unregisterListener(this, gyroscopeSensor);
            }
            if (magnetometerSensor != null) {
                sensorManager.unregisterListener(this, magnetometerSensor);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sensorManager != null) {
            if (accelerometerSensor != null) {
                sensorManager.unregisterListener(this, accelerometerSensor);
            }
            if (gyroscopeSensor != null) {
                sensorManager.unregisterListener(this, gyroscopeSensor);
            }
            if (magnetometerSensor != null) {
                sensorManager.unregisterListener(this, magnetometerSensor);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se requiere ninguna acción en este método.
    }

    protected void atras(){
        getOnBackPressedDispatcher().addCallback(SennalesActivity.this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Maneja la acción personalizada aquí
                // Por ejemplo, muestra un mensaje de confirmación
                if (shouldAllowBackPress()) {
                    // Permitir retroceso
                   retrocederActividad("com.example.sennales.ComenzarSennalesActivity");
                } else {
                    // Mostrar un mensaje o hacer otra acción
                    Toast.makeText(SennalesActivity.this, "No puedes volver atrás ahora", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Método que decide si permitir o no el retroceso
    private boolean shouldAllowBackPress() {
        // Lógica para decidir si se permite la navegación hacia atrás
        return true;
    }

    private int getSensorDelayFromFrequency(int frecuencia) {
        // Comparar la frecuencia y asignar la constante apropiada
        if (frecuencia == 1000) {
            return SensorManager.SENSOR_DELAY_UI; // Máxima frecuencia (aproximadamente 1000 Hz)
        } else if (frecuencia == 500) {
            return SensorManager.SENSOR_DELAY_UI; // Aproximadamente 500 Hz
        } else if (frecuencia == 333) {
            return SensorManager.SENSOR_DELAY_GAME; // Aproximadamente 333 Hz
        } else if (frecuencia == 250) {
            return SensorManager.SENSOR_DELAY_GAME; // Aproximadamente 250 Hz
        } else if (frecuencia == 200) {
            return SensorManager.SENSOR_DELAY_NORMAL; // Aproximadamente 200 Hz
        } else {
            // Si la frecuencia no está en el rango esperado, asigna un valor por defecto
            return SensorManager.SENSOR_DELAY_UI;
        }
    }

    private LineChart agregarGrafica(String titulo, LinearLayout container) {
        LineChart chart = new LineChart(this);
        chart.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                500 // Altura fija para cada gráfico
        ));

        Description desc = new Description();
        desc.setText(titulo);
        chart.setDescription(desc);

        LineData data = new LineData();
        chart.setData(data);
        chart.invalidate();

        container.addView(chart);
        return chart;
    }

    private void addLimitedEntry(List<Entry> list, float value) {
        list.add(new Entry(list.size(), value));
        if (list.size() > MAX_ENTRIES) {
            list.remove(0);
            // Ajustar índices para que el eje X siga en orden
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setX(i);
            }
        }
    }

    private void insertarSennal(float ax, float ay, float az,float gx, float gy, float gz, float mx, float my, float mz){
        manager.sennal_insert(people,dato,(double)ax,(double)ay,(double)az,
                (double)gx,(double)gy,(double)gz,(double)mx,(double)my,
                mz,tempe,selec_acel,selec_giros,selec_frecuencia);
        Log.d("Valores", "El valor de la configuración es giros es: " +selec_giros + "El valor de la config del ace es: " + selec_acel +"El valor de la frec es: " + selec_frecuencia);
    }

    private void iniciarActividad(Class<?> cls) {
        String classname = "com.example.sennales.ComenzarSennalesActivity";
        Intent intent = new Intent(SennalesActivity.this,cls);
        Bundle enviardatos = new Bundle();
        enviardatos.putSerializable("persona", people);
        enviardatos.putSerializable("dato",dato);
        enviardatos.putSerializable("clase",SennalesActivity.class);
        intent.putExtras(enviardatos);
        startActivity(intent);
        LimpiarCache.cleanCache(SennalesActivity.this);
        finish();
    }

    private void retrocederActividad(String className) {
        iniciarActividad(Retroceso.getClass(className));
    }

    public void volver(){
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrocederActividad("com.example.sennales.ComenzarSennalesActivity");
            }
        });
    }

    private void iniciarJoyCon(){
        joyConManager = new BluetoothJoyCon();
        JoyConListener joyConListener = new JoyConListener() {
            @Override
            public void onConectado() {
                runOnUiThread(()-> Toast.makeText(SennalesActivity.this,
                        "Joy-Con conectado", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onDesconectado() {
                runOnUiThread(()-> Toast.makeText(SennalesActivity.this,
                        "Joy-Con desconectado", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onSennalRecibida(Sennales sennales) {
                if(!Iniciar.isComenzar()) {
                    return;
                }

                ax=(float) (sennales.getAcelerometroX() * config_acel);
                ay=(float) (sennales.getAcelerometroY() * config_acel);
                az=(float) (sennales.getAcelerometroZ() * config_acel);

                gx = (float) (sennales.getGiroscopioX() * config_giros);
                gy = (float) (sennales.getGiroscopioY() * config_giros);
                gz = (float) (sennales.getAcelerometroZ() * config_giros * -1);

                runOnUiThread(()->{
                    addLimitedEntry(accelerometerDataX, ax);
                    addLimitedEntry(accelerometerDataY, ay);
                    addLimitedEntry(accelerometerDataZ, az);

                    updateChart(acelerometroX, accelerometerDataX, "");
                    updateChart(acelerometroY, accelerometerDataY, "");
                    updateChart(acelerometroZ, accelerometerDataZ, "");

                    addLimitedEntry(gyroscopeDataX, gx);
                    addLimitedEntry(gyroscopeDataY, gy);
                    addLimitedEntry(gyroscopeDataZ, gz);

                    updateChart(giroscopioX, gyroscopeDataX, "");
                    updateChart(giroscopioY, gyroscopeDataY, "");
                    updateChart(giroscopioZ, gyroscopeDataZ, "");
                });
            }

            @Override
            public void onError(String mensaje) {
                runOnUiThread(()-> Toast.makeText(SennalesActivity.this,
                        mensaje, Toast.LENGTH_SHORT).show());
            }
        };

        joyConManager.conectar(SennalesActivity.this, select_dispo);
    }
}