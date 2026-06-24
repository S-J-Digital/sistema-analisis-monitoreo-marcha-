package com.example.funcionalidadrecordatorio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.provider.CalendarContract;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowPackageManager;

import java.util.ArrayList;
import java.util.Calendar;

@RunWith(RobolectricTestRunner.class)
public class RecordatorioTest {

    @Test
    public void testAddEventToCalendar_CrearIntentCorrecto(){
        Context context = ApplicationProvider.getApplicationContext();

        // 1. Configurar el "falso" calendario para que resolveActivity no sea null
        ShadowPackageManager shadowPackageManager = Shadows.shadowOf(context.getPackageManager());

        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.packageName = "com.google.android.calendar";
        activityInfo.name = "CalendarActivity";
        activityInfo.applicationInfo = new ApplicationInfo();
        activityInfo.applicationInfo.packageName = activityInfo.packageName;

        ResolveInfo resolveInfo = new ResolveInfo();
        resolveInfo.activityInfo = activityInfo;

        // El filtro debe coincidir con la acción y los datos del Intent real
        Intent intentFiltro = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI);

        shadowPackageManager.addResolveInfoForIntent(intentFiltro, resolveInfo);

        // 2. Ejecutar
        Calendar fecha = Calendar.getInstance();
        fecha.set(2026, Calendar.APRIL, 1, 12, 0);

        Recordatorio.addEventToCalendar(fecha, context);

        // 3. Capturar y Validar
        ShadowApplication shadowApp = Shadows.shadowOf((Application) context);
        Intent intentDisparado = shadowApp.getNextStartedActivity();

        assertNotNull("El Intent no se capturó. Revisa el flag NEW_TASK.", intentDisparado);

        // Validar que el flag NEW_TASK esté presente (esto causaba tu error)
        int flags = intentDisparado.getFlags();
        assertEquals("Debe contener el flag FLAG_ACTIVITY_NEW_TASK",
                Intent.FLAG_ACTIVITY_NEW_TASK, flags & Intent.FLAG_ACTIVITY_NEW_TASK);

        // Validar datos básicos
        assertEquals(Intent.ACTION_INSERT, intentDisparado.getAction());
        assertEquals("Realizar nuevas pruebas", intentDisparado.getStringExtra(CalendarContract.Events.TITLE));
    }

}