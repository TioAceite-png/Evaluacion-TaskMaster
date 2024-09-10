package com.zimmer.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    public static int TiempoCarga = 3000;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Menu.class);
                startActivity(intent);
                finish();
            }
        }, TiempoCarga);

        // ProgressBar

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100); // Establece el valor máximo del ProgressBar

        // Crear un Runnable para actualizar el progreso
        final Runnable updateProgressRunnable = new Runnable() {
            @Override
            public void run() {
                if (progressStatus < 100) {
                    progressStatus++;
                    progressBar.setProgress(progressStatus);
                    handler.postDelayed(this, TiempoCarga / 100); // Actualiza el progreso en intervalos regulares
                } else {
                    // Cuando el progreso llega al 100%, inicia la nueva actividad
                    startActivity(new Intent(MainActivity.this, Menu.class));
                    finish();
                }
            }
        };
        // Inicia la actualización del progreso
        handler.post(updateProgressRunnable);
    }
}




