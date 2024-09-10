package com.zimmer.taskmaster;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Crear un intent para abrir la aplicación cuando se toque la notificación
        Intent notificationIntent = new Intent(context, Menu.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Crear la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "REMINDER_CHANNEL")
                .setSmallIcon(R.drawable.notification_icon) // Reemplaza con tu propio icono
                .setContentTitle("Recordatorio")
                .setContentText("Es hora de revisar tus tareas!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Mostrar la notificación
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Crear el canal de notificaciones si es necesario (Android O y superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder Channel";
            String description = "Canal para recordatorios";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("REMINDER_CHANNEL", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        // Mostrar la notificación
        notificationManager.notify(1, builder.build());
    }
}
