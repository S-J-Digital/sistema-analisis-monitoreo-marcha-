package com.example.sennales;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cache.LimpiarCache;
import com.example.database.Bd_manager_Sennales;
import com.example.database.Bd_manager_patologias;
import com.example.enviodatos.Dto.RedNeuronal.ModeloRequest;
import com.example.enviodatos.Dto.RedNeuronal.Prediccion;
import com.example.enviodatos.Dto.RedNeuronal.PredictionCallback;
import com.example.enviodatos.Dto.RedNeuronal.Sennal;
import com.example.enviodatos.impl.PredecirRepository;
import com.example.enviodatos.retrofit.RetrofitClient;
import com.example.enviodatos.service.ApiService;
import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;
import com.example.retroceso.Retroceso;
import com.example.visualizarfuncionalidades.Strategy.Clases.AceXStrategy;
import com.example.visualizarfuncionalidades.Strategy.Clases.AceYStrategy;
import com.example.visualizarfuncionalidades.Strategy.Clases.AceZStrategy;
import com.example.visualizarfuncionalidades.Strategy.Clases.GyroXStrategy;
import com.example.visualizarfuncionalidades.Strategy.Clases.GyroYStrategy;
import com.example.visualizarfuncionalidades.Strategy.Clases.GyroZStrategy;
import com.example.visualizarfuncionalidades.Strategy.Clases.MagXStrategy;
import com.example.visualizarfuncionalidades.Strategy.Clases.MagYStrategy;
import com.example.visualizarfuncionalidades.Strategy.Clases.MagZStrategy;
import com.example.visualizarfuncionalidades.Strategy.Control.VisualizadorGrafica;
import com.example.visualizarfuncionalidades.TemperaturaPromedio;
import com.example.visualizarfuncionalidades.VisualizarAceX;
import com.example.visualizarfuncionalidades.VisualizarAceY;
import com.example.visualizarfuncionalidades.VisualizarAceZ;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GraficaSennalesActivity extends AppCompatActivity implements Serializable {
    private People people;
    private DataPeople dato;
    private Sennales sennales;
    private Class clase;
    private PredecirRepository repository;
    private ApiService apiService;

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

    private LinearLayout acelerometro;
    private LinearLayout giroscopio;
    private LinearLayout magnetometro;
    private TextView temperatura;
    private TextView diagnostico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica_sennales);

        initializeData();
        initializeViews();
        setupCharts();
        setupBackButton();
    }

    private void initializeData() {
        Bundle recibirdatos = getIntent().getExtras();
        if (recibirdatos != null) {
            people = (People) recibirdatos.get("persona");
            dato = (DataPeople) recibirdatos.get("datos");
            clase = (Class) recibirdatos.get("clase");
            sennales = (Sennales) recibirdatos.get("sennal");
            Retroceso.insertarClass(clase);
        }
    }

    private void initializeViews() {
        acelerometro = findViewById(R.id.chartAccelerometerContainer);
        magnetometro = findViewById(R.id.chartMagnetometroContainer);
        giroscopio = findViewById(R.id.chartGiroscopioContainer);
        creargraficas();
        temperatura = findViewById(R.id.tempValor);
        diagnostico = findViewById(R.id.diagnosticoValor);
    }

    private void setupCharts() {
        //VisualizarAceX.LlenarGraficaAceX(this, acelerometroX, people, dato, sennales);
        VisualizadorGrafica.llenarGrafica(this,acelerometroX,people,dato,sennales, new AceXStrategy());
        VisualizadorGrafica.llenarGrafica(this,acelerometroY,people,dato,sennales, new AceYStrategy());
        VisualizadorGrafica.llenarGrafica(this,acelerometroZ,people,dato,sennales, new AceZStrategy());

        VisualizadorGrafica.llenarGrafica(this,giroscopioX,people,dato,sennales, new GyroXStrategy());
        VisualizadorGrafica.llenarGrafica(this,giroscopioY,people,dato,sennales, new GyroYStrategy());
        VisualizadorGrafica.llenarGrafica(this,giroscopioZ,people,dato,sennales, new GyroZStrategy());

        VisualizadorGrafica.llenarGrafica(this,magnetometroX,people,dato,sennales, new MagXStrategy());
        VisualizadorGrafica.llenarGrafica(this,magnetometroY,people,dato,sennales, new MagYStrategy());
        VisualizadorGrafica.llenarGrafica(this,magnetometroZ,people,dato,sennales, new MagZStrategy());
        //VisualizarAceY.LlenarGraficaAceY(this, acelerometroY, people, dato, sennales);
        //VisualizarAceZ.LlenarGraficaAceZ(this, acelerometroZ, people, dato, sennales);
        TemperaturaPromedio.LlenarTemp(this, temperatura, people, dato, sennales);
        Predecir(GraficaSennalesActivity.this);
    }

    private void setupBackButton() {
        getOnBackPressedDispatcher().addCallback(GraficaSennalesActivity.this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Maneja la acción personalizada aquí
                // Por ejemplo, muestra un mensaje de confirmación
                if (shouldAllowBackPress()) {
                    // Permitir retroceso
                    String classname = "com.example.sennales.Visualizar_TablaActivity";
                    Intent intent = new Intent(GraficaSennalesActivity.this, Retroceso.getClass(classname));
                    Bundle enviardatos = new Bundle();
                    enviardatos.putSerializable("persona", people);
                    enviardatos.putSerializable("datos", dato);
                    enviardatos.putSerializable("clase", GraficaSennalesActivity.class);
                    intent.putExtras(enviardatos);
                    startActivity(intent);
                    LimpiarCache.cleanCache(GraficaSennalesActivity.this);
                    finish();  // O usa super.onBackPressed() si es necesario
                } else {
                    // Mostrar un mensaje o hacer otra acción
                    Toast.makeText(GraficaSennalesActivity.this, "No puedes volver atrás ahora", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton atras = findViewById(R.id.atras_visualizar);
        atras.setOnClickListener(view -> {
            String classname = "com.example.sennales.Visualizar_TablaActivity";
            Intent intent = new Intent(GraficaSennalesActivity.this, Retroceso.getClass(classname));
            Bundle enviardatos = new Bundle();
            enviardatos.putSerializable("persona", people);
            enviardatos.putSerializable("datos", dato);
            enviardatos.putSerializable("clase", GraficaSennalesActivity.class);
            intent.putExtras(enviardatos);
            startActivity(intent);
            LimpiarCache.cleanCache(GraficaSennalesActivity.this);
            finish();
        });
    }

    // Método que decide si permitir o no el retroceso
    private boolean shouldAllowBackPress() {
        // Lógica para decidir si se permite la navegación hacia atrás
        return true;
    }

    private void creargraficas(){
        acelerometroX = agregarGrafica("Acelerómetro X", acelerometro);
        acelerometroY = agregarGrafica("Acelerómetro Y", acelerometro);
        acelerometroZ = agregarGrafica("Acelerómetro Z", acelerometro);
        giroscopioX = agregarGrafica("Giroscopio X", giroscopio);
        giroscopioY = agregarGrafica("Giroscopio Y", giroscopio);
        giroscopioZ = agregarGrafica("Giroscopio Z", giroscopio);
        magnetometroX = agregarGrafica("Magnetómetro X", magnetometro);
        magnetometroY = agregarGrafica("Magnetómetro Y", magnetometro);
        magnetometroZ = agregarGrafica("Magnetómetro Z", magnetometro);
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

    private void Predecir(Context context){
        apiService = RetrofitClient.getApiService(context);
        repository = new PredecirRepository(apiService);
        ModeloRequest request =
                construirRequest(context, people, dato);
        Log.e("Probar", request.toString());
        repository.predecir(request, new PredictionCallback() {
            @Override
            public void onSuccess(Prediccion resultado) {
                if(resultado != null){
                    diagnostico.setText(resultado.getEnfermedad1() + resultado.getProb1()
                    + ","+ resultado.getEnfermedad2() + resultado.getProb2()  + ","+ resultado.getEnfermedad3() + resultado.getProb3());
                }
                // Avanzar al siguiente dato (secuencial)

            }

            @Override
            public void onError(String error) {
                Log.e("PRED_ERROR", "Error en predicción: " + error);
                // En error también avanzar al siguiente dato

            }
        });
    }

    private static ModeloRequest construirRequest(
            Context context,
            People people,
            DataPeople dataPeople) {

        Bd_manager_Sennales managerSennales =
                new Bd_manager_Sennales(context);
        List<Sennales> sennalesList = managerSennales.sennales_list(people.getCi(), dataPeople);

        if (sennalesList == null || sennalesList.isEmpty()) {
            return null; // indicar que no hay señales para construir el request
        }

        Sennales s = sennalesList.get(0);

        Bd_manager_patologias managerEnfermedad =
                new Bd_manager_patologias(context);

        Sennal sennal = new Sennal();

        sennal.setAccX(convertToDouble(
                managerSennales.SennalesMismaFechaAceX(people.getCi(), dataPeople,s)));

        sennal.setAccY(convertToDouble(
                managerSennales.SennalesMismaFechaAceY(
                        people.getCi(), dataPeople,s)));

        sennal.setAccZ(convertToDouble(
                managerSennales.SennalesMismaFechaAceZ(
                        people.getCi(), dataPeople, s)));

        sennal.setGyroX(convertToDouble(managerSennales.SennalesMismaFechaGirosX(people.getCi(), dataPeople,s)));
        sennal.setGyroY(convertToDouble(managerSennales.SennalesMismaFechaGirosY(people.getCi(), dataPeople,s)));
        sennal.setGyroZ(convertToDouble(managerSennales.SennalesMismaFechaGirosZ(people.getCi(), dataPeople,s)));

        sennal.setMagX(convertToDouble(managerSennales.SennalesMismaFechaMagneX(people.getCi(), dataPeople,s)));
        sennal.setMagY(convertToDouble(managerSennales.SennalesMismaFechaMagneY(people.getCi(), dataPeople,s)));
        sennal.setMagZ(convertToDouble(managerSennales.SennalesMismaFechaMagneZ(people.getCi(), dataPeople,s)));

        List<Sennal> listaSennales = new ArrayList<>();
        listaSennales.add(sennal);

        List<String> enfermedades =
                managerEnfermedad.allpatologia();

        return new ModeloRequest(listaSennales, enfermedades);
    }

    private static List<Double> convertToDouble(ArrayList<Float> sennalesMismaFechaAceY) {
        List<Double> doubleList = new ArrayList<>();
        if (sennalesMismaFechaAceY == null){
            return doubleList;
        }
        for(Float f : sennalesMismaFechaAceY){
            doubleList.add(f.doubleValue());
        }
        return doubleList;
    }

}