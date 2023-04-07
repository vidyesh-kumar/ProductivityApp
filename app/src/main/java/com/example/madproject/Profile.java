package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class Profile extends AppCompatActivity {
    PreferenceManager preferenceManager;
    ImageView dp;
    TextView user,useremail;
    AppCompatButton edit;
    Switch notifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getUI();
    }

    private void getUI() {
        preferenceManager=PreferenceManager.getInstance(this);
        dp = findViewById(R.id.dp);
        user = findViewById(R.id.username);
        useremail = findViewById(R.id.useremail);
        edit =findViewById(R.id.edit);
        notifi = findViewById(R.id.notify);
        String previouslyEncodedImage = preferenceManager.getString("image_data");
        if( !previouslyEncodedImage.equalsIgnoreCase("") ){
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            dp.setImageBitmap(bitmap);
        }
        else
        {   Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar);
            dp.setImageBitmap(icon);
        }
        user.setText(preferenceManager.getString("Username"));
        useremail.setText(preferenceManager.getString("UserEmail"));
    }
}