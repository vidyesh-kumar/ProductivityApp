package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ChooseCategory extends AppCompatActivity {
    RadioGroup rg;
    RadioButton selected,work,study;
    PreferenceManager preferenceManager;
    CheckBox fit,hobby,entertainment,projects;
    AppCompatButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        getUI();
        colourListeners();
        submit.setOnClickListener(view -> {
            int id = rg.getCheckedRadioButtonId();
            selected = findViewById(id);
            preferenceManager.setString("Occupation",selected.getText().toString()+"ing");
            ArrayList<Category> categories = new ArrayList<Category>();
            if(selected.getText().toString().equals("Work"))
            {   categories.add(new Category("Work",R.drawable.drawable_work_icon,0));
                categories.add(new Category("Urgent",R.drawable.drawable_urgent_icon,0));
            }
            else
            {   categories.add(new Category("Study",R.drawable.drawable_study_icon,0));
                categories.add(new Category("Urgent",R.drawable.drawable_urgent_icon,0));
            }
            if(fit.isChecked())
                categories.add(new Category("Fitness",R.drawable.drawable_fitness_icon,0));
            if(hobby.isChecked())
                categories.add(new Category("Hobbies",R.drawable.drawable_hobby_icon,0));
            if(projects.isChecked())
                categories.add(new Category("Projects",R.drawable.drawable_project_icon,0));
            if(entertainment.isChecked())
                categories.add(new Category("Entertainment",R.drawable.drawable_entertainment_icon,0));
            Gson gson = new Gson();
            String json = gson.toJson(categories);
            preferenceManager.setString("Categories",json);
            setActivity(Home.class);
        });
    }

    private void colourListeners()
    {   work.setOnClickListener(view -> {
        if(work.isChecked()){
            work.setBackgroundResource(R.drawable.selected_occupation);
            study.setBackgroundResource(R.drawable.profile_page_card);
    }
    });
        study.setOnClickListener(view -> {
            if(study.isChecked()){
                study.setBackgroundResource(R.drawable.selected_occupation);
                work.setBackgroundResource(R.drawable.profile_page_card);
        }
    });
        fit.setOnClickListener(view -> {
            if(fit.isChecked())
                fit.setBackgroundResource(R.drawable.selected_categories);
            else
                fit.setBackgroundResource(R.drawable.profile_page_card);
        });
        projects.setOnClickListener(view -> {
            if(projects.isChecked())
                projects.setBackgroundResource(R.drawable.selected_categories);
            else
                projects.setBackgroundResource(R.drawable.profile_page_card);
        });
        hobby.setOnClickListener(view -> {
            if(hobby.isChecked())
                hobby.setBackgroundResource(R.drawable.selected_categories);
            else
                hobby.setBackgroundResource(R.drawable.profile_page_card);
        });
        entertainment.setOnClickListener(view -> {
            if(entertainment.isChecked())
                entertainment.setBackgroundResource(R.drawable.selected_categories);
            else
                entertainment.setBackgroundResource(R.drawable.profile_page_card);
        });
    }

    private void setActivity(Class ctx)
    {   Intent i = new Intent(getApplicationContext(),ctx);
        startActivity(i);
        finish();
    }

    private void getUI() {
        rg=findViewById(R.id.occupation);
        fit=findViewById(R.id.fit);
        hobby=findViewById(R.id.hobby);
        work = findViewById(R.id.work);
        study=findViewById(R.id.study);
        entertainment=findViewById(R.id.enter);
        projects=findViewById(R.id.project);
        submit=findViewById(R.id.selcategory);
        preferenceManager = PreferenceManager.getInstance(this);
    }
}