package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class CreateUser extends AppCompatActivity {
    EditText name;
    EditText email;
    TextView header;
    ImageView dp;
    DatePicker dob;
    AppCompatButton submit;
    PreferenceManager preferenceManager;
    public static final int GET_FROM_GALLERY = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        getUI();
        dp.setOnClickListener(view -> startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = name.getText().toString();
                String useremail = email.getText().toString();
                int year = dob.getYear();
                Date d = new Date();
                int curr_year = d.getYear() + 1900;
                int age = curr_year - year;
                Bitmap dpBitmap = ((BitmapDrawable) dp.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                dpBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                preferenceManager.setString("image_data", encodedImage);
                if (username.isEmpty())
                    Toast.makeText(getApplicationContext(), "Enter Username", Toast.LENGTH_LONG).show();
                else if (useremail.isEmpty())
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_LONG).show();
                else if (age <= 0)
                    Toast.makeText(getApplicationContext(), "Enter Date of Birth", Toast.LENGTH_LONG).show();
                else
                {   preferenceManager.setString("Username", username);
                    preferenceManager.setString("UserEmail", useremail);
                    preferenceManager.setString("Age", "" + age);
                    Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_LONG).show();
                    setActivity(Profile.class);
                }
            }
        });

    }

    private void getUI() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        header = findViewById(R.id.header);
        dp = findViewById(R.id.setdp);
        dob = findViewById(R.id.dobpicker);
        submit = findViewById(R.id.submit);
        preferenceManager=PreferenceManager.getInstance(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap dpBitmap;
            try {
                dpBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                dp.setImageBitmap(dpBitmap);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void setActivity(Class ctx)
    {   Intent i = new Intent(getApplicationContext(),ctx);
        startActivity(i);
    }
}