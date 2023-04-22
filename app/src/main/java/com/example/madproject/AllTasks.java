package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class AllTasks extends AppCompatActivity implements TaskRecycleListener{
    AppCompatButton move;
    PreferenceManager preferenceManager;

    RecyclerView view;

    ImageView profiledp;

    ArrayList<Tasks> allTasks;
    ImageView home,pomo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
        getGUI();
        move.setOnClickListener(view -> setActivity(AddTask.class));
        home.setOnClickListener(view -> setActivity(Home.class));
        pomo.setOnClickListener(view -> setActivity(Pomodoro.class));
    }

    private void setActivity(Class ctx) {
        Intent i = new Intent(getApplicationContext(),ctx);
        startActivity(i);
        finish();
    }


    private void getGUI() {
        move = findViewById(R.id.move_to_add);
        home = findViewById(R.id.task_home_button);
        pomo = findViewById(R.id.pomo_set_button);
        view = findViewById(R.id.alltaskRecycler);
        preferenceManager = PreferenceManager.getInstance(this);
        profiledp= findViewById(R.id.task_profile);
        String taskJson=preferenceManager.getString("Tasks");

        byte[] b = Base64.decode(preferenceManager.getString("image_data"), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        profiledp.setImageBitmap(bitmap);

        Gson gson = new Gson();
        allTasks = gson.fromJson(taskJson,new TypeToken<ArrayList<Tasks>>(){}.getType());
        TaskAdapter adapter=new TaskAdapter(allTasks,this,this);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        view.setLayoutManager(layoutManager);
        ItemOffsetDecorator itemDecoration = new ItemOffsetDecorator(getApplicationContext(),R.dimen.gap);
        view.addItemDecoration(itemDecoration);
        view.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(Tasks c) {
        Toast.makeText(getApplicationContext(),c.getName(),Toast.LENGTH_SHORT).show();
    }
}