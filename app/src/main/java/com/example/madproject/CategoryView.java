package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CategoryView extends AppCompatActivity implements TaskRecycleListener{
    PreferenceManager preferenceManager;

    ImageView profile,pomo,home,task;
    RecyclerView recyclerView;
    ArrayList<Category> categories;
    TextView headerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);
        getUI();
        profile.setOnClickListener(view -> setActivity(Profile.class));
        pomo.setOnClickListener(view -> setActivity(Pomodoro.class));
        home.setOnClickListener(view -> setActivity(Home.class));
        task.setOnClickListener(view -> setActivity(Home.class));
    }

    private void getUI()
    {   preferenceManager = PreferenceManager.getInstance(this);
        profile = findViewById(R.id.cat_profile);
        pomo = findViewById(R.id.pom_set_button);
        home = findViewById(R.id.go_to_home);
        task = findViewById(R.id.add_task);
        recyclerView=findViewById(R.id.taskRecycler);
        headerText = findViewById(R.id.category_task_header);
        Bundle data = this.getIntent().getExtras();
        String header = data.getString("Header");
        headerText.setText(header+" Tasks");
        Gson gson = new Gson();
        String catJson = preferenceManager.getString("Categories");
        categories = gson.fromJson(catJson,new TypeToken<ArrayList<Category>>(){}.getType());
        ArrayList<Tasks> categorisedTasks = null;
        for(Category c:categories)
        {   if(c.getTitle().equals(header))
                categorisedTasks = c.getTasks();            
        }
        TaskAdapter adapter=new TaskAdapter(categorisedTasks,this,this);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecorator itemDecoration = new ItemOffsetDecorator(getApplicationContext(),R.dimen.gap);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);

        byte[] b = Base64.decode(preferenceManager.getString("image_data"), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        profile.setImageBitmap(bitmap);
    }

    protected void setActivity(Class ctx)
    {   Intent i = new Intent(getApplicationContext(),ctx);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemClicked(Tasks c) {
        
    }
}