package com.zimmer.taskmaster;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


public class Menu extends AppCompatActivity {

    private EditText taskInput;
    private Button addTaskButton;
    private ListView taskListView;
    private Button setReminderButton;

    private ArrayList<String> tasks;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        // Ajuste de márgenes para las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de vistas
        taskInput = findViewById(R.id.taskInput);
        addTaskButton = findViewById(R.id.btn2);
        taskListView = findViewById(R.id.ListView);
        setReminderButton = findViewById(R.id.btn3);

        // Configuración de la lista de tareas
        tasks = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);
        taskListView.setAdapter(adapter);

        // Listener para agregar tareas
        addTaskButton.setOnClickListener(v -> {
            String task = taskInput.getText().toString();
            if (!task.isEmpty()) {
                tasks.add(task);
                adapter.notifyDataSetChanged();
                taskInput.setText("");
            }
        });

        // Listener para establecer recordatorios
        setReminderButton.setOnClickListener(v -> setReminder());
    }


    private void setReminder() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12); // Establece la hora del recordatorio
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Crear un intent para abrir la aplicación cuando se toque la notificación
            Intent notificationIntent = new Intent(context, Menu.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Crear un canal de notificación para versiones de Android Oreo y posteriores
            createNotificationChannel(context);

            // Crear la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "REMINDER_CHANNEL")
                    .setSmallIcon(R.drawable.notification_icon) // Asegúrate de usar el nombre correcto del recurso
                    .setContentTitle("Recordatorio")
                    .setContentText("Es hora de revisar tus tareas!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            // Mostrar la notificación
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, builder.build());
        }

        private void createNotificationChannel(Context context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Reminder Channel";
                String description = "Canal para recordatorios";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("REMINDER_CHANNEL", name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }

    }
}
