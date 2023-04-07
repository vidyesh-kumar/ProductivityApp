package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewUser extends AppCompatActivity {
    PreferenceManager preferenceManager;
    AppCompatButton signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenceManager = PreferenceManager.getInstance(this);
        if(!preferenceManager.getString("Username").isEmpty()) {
            setActivity(Home.class);
            finish();
        }
        else
        {   signIn = findViewById(R.id.signIn);
            signIn.setOnClickListener(view -> setActivity(CreateUser.class));
        }

    }
    private void setActivity(Class ctx)
    {   Intent i = new Intent(getApplicationContext(),ctx);
        startActivity(i);
    }
}