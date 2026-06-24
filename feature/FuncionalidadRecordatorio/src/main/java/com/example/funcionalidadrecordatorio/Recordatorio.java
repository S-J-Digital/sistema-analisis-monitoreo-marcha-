package com.example.funcionalidadrecordatorio;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.Calendar;
import java.util.TimeZone;

public class Recordatorio {

    private static final int REQUEST_CODE = 1;


    public static void showDatePickerDialog(Context context) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year1, month1, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year1, month1, dayOfMonth);
            if (selectedDate.before(c)) {
                // Muestra un mensaje de error si la fecha es menor a la actual
                Toast.makeText(context, "La fecha seleccionada es anterior a la actual. Por favor, elige una fecha válida.", Toast.LENGTH_SHORT).show();
            } else {
                // Llama al método addEventToCalendar si la fecha es válida
                addEventToCalendar(selectedDate, context);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    /*private static void addEventToCalendar(Calendar date, Context context) {
        try{
            ContentValues event = new ContentValues();
            event.put(CalendarContract.Events.CALENDAR_ID, 1);
            event.put(CalendarContract.Events.TITLE, "Nuevas pruebas");
            event.put(CalendarContract.Events.DESCRIPTION, "Realizar nuevas pruebas de desempeño físico");
            event.put(CalendarContract.Events.EVENT_LOCATION, "Ubicación");
            event.put(CalendarContract.Events.DTSTART, date.getTimeInMillis());
            event.put(CalendarContract.Events.DTEND, date.getTimeInMillis() + 60 * 60 * 1000); // 1 hora de duración
            event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
            event.put(CalendarContract.Events.HAS_ALARM, 1);

            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_CALENDAR}, REQUEST_CODE);
            }
            Uri eventUri = context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, event);
            if (eventUri != null) {
                long eventID = Long.parseLong(eventUri.getLastPathSegment());
                ContentValues reminders = new ContentValues();
                reminders.put(CalendarContract.Reminders.EVENT_ID, eventID);
                reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                reminders.put(CalendarContract.Reminders.MINUTES, 10);
                context.getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminders);
            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }*/

    public static void addEventToCalendar(Calendar date, Context context) {
        try{
            Long startTime = date.getTimeInMillis();
            Long endTime = startTime + (60 * 60 * 1000);

            Intent intent = new Intent(Intent.ACTION_INSERT).setData(CalendarContract.Events.CONTENT_URI)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,startTime)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
                    .putExtra(CalendarContract.Events.TITLE, "Realizar nuevas pruebas")
                    .putExtra(CalendarContract.Events.DESCRIPTION, "Realizar nuevas pruebas de desempeño físico")
                    .putExtra(CalendarContract.Events.EVENT_LOCATION,"Ubicación")
                    .putExtra(CalendarContract.Events.AVAILABILITY,CalendarContract.Events.AVAILABILITY_BUSY);

            if(intent.resolveActivity(context.getPackageManager()) != null){
                context.startActivity(intent);
            }else{
                Toast.makeText(context,"No se encontró una aplicación de calendario", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }
}
