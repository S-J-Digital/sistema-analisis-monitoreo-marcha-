package com.example.funcionalidadesconfig;

import static com.example.funcionalidadesconfig.ExportarPDF.CrearPDF;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import com.example.database.Bd_manager_DatosParticipante;
import com.example.database.Bd_manager_Participante;
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
import com.example.user.GetUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ReporteManager {
    private PredecirRepository repository;
    private ApiService apiService;

    public ReporteManager(Context context) {
        apiService = RetrofitClient.getApiService(context);
        this.repository = new PredecirRepository(apiService);
    }

    public  void generarReporte(Context context) {

        Bd_manager_Participante managerP =
                new Bd_manager_Participante(context);

        Bd_manager_DatosParticipante managerD =
                new Bd_manager_DatosParticipante(context);

        String user = GetUser.leerValor(context, "user");

        List<People> listaPersonas = managerP.person_list(user);

        if(listaPersonas == null || listaPersonas.isEmpty()){
            Toast.makeText(context,"No hay participantes", Toast.LENGTH_SHORT).show();
            return;
        }

        PdfDocument pdfDocument = new PdfDocument();
        PDFHelper pdf = new PDFHelper(pdfDocument,
                "Reporte de Señales - Análisis de Marcha");

        procesarPersonas(context, listaPersonas, 0,
                managerD, pdfDocument, pdf);
    }

    private  void procesarPersonas(
            Context context,
            List<People> personas,
            int index,
            Bd_manager_DatosParticipante managerD,
            PdfDocument pdfDocument,
            PDFHelper pdf) {

        if (index >= personas.size()) {
            // Se terminó con todas las personas: finalizar y guardar
            pdf.finish();
            guardarPDF(context, pdfDocument);
            return;
        }

        People people = personas.get(index);
        List<DataPeople> datos = managerD.data_list(people);

        procesarSesiones(context, people, datos, 0,
                () -> procesarPersonas(context, personas,
                        index + 1, managerD, pdfDocument, pdf),
                pdf, pdfDocument);
    }

    private static void guardarPDF(Context context, PdfDocument pdfDocument) {

        String fechaHora = new SimpleDateFormat(
                "yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());

        String nombreArchivo = "SeñalesSinAnalisis_" + fechaHora + ".pdf";

        File directorio = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS),
                "Analisis");

        if (!directorio.exists()) {
            if (!directorio.mkdirs()) {
                Toast.makeText(context,
                        "No se pudo crear la carpeta Analisis",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }

        File archivo = new File(directorio, nombreArchivo);

        try {
            pdfDocument.writeTo(new FileOutputStream(archivo));
            Toast.makeText(context,
                    "PDF guardado en: " + archivo.getAbsolutePath(),
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("PDF_SAVE_ERROR", e.getMessage(), e);
            Toast.makeText(context,
                    "Error al guardar PDF",
                    Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }

    private  void procesarSesiones(
            Context context,
            People people,
            List<DataPeople> datos,
            int index,
            Runnable onFinishPerson,
            PDFHelper pdf, PdfDocument pdfDocument) {

        if (datos == null || index >= datos.size()) {
            onFinishPerson.run();
            return;
        }

        DataPeople dataPeople = datos.get(index);

        ModeloRequest request =
                construirRequest(context, people, dataPeople);

        if (request == null) {
            // No hay señales para esta sesión, continuar con la siguiente
            procesarSesiones(context, people, datos, index + 1, onFinishPerson, pdf, pdfDocument);
            return;
        }


        Log.e("LLamada","Iniciando sesion index=" + index);

        repository.predecir(request, new PredictionCallback() {
            @Override
            public void onSuccess(Prediccion resultado) {
                if(resultado != null){
                    CrearPDF(context,people,dataPeople,resultado,pdf);
                }
                // Avanzar al siguiente dato (secuencial)
                procesarSesiones(context,people,datos,index+1,onFinishPerson,pdf,pdfDocument);
            }

            @Override
            public void onError(String error) {
                Log.e("PRED_ERROR", "Error en predicción: " + error);
                // En error también avanzar al siguiente dato
                procesarSesiones(context,people,datos,index+1,onFinishPerson,pdf,pdfDocument);
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
        if (sennalesMismaFechaAceY == null) return doubleList;
        for(Float f : sennalesMismaFechaAceY){
            doubleList.add(f.doubleValue());
        }
        return doubleList;
    }
}
