package com.example.funcionalidadesconfig;

import static android.media.CamcorderProfile.get;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.util.Log;
import android.widget.Toast;

import com.example.database.Bd_manager_DatosParticipante;
import com.example.database.Bd_manager_Participante;
import com.example.database.Bd_manager_Sennales;
import com.example.enviodatos.Dto.RedNeuronal.Prediccion;
import com.example.funcionalidadesconfig.Acelerometro.AcelXStrategy;
import com.example.funcionalidadesconfig.Acelerometro.AcelYStrategy;
import com.example.funcionalidadesconfig.Acelerometro.AcelZStrategy;
import com.example.funcionalidadesconfig.Giroscopio.GiroXStrategy;
import com.example.funcionalidadesconfig.Giroscopio.GiroYStrategy;
import com.example.funcionalidadesconfig.Giroscopio.GiroZStrategy;
import com.example.funcionalidadesconfig.Magnetometro.MagneXStrategy;
import com.example.funcionalidadesconfig.Magnetometro.MagneYStrategy;
import com.example.funcionalidadesconfig.Magnetometro.MagneZStrategy;
import com.example.model.DataPeople;
import com.example.model.People;
import com.example.model.Sennales;
import com.example.user.GetUser;

import java.util.List;

public class ExportarPDF {
    private static People people;
    private static DataPeople dataPeople;

    private static SignalStrategy signalStrategy;


   public static void setSignalStrategy(SignalStrategy signalStrategy2) {
       signalStrategy = signalStrategy2;
   }

   /* public static void CrearPDF(Context context,

            Prediccion resultado,
            PDFHelper pdf) {

        Bd_manager_Participante managerParticipante = new Bd_manager_Participante(context);
        Bd_manager_DatosParticipante managerDatos = new Bd_manager_DatosParticipante(context);
        Bd_manager_Sennales managerSennales = new Bd_manager_Sennales(context);

        String user = GetUser.leerValor(context, "user");

        List<People> listaPersonas = managerParticipante.person_list(user);

        if (listaPersonas == null || listaPersonas.isEmpty()) {
            Toast.makeText(context,
                    "Necesita registrar al menos 1 paciente",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        PdfDocument pdfDocument = new PdfDocument();
        PDFHelper pdf = new PDFHelper(pdfDocument,
                "Reporte de Señales - Análisis de Marcha");

        for (People people : listaPersonas) {

            List<DataPeople> listaDatos = managerDatos.data_list(people);

            if (listaDatos == null || listaDatos.isEmpty())
                continue;

            for (DataPeople dataPeople : listaDatos) {

                List<Sennales> listaSennales =
                        managerSennales.sennales_list(
                                people.getCi(),
                                dataPeople
                        );

                if (listaSennales == null || listaSennales.isEmpty()){
                    continue;
                }
                try {
                    Sennales sennales = listaSennales.get(0);

                    // ===== DATOS PARTICIPANTE =====

                    pdf.drawSectionTitle("Datos del Paciente");

                     //Columna izquierda
                    pdf.drawTwoColumns(
                            "Nombre: " + people.getName(),
                            "Edad: " + dataPeople.getAge()
                    );

                    pdf.drawTwoColumns(
                            "CI: " + people.getCi(),
                            "Fecha: " + dataPeople.getDate()
                    );

                    pdf.drawTwoColumns(
                            "Sexo: " + people.getSexo(),
                            "Hora: " + dataPeople.getHour()
                    );

                     //Columna derecha (datos clínicos)
                    pdf.drawTwoColumns(
                            "Calzado: " + dataPeople.getShoes(),
                            "Medición cintura-tobillo: " + dataPeople.getCinturaTobillo() + " cm"
                    );

                    pdf.drawTwoColumns(
                            "Largo pierna: " + dataPeople.getLeg() + " cm",
                            "Altura sensor: " + dataPeople.getHeight() + " cm"
                    );

                    String patologia = (dataPeople.getObservation() == null ||
                            dataPeople.getObservation().isEmpty())
                            ? "Ninguna"
                            : dataPeople.getObservation().toString();

                    pdf.drawText("Patología: " + patologia);

                    pdf.drawLine();

                    // ===== ACELERÓMETRO =====
                    pdf.drawSectionTitle("Acelerómetro");

                    drawSignal(pdf, context, people, dataPeople, sennales, new AcelXStrategy());
                    drawSignal(pdf, context, people, dataPeople, sennales, new AcelYStrategy());
                    drawSignal(pdf, context, people, dataPeople, sennales, new AcelZStrategy());

                    // ===== GIROSCOPIO =====
                    pdf.drawSectionTitle("Giroscopio");
                    drawSignal(pdf, context, people, dataPeople, sennales, new GiroXStrategy());
                    drawSignal(pdf, context, people, dataPeople, sennales, new GiroYStrategy());
                    drawSignal(pdf, context, people, dataPeople, sennales, new GiroZStrategy());

                    // ===== MAGNETÓMETRO =====
                    pdf.drawSectionTitle("Magnetómetro");
                    drawSignal(pdf, context, people, dataPeople, sennales, new MagneXStrategy());
                    drawSignal(pdf, context, people, dataPeople, sennales, new MagneYStrategy());
                    drawSignal(pdf, context, people, dataPeople, sennales, new MagneZStrategy());

                    // ===== ANALISIS RED NEURONAL =====
                    // ===== ANALISIS =====
        drawNeuralAnalysisSection(
                pdf,
                resultado.getEnfermedad1(), resultado.getProb1(),
                resultado.getEnfermedad2(), resultado.getProb2(),
                resultado.getEnfermedad3(), resultado.getProb3()
        );

                } catch (Exception e) {
                    Log.e("PDF_ERROR", e.getMessage(), e);
                }


            }
        }

        pdf.finish();

    }*/


