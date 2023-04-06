package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewUser extends AppCompatActivity {
    AppCompatButton signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signIn = (AppCompatButton) findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActivity(Profile.class);
            }
        });
    }
    private void setActivity(Class ctx)
    {   Intent i = new Intent(getApplicationContext(),ctx);
        startActivity(i);

    }
}