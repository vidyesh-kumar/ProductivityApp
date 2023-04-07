package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditUser extends AppCompatActivity {
    PreferenceManager preferenceManager;

    public static final int GET_FROM_GALLERY = 3;
    ImageView editdp;
    TextView email,dob;
    EditText editname;
    AppCompatButton editsubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        getUI();
        editdp.setOnClickListener(view -> startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY));
        editsubmit.setOnClickListener(view -> {
            String val = editname.getText().toString();
            preferenceManager.setString("Username",val);
            Bitmap dpBitmap = ((BitmapDrawable) editdp.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            dpBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            preferenceManager.setString("image_data", encodedImage);
            Toast.makeText(getApplicationContext(),"Profile Updated",Toast.LENGTH_LONG).show();
            setActivity(Profile.class);
        });
    }

    private void setActivity(Class ctx) {
        Intent i = new Intent(getApplicationContext(),ctx);
        startActivity(i);
    }

    private void getUI() {
        editdp = findViewById(R.id.editdp);
        email = findViewById(R.id.emailtext);
        dob = findViewById(R.id.dobtext);
        editname = findViewById(R.id.changename);
        editsubmit = findViewById(R.id.editprofile);
        preferenceManager = PreferenceManager.getInstance(this);
        email.setText(preferenceManager.getString("UserEmail"));
        dob.setText(preferenceManager.getString("Age"));
        editname.setText(preferenceManager.getString("Username"));
        byte[] b = Base64.decode(preferenceManager.getString("image_data"), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        editdp.setImageBitmap(bitmap);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap dpBitmap;
            try {
                dpBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                editdp.setImageBitmap(dpBitmap);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}