package com.example.madproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Pomodoro extends AppCompatActivity {
    PreferenceManager preferenceManager;
    ImageView profiledp,home;

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
        timerListeners();

    }

    private void timerListeners() {
        m30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isTimerActive) {
                    CountDownTimer main = new CountDownTimer(30*60*1000,1000) {
                        @Override
                        public void onTick(long l) {
                            NumberFormat f = new DecimalFormat("00");
                            long hour = (l/ 3600000) % 24;
                            long min = (l / 60000) % 60;
                            long sec = (l / 1000) % 60;
                            timerView.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                            hintView.setText("Timer Active");
                        }

                        @Override
                        public void onFinish() {
                            isTimerActive=false;
                            timerView.setText("No Active Timers");
                            hintView.setText("Please Press A Button To Set Timer");
                        }
                    }.start();
                }
                else
                    Toast.makeText(getApplicationContext(),"A Timer Is Already Active",Toast.LENGTH_SHORT).show();
            }
        });
        m60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isTimerActive) {
                    CountDownTimer main = new CountDownTimer(30*60*1000,1000) {
                        @Override
                        public void onTick(long l) {
                            NumberFormat f = new DecimalFormat("00");
                            long hour = (l/ 3600000) % 24;
                            long min = (l / 60000) % 60;
                            long sec = (l / 1000) % 60;
                            timerView.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                            hintView.setText("Timer Active");
                        }

                        @Override
                        public void onFinish() {
                            isTimerActive=false;
                            timerView.setText("No Active Timers");
                            hintView.setText("Please Press A Button To Set Timer");
                        }
                    }.start();
                }
                else
                    Toast.makeText(getApplicationContext(),"A Timer Is Already Active",Toast.LENGTH_SHORT).show();
            }
        });
        m120.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isTimerActive) {
                    CountDownTimer main = new CountDownTimer(30*60*1000,1000) {
                        @Override
                        public void onTick(long l) {
                            NumberFormat f = new DecimalFormat("00");
                            long hour = (l/ 3600000) % 24;
                            long min = (l / 60000) % 60;
                            long sec = (l / 1000) % 60;
                            timerView.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                            hintView.setText("Timer Active");
                        }

                        @Override
                        public void onFinish() {
                            isTimerActive=false;
                            timerView.setText("No Active Timers");
                            hintView.setText("Please Press A Button To Set Timer");
                        }
                    }.start();
                }
                else
                    Toast.makeText(getApplicationContext(),"A Timer Is Already Active",Toast.LENGTH_SHORT).show();
            }
        });
        m180.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isTimerActive) {
                    CountDownTimer main = new CountDownTimer(30*60*1000,1000) {
                        @Override
                        public void onTick(long l) {
                            NumberFormat f = new DecimalFormat("00");
                            long hour = (l/ 3600000) % 24;
                            long min = (l / 60000) % 60;
                            long sec = (l / 1000) % 60;
                            timerView.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                            hintView.setText("Timer Active");
                        }

                        @Override
                        public void onFinish() {
                            isTimerActive=false;
                            timerView.setText("No Active Timers");
                            hintView.setText("Please Press A Button To Set Timer");
                        }
                    }.start();
                }
                else
                    Toast.makeText(getApplicationContext(),"A Timer Is Already Active",Toast.LENGTH_SHORT).show();
            }
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
        if(timerView.getText().toString().equals("No Active Timers"))
                isTimerActive=false;
        else
                isTimerActive=true;
        byte[] b = Base64.decode(preferenceManager.getString("image_data"), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        profiledp.setImageBitmap(bitmap);
    }


}

