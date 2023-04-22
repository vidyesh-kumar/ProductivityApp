package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    TimePickerDialog timePickerDialog;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    AlarmManager alrm1,alrm2;
    String datetime;
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
            {   SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
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

                alrm1 = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent iStart = new Intent(getApplicationContext(), TaskStartReciever.class);
                PendingIntent piStart = PendingIntent.getBroadcast(getApplicationContext(),(int)System.currentTimeMillis(),iStart,PendingIntent.FLAG_IMMUTABLE);
                alrm1.setExact(AlarmManager.RTC_WAKEUP,startdate.getTime(),piStart);

                alrm2 = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent iEnd = new Intent(getApplicationContext(), TaskEndReciever.class);
                PendingIntent piEnd = PendingIntent.getBroadcast(getApplicationContext(), (int)System.currentTimeMillis(),iEnd,PendingIntent.FLAG_IMMUTABLE);
                alrm2.setExact(AlarmManager.RTC_WAKEUP,enddate.getTime(),piEnd);
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
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {   String name="Tasks";
            String desc="Task Updates";
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("TaskNotification",name,importance);
            channel.setDescription(desc);

            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);

        }
    }

    private void setActivity(Class ctx)
    {   Intent i = new Intent(getApplicationContext(),ctx);
        startActivity(i);
        finish();
    }


    public void openDatePicker(View view)
    {   dateSetListener = (datePicker, y, m, d) -> {
            m = m+1;
            datetime = d+"/"+m+"/"+y;
            openTimePicker(view);
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = R.style.MySpinnerDatePickerStyle;

        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
    }

    private void openTimePicker(View view) {
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                datetime += " "+i+":"+i1;
                SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                SimpleDateFormat tv = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                try {
                    Date setDate = sm.parse(datetime);
                    TextView tt = findViewById(R.id.task_date);
                    if(view.getId()==R.id.task_date){
                        if(setDate.after(new Date()))
                            tt.setText(tv.format(setDate));
                        else
                            Toast.makeText(getApplicationContext(),"Set Proper Start Date",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {   Date start = tv.parse(tt.getText().toString());
                        tt = findViewById(R.id.task_date_end);
                        if(setDate.after(start))
                            tt.setText(tv.format(setDate));
                        else
                            Toast.makeText(getApplicationContext(),"Set Proper End Date",Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(),"Start Date Not Set",Toast.LENGTH_SHORT).show();
                }
            }
        };
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        int style = R.style.MySpinnerTimePickerStyle;

        timePickerDialog = new TimePickerDialog(this,style,timeSetListener,hour,minute,false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}