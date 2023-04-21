package com.example.madproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Pomodoro extends AppCompatActivity {
    PreferenceManager preferenceManager;
    ImageView profiledp,home,task;

    Long alarmtime;

    AlarmManager alrm;

    AppCompatButton m30,m60,m120,m180,custom;

    TextView timerView,hintView;

    Boolean isTimerActive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);
        getUI();
        profiledp.setOnClickListener(view -> setActivity(Profile.class));
        home.setOnClickListener(view -> setActivity(Home.class));
        task.setOnClickListener(view->setActivity(AllTasks.class));
        timerListeners();

    }

    private void timerListeners() {
        m30.setOnClickListener(view -> {
            if(!isTimerActive) {
                Intent i = new Intent(getApplicationContext(),TimerReciever.class);
                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 100,i,PendingIntent.FLAG_IMMUTABLE);
                alarmtime = 60*30*1000+System.currentTimeMillis();
                alrm.setExact(AlarmManager.RTC_WAKEUP,1000,pi);
                preferenceManager.setString("AlarmTime",Long.toString(alarmtime));
                showTimerStatus();
                isTimerActive = true;
            }
            else
                Toast.makeText(getApplicationContext(),"A Timer Is Already Active",Toast.LENGTH_SHORT).show();
        });
        m60.setOnClickListener(view -> {
            if(!isTimerActive) {
                Intent i = new Intent(getApplicationContext(),TimerReciever.class);
                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 100,i,PendingIntent.FLAG_IMMUTABLE);
                alarmtime = 60*65*1000+System.currentTimeMillis();
                alrm.setExact(AlarmManager.RTC_WAKEUP,alarmtime,pi);
                preferenceManager.setString("AlarmTime",Long.toString(alarmtime));
                showTimerStatus();
                isTimerActive = true;
            }
            else
                Toast.makeText(getApplicationContext(),"A Timer Is Already Active",Toast.LENGTH_SHORT).show();
        });
        m120.setOnClickListener(view -> {
            if(!isTimerActive) {
                Intent i = new Intent(getApplicationContext(),TimerReciever.class);
                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 100,i,PendingIntent.FLAG_IMMUTABLE);
                alarmtime = 60*130*1000+System.currentTimeMillis();
                alrm.setExact(AlarmManager.RTC_WAKEUP,alarmtime,pi);
                preferenceManager.setString("AlarmTime",Long.toString(alarmtime));
                showTimerStatus();
                isTimerActive = true;
            }
            else
                Toast.makeText(getApplicationContext(),"A Timer Is Already Active",Toast.LENGTH_SHORT).show();
        });
        m180.setOnClickListener(view -> {
            if(!isTimerActive) {
                Intent i = new Intent(getApplicationContext(),TimerReciever.class);
                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 100,i,PendingIntent.FLAG_IMMUTABLE);
                alarmtime = 60*195*1000+System.currentTimeMillis();
                alrm.setExact(AlarmManager.RTC_WAKEUP,alarmtime,pi);
                preferenceManager.setString("AlarmTime",Long.toString(alarmtime));
                showTimerStatus();
                isTimerActive = true;
            }
            else
                Toast.makeText(getApplicationContext(),"A Timer Is Already Active",Toast.LENGTH_SHORT).show();
        });

    }

    private void setActivity(Class ctx) {
        Intent i = new Intent(getApplicationContext(),ctx);
        startActivity(i);
    }

    private void getUI() {
        preferenceManager = PreferenceManager.getInstance(this);
        profiledp = findViewById(R.id.profile_link);
        home = findViewById(R.id.hom_button);
        m30 = findViewById(R.id.m30);
        m60 = findViewById(R.id.m60);
        m120 = findViewById(R.id.m120);
        m180 = findViewById(R.id.m180);
        custom = findViewById(R.id.custom);
        timerView = findViewById(R.id.timer_text);
        hintView = findViewById(R.id.hint_timer_view);
        task = findViewById(R.id.tas_button);
        byte[] b = Base64.decode(preferenceManager.getString("image_data"), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        profiledp.setImageBitmap(bitmap);
        alrm = (AlarmManager)getSystemService(ALARM_SERVICE);
        createNotificationChannel();
        showTimerStatus();
    }

    private void showTimerStatus() {
        String alarm=preferenceManager.getString("AlarmTime");
        if(!alarm.equals(""))
            alarmtime = Long.parseLong(alarm);
        else
            alarmtime = Long.parseLong("0");
        if(alarmtime>System.currentTimeMillis())
        {   new CountDownTimer(alarmtime - System.currentTimeMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timerView.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                hintView.setText("Timer Is Active");
            }

            @Override
            public void onFinish() {
                isTimerActive=false;
                timerView.setText("No Active Timers");
                hintView.setText("Please Press A Button To Start Timer");
            }
            }.start();
        }
        else
        {   isTimerActive=false;
            timerView.setText("No Active Timers");
            hintView.setText("Please Press A Button To Start Timer");
        }
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {   String name="Pomodoro";
            String desc="Channel For Timer";
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("TimerNotification",name,importance);
            channel.setDescription(desc);

            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);

        }
    }
}

