package com.zimmer.taskmaster;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class Menu extends AppCompatActivity {

    private EditText taskInput;
    private Button addTaskButton;
    private ListView taskListView;
    private Button setReminderButton;

    private ArrayList<String> tasks;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;

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

        // Configuración de SharedPreferences
        sharedPreferences = getSharedPreferences("TaskPreferences", MODE_PRIVATE);

        // Cargar tareas desde SharedPreferences
        loadTasks();

        // Configuración de la lista de tareas
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);
        taskListView.setAdapter(adapter);

        // Listener para agregar tareas
        addTaskButton.setOnClickListener(v -> addTask());

        // Listener para eliminar tareas con un clic largo
        taskListView.setOnItemLongClickListener((parent, view, position, id) -> {
            Log.d("Menu", "Item long clicked: " + position);
            tasks.remove(position);
            adapter.notifyDataSetChanged();
            saveTasks(); // Guardar las tareas en SharedPreferences después de eliminar
            return true; // Indica que el clic largo se ha manejado
        });

        // Listener para establecer recordatorios
        setReminderButton.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, ActivityAlarmReceiver.class);
            startActivity(intent);
        });
    }

    private void addTask() {
        String task = taskInput.getText().toString();
        if (!task.isEmpty()) {
            tasks.add(task);
            adapter.notifyDataSetChanged();
            saveTasks(); // Guardar las tareas en SharedPreferences
            taskInput.setText("");
        }
    }

    private void saveTasks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("tasks", new HashSet<>(tasks));
        editor.apply();
    }

    private void loadTasks() {
        Set<String> taskSet = sharedPreferences.getStringSet("tasks", new HashSet<>());
        tasks = new ArrayList<>(taskSet);
    }
}
