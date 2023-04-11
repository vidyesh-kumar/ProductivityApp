package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    ImageView profile,pomo;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getUI();
        profile.setOnClickListener(view -> setActivity(Profile.class));
        pomo.setOnClickListener(view -> setActivity(Pomodoro.class));
    }

    private void getUI()
    {   preferenceManager = PreferenceManager.getInstance(this);
        profile = findViewById(R.id.profile_id);
        pomo = findViewById(R.id.pomodoro_button);

        Gson gson = new Gson();
        String catJson = preferenceManager.getString("Categories");
        ArrayList <Category> categories = gson.fromJson(catJson,new TypeToken<ArrayList<Category>>(){}.getType());

        byte[] b = Base64.decode(preferenceManager.getString("image_data"), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        profile.setImageBitmap(bitmap);

    }
    protected void setActivity(Class ctx)
    {   Intent i = new Intent(getApplicationContext(),ctx);
        startActivity(i);
    }
}