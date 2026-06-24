package com.example.enviodatos.impl;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.enviodatos.service.ApiService;
import com.example.enviodatos.service.EnvioStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObtenerPDF implements EnvioStrategy {
    private ApiService apiService;
    private String username;

    public ObtenerPDF(ApiService apiService, String username) {
        this.apiService = apiService;
        this.username = username;
    }

    @Override
    public void enviarInsert(Context context) {
        Call<Map<String, String>> call = apiService.descargarPdf(username);
        call.enqueue(new Callback<Map<String, String>>() {

            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
              /* try{
                   if (response.isSuccessful() && response.body() != null) {
                       boolean guardado = guardarPdf(response.body(), context);

                       if (guardado) {
                           Toast.makeText(context, "PDF guardado correctamente", Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(context, "Error al guardar el PDF", Toast.LENGTH_SHORT).show();
                       }
                   } else {
                       Toast.makeText(context, "Error al recibir el PDF", Toast.LENGTH_SHORT).show();
                   }
               }catch (Exception e){
                   Log.e("Recibir PDF", e.getMessage());
               }*/

                if (response.isSuccessful() && response.body() != null) {
                    String base64Pdf = response.body().get("pdfBase64");
                    if (guardarPdfBase64(base64Pdf, context)) {
                        Toast.makeText(context, "PDF guardado correctamente", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Error al guardar el PDF", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Respuesta inválida del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e("PDF_ERROR", "Fallo: " + t.getMessage());
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void enviarUpdate(Object object, String name, Long id, Context context) {

    }

    @Override
    public void enviarDelete(Long id) {

    }

    @Override
    public void obtenerIdbynameUpdate(String nombre, Object object, Context context, String operacion) {

    }

    @Override
    public void obtenerIdbynameDelete(String nombre) {

    }

    private static boolean guardarPdf(ResponseBody body, Context context) {
        try {
            // Ruta pública de Descargas
            File directorioDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            // Carpeta "Analisis" dentro de Descargas
            File carpetaAnalisis = new File(directorioDescargas, "Analisis");

            if (!carpetaAnalisis.exists()) {
                boolean creada = carpetaAnalisis.mkdirs();
                if (!creada) {
                    Log.e("PDF_ERROR", "No se pudo crear la carpeta Analisis");
                    return false;
                }
            }

            // Nombre con fecha
            String fechaHora = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                fechaHora = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
            }
            String nombreArchivo = "Analisis_" + fechaHora + ".pdf";

            File archivoPdf = new File(carpetaAnalisis, nombreArchivo);

            // Guardar archivo
            InputStream inputStream = body.byteStream();
            FileOutputStream outputStream = new FileOutputStream(archivoPdf);

            byte[] buffer = new byte[4096];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            Log.i("PDF", "PDF guardado en: " + archivoPdf.getAbsolutePath());
            return true;

        } catch (Exception e) {
            Log.e("PDF_ERROR", "Error al guardar PDF: " + e.getMessage());
            return false;
        }
    }

    private static boolean guardarPdfBase64(String base64Pdf, Context context) {
        try {
            // Decodificar Base64 a bytes
            byte[] pdfBytes = android.util.Base64.decode(base64Pdf, android.util.Base64.DEFAULT);

            // Ruta pública de Descargas
            File directorioDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            // Carpeta "Analisis" dentro de Descargas
            File carpetaAnalisis = new File(directorioDescargas, "Analisis");

            if (!carpetaAnalisis.exists()) {
                boolean creada = carpetaAnalisis.mkdirs();
                if (!creada) {
                    Log.e("PDF_ERROR", "No se pudo crear la carpeta Analisis");
                    return false;
                }
            }

            // Nombre con fecha y hora para evitar sobreescribir
            String fechaHora = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                fechaHora = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
            }
            String nombreArchivo = "Analisis_" + fechaHora + ".pdf";

            File archivoPdf = new File(carpetaAnalisis, nombreArchivo);

            // Guardar archivo
            FileOutputStream outputStream = new FileOutputStream(archivoPdf);
            outputStream.write(pdfBytes);
            outputStream.flush();
            outputStream.close();

            Log.i("PDF", "PDF guardado en: " + archivoPdf.getAbsolutePath());
            return true;

        } catch (Exception e) {
            Log.e("PDF_ERROR", "Error al guardar PDF: " + e.getMessage());
            return false;
        }
    }

}