    public static void CrearPDF(
            Context context,
            People people,
            DataPeople dataPeople,
            Prediccion resultado,
            PDFHelper pdf) {

        Bd_manager_Sennales managerSennales = new Bd_manager_Sennales(context);

        List<Sennales> listaSennales =
                managerSennales.sennales_list(
                        people.getCi(),
                        dataPeople
                );

       /*if (listaSennales == null || listaSennales.isEmpty()){
            continue;
        }*/
        Sennales sennales = listaSennales.get(0);

        // ===== DATOS PARTICIPANTE =====

        pdf.drawSectionTitle("Datos del Paciente");

        //Columna izquierda
        pdf.drawTwoColumns(
                "Nombre: " + people.getName(),
                "Edad: " + dataPeople.getAge()
        );

        pdf.drawTwoColumns(
                "CI: " + people.getCi(),
                "Fecha: " + dataPeople.getDate()
        );

        pdf.drawTwoColumns(
                "Sexo: " + people.getSexo(),
                "Hora: " + dataPeople.getHour()
        );

        //Columna derecha (datos clínicos)
        pdf.drawTwoColumns(
                "Calzado: " + dataPeople.getShoes(),
                "Medición cintura-tobillo: " + dataPeople.getCinturaTobillo() + " cm"
        );

        pdf.drawTwoColumns(
                "Largo pierna: " + dataPeople.getLeg() + " cm",
                "Altura sensor: " + dataPeople.getHeight() + " cm"
        );

        String patologia = (dataPeople.getObservation() == null ||
                dataPeople.getObservation().isEmpty())
                ? "Ninguna"
                : dataPeople.getObservation().toString();

        pdf.drawText("Patología: " + patologia);

        pdf.drawLine();

        // ===== SEÑALES =====
        pdf.drawSectionTitle("Acelerómetro");
        // ===== ACELERÓMETRO =====
        pdf.drawSectionTitle("Acelerómetro");

        drawSignal(pdf, context, people, dataPeople, sennales, new AcelXStrategy());
        drawSignal(pdf, context, people, dataPeople, sennales, new AcelYStrategy());
        drawSignal(pdf, context, people, dataPeople, sennales, new AcelZStrategy());

        // ===== GIROSCOPIO =====
        pdf.drawSectionTitle("Giroscopio");
        drawSignal(pdf, context, people, dataPeople, sennales, new GiroXStrategy());
        drawSignal(pdf, context, people, dataPeople, sennales, new GiroYStrategy());
        drawSignal(pdf, context, people, dataPeople, sennales, new GiroZStrategy());

        // ===== MAGNETÓMETRO =====
        pdf.drawSectionTitle("Magnetómetro");
        drawSignal(pdf, context, people, dataPeople, sennales, new MagneXStrategy());
        drawSignal(pdf, context, people, dataPeople, sennales, new MagneYStrategy());
        drawSignal(pdf, context, people, dataPeople, sennales, new MagneZStrategy());

        // ===== ANALISIS =====
        drawNeuralAnalysisSection(
                pdf,
                resultado.getEnfermedad1(), resultado.getProb1(),
                resultado.getEnfermedad2(), resultado.getProb2(),
                resultado.getEnfermedad3(), resultado.getProb3()
        );
    }

    private static void drawSignal(PDFHelper pdf,
                                   Context context,
                                   People people,
                                   DataPeople dataPeople,
                                   Sennales sennales,
                                   SignalStrategy strategy) {

        setSignalStrategy(strategy);

        Bitmap chart = signalStrategy.generateSignalChart(
                context, people, dataPeople, sennales);

        pdf.drawBitmap(chart);
    }


    /*private static void guardarPDF(Context context, PdfDocument pdfDocument) {

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
    }*/

    private static void drawNeuralAnalysisSection(PDFHelper pdf,
                                                  String e1, double p1,
                                                  String e2, double p2,
                                                  String e3, double p3) {

        pdf.drawSectionTitle("Análisis de la Red Neuronal");

        // 🔹 Datos simulados (luego vendrán de la API)
        pdf.drawSectionTitle("Análisis de la Red Neuronal");

        String[][] resultados = {
                {e1, p1 + "%", calcularNivelRiesgo(p1)},
                {e2, p2 + "%", calcularNivelRiesgo(p2)},
                {e3, p3 + "%", calcularNivelRiesgo(p3)}
        };

        String[] headers = {
                "Enfermedad",
                "Probabilidad",
                "Nivel de Riesgo"
        };

        pdf.drawTable(headers, resultados);

        pdf.drawText("Diagnóstico más probable: Parkinson (72%)");

        pdf.drawLine();
    }

    private static String calcularNivelRiesgo(double p3) {
        String riesgo = null;

        if(p3 >= 70){
            riesgo = "Alto";

        }else if(p3 <70 && p3 >= 50){
            riesgo = "Medio";
        }else{
            riesgo = "Bajo";
        }

        return riesgo;
    }


}
