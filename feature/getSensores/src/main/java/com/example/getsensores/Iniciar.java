package com.example.getsensores;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

public class Iniciar {
    private static boolean comenzar = false;
    public static void iniciarCuentaRegresiva(TextView countdownText, Vibrator vibrator,MediaPlayer mediaPlayer, int selec_time) {
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                countdownText.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                mediaPlayer.start(); // Reproduce el sonido
                vibrator.vibrate(5000); // Vibra por 5000 milisegundos
                countdownText.setVisibility(View.INVISIBLE);
                comenzar = true;
                new CountDownTimer( selec_time * 1000, 1000){

                    @Override
                    public void onTick(long l) {
                        countdownText.setText(l/1000 + "seg");
                    }

                    @Override
                    public void onFinish() {
                     comenzar = false;
                     mediaPlayer.start(); // Reproduce el sonido
                     vibrator.vibrate(5000); // Vibra por 5000 milisegundos
                    }
                }.start();

            }
        }.start();
    }

    public static boolean isComenzar() {
        return comenzar;
    }

    public static void setComenzar(boolean comenzar) {
        Iniciar.comenzar = comenzar;
    }
}
