package com.zimmer.taskmaster;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ActivityAlarmReceiver extends AppCompatActivity {

    private TimePicker timePicker;
    private EditText etReminderText;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_receiver2);

        timePicker = findViewById(R.id.timePicker);
        etReminderText = findViewById(R.id.etReminderName);
        btnSave = findViewById(R.id.btnSaveReminder);

        btnSave.setOnClickListener(v -> saveReminder());
    }

    private void saveReminder() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String reminderText = etReminderText.getText().toString();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("reminderText", reminderText);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        finish();
    }
}
