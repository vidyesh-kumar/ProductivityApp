package com.example.madproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class AddTask extends AppCompatActivity {
    AutoCompleteTextView dropdown;
    AppCompatButton submit,goback;
    EditText name,desc;
    ArrayList<String> values;
    ArrayList<Category> categories;
    ArrayList<Tasks> allTasks;
    ArrayAdapter<String> dropdownValues;
    PreferenceManager preferenceManager;
    Category catSelected;
    TextView startDate,endDate;
    DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        getGUI();
        dropdown.setOnItemClickListener((adapterView, view, i, l) -> {
            String selected = adapterView.getItemAtPosition(i).toString();
            catSelected = categories.get(i);
        });
        goback.setOnClickListener(view -> setActivity(AllTasks.class));
        submit.setOnClickListener(view -> {
            String taskname = name.getText().toString();
            String taskdesc = desc.getText().toString();
            String startdatestring = startDate.getText().toString();
            String enddatestring = endDate.getText().toString();
            if(taskdesc.equals(""))
                taskdesc="No Description Added";
            if(taskname.equals(""))
                Toast.makeText(getApplicationContext(),"Enter Task Header",Toast.LENGTH_SHORT).show();
            else if(startdatestring.equals(""))
                Toast.makeText(getApplicationContext(),"Enter Start Date",Toast.LENGTH_SHORT).show();
            else if(enddatestring.equals(""))
                Toast.makeText(getApplicationContext(),"Enter End Date",Toast.LENGTH_SHORT).show();
            else
            {   SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                Date startdate = null;
                try {
                    startdate = sf.parse(startdatestring);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Date enddate = null;
                try {
                    enddate = sf.parse(enddatestring);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Tasks newTask = new Tasks(startdate, enddate, taskname, taskdesc, catSelected.getTitle(), catSelected.getImgid());
                allTasks.add(newTask);
                allTasks.sort(Comparator.comparing(Tasks::getEndDate));
                Gson gson = new Gson();
                String taskJson = gson.toJson(allTasks);
                preferenceManager.setString("Tasks",taskJson);
                for(Category c:categories)
                {   if(catSelected.getTitle().equals(c.getTitle()))
                        c.AddTasks(newTask);
                }
                String catJson = gson.toJson(categories);
                preferenceManager.setString("Categories",catJson);
                setActivity(AllTasks.class);

            }
        });
    }

    private void getGUI() {
        dropdown = findViewById(R.id.category_select);
        startDate = findViewById(R.id.task_date);
        endDate=findViewById(R.id.task_date_end);
        name = findViewById(R.id.task_name);
        desc = findViewById(R.id.task_note);
        submit = findViewById(R.id.add_task_button);
        goback = findViewById(R.id.go_back);

        preferenceManager = PreferenceManager.getInstance(this);
        Gson gson = new Gson();
        String catJson = preferenceManager.getString("Categories");
        String taskJson = preferenceManager.getString("Tasks");
        allTasks = gson.fromJson(taskJson,new TypeToken<ArrayList<Tasks>>(){}.getType());
        categories = gson.fromJson(catJson,new TypeToken<ArrayList<Category>>(){}.getType());
        values = new ArrayList<>();
        for(Category c:categories)
        {   values.add(c.getTitle());
        }
        dropdownValues = new ArrayAdapter<>(this, R.layout.category_list, values);
        dropdown.setAdapter(dropdownValues);
    }

    private void setActivity(Class ctx)
    {   Intent i = new Intent(getApplicationContext(),ctx);
        startActivity(i);
        finish();
    }


    public void openDatePicker(View view)
    {   dateSetListener = (datePicker, y, m, d) -> {
            m = m+1;
            String date = d+"/"+m+"/"+y;
            int id = view.getId();
            TextView text = findViewById(id);
            text.setText(date);
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = DatePickerDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();

    }
}