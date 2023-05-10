package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ViewTask extends AppCompatActivity {
    ImageView home,pomo,tasks;
    TextView taskname,taskdesc,startdate,enddate;
    AutoCompleteTextView dropdown;

    int taskPos;

    Tasks tasktoDisplay;

    ArrayList <Tasks> allTasks;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        getGUI();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActivity(Home.class);
            }
        });
        pomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActivity(Pomodoro.class);
            }
        });
        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActivity(AllTasks.class);
            }
        });

    }

    private void setActivity(Class ctx)
    {   Intent i = new Intent(getApplicationContext(),ctx);
        startActivity(i);
        finish();
    }

    private void getGUI() {
        home = findViewById(R.id.view_home_button);
        pomo = findViewById(R.id.view_pomo_button);
        tasks = findViewById(R.id.view_add_task);
        taskname = findViewById(R.id.view_name);
        taskdesc = findViewById(R.id.viewnote);
        startdate = findViewById(R.id.view_date);
        enddate = findViewById(R.id.view_date_end);
        dropdown = findViewById(R.id.view_category_select);

        preferenceManager = PreferenceManager.getInstance(this);
        String taskJson=preferenceManager.getString("Tasks");
        Gson gson = new Gson();
        allTasks = gson.fromJson(taskJson,new TypeToken<ArrayList<Tasks>>(){}.getType());

        Bundle b = getIntent().getExtras();
        taskPos = b.getInt("TaskPosition");
        tasktoDisplay = allTasks.get(taskPos);
        taskname.setText(tasktoDisplay.getName());
        dropdown.setText(tasktoDisplay.getCatname());
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        String startDateText = sf.format(tasktoDisplay.getStartDate());
        String endDateText = sf.format(tasktoDisplay.getEndDate());
        startdate.setText(startDateText);
        enddate.setText(endDateText);
        taskdesc.setText(tasktoDisplay.getDesc());
    }
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),AllTasks.class);
        startActivity(i);
        finish();
    }

}