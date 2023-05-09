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
import android.widget.Button;
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

public class EditTask extends AppCompatActivity {
    AutoCompleteTextView edropdown;

    AppCompatButton esubmit,egoback;
    EditText ename,edesc;
    ArrayList<String> evalues;
    ArrayList<Category> ecategories;
    ArrayList<Tasks> eallTasks;
    ArrayAdapter<String> edropdownValues;
    PreferenceManager epreferenceManager;
    Category ecatSelected;
    TextView estartDate,eendDate;
    DatePickerDialog edatePickerDialog;
    DatePickerDialog.OnDateSetListener edateSetListener;

    TimePickerDialog etimePickerDialog;
    TimePickerDialog.OnTimeSetListener etimeSetListener;
    AlarmManager ealrm1,ealrm2;
    String edatetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        getGUI();
        edropdown.setOnItemClickListener((adapterView, view, i, l) -> {
            String selected = adapterView.getItemAtPosition(i).toString();
            ecatSelected = ecategories.get(i);





        });
        egoback.setOnClickListener(view -> setActivity(AllTasks.class));
        esubmit.setOnClickListener(view -> {
            String taskname = ename.getText().toString();
            String taskdesc = edesc.getText().toString();
            String startdatestring = estartDate.getText().toString();
            String enddatestring = eendDate.getText().toString();
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
                Tasks newTask = new Tasks(startdate, enddate, taskname, taskdesc, ecatSelected.getTitle(), ecatSelected.getImgid());
                eallTasks.add(newTask);
                eallTasks.sort(Comparator.comparing(Tasks::getEndDate));
                Gson gson = new Gson();
                String taskJson = gson.toJson(eallTasks);
                epreferenceManager.setString("Tasks",taskJson);
                for(Category c:ecategories)
                {   if(ecatSelected.getTitle().equals(c.getTitle()))
                    c.AddTasks(newTask);
                }
                String catJson = gson.toJson(ecategories);
                epreferenceManager.setString("Categories",catJson);

                ealrm1 = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent iStart = new Intent(getApplicationContext(), TaskStartReciever.class);
                PendingIntent piStart = PendingIntent.getBroadcast(getApplicationContext(),(int)System.currentTimeMillis(),iStart,PendingIntent.FLAG_IMMUTABLE);
                ealrm1.setExact(AlarmManager.RTC_WAKEUP,startdate.getTime(),piStart);

                ealrm2 = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent iEnd = new Intent(getApplicationContext(), TaskEndReciever.class);
                PendingIntent piEnd = PendingIntent.getBroadcast(getApplicationContext(), (int)System.currentTimeMillis(),iEnd,PendingIntent.FLAG_IMMUTABLE);
                ealrm2.setExact(AlarmManager.RTC_WAKEUP,enddate.getTime(),piEnd);
                setActivity(AllTasks.class);

            }
        });
    }

    private void getGUI() {
        edropdown = findViewById(R.id.edit_category_select);
        estartDate = findViewById(R.id.edit_date);
        eendDate=findViewById(R.id.edit_date_end);
        ename = findViewById(R.id.edit_name);
        edesc = findViewById(R.id.edit_note);
        esubmit = findViewById(R.id.edit_task_button);
        egoback = findViewById(R.id.cancel_go_back);

        epreferenceManager = PreferenceManager.getInstance(this);
        Gson gson = new Gson();
        String catJson = epreferenceManager.getString("Categories");
        String taskJson = epreferenceManager.getString("Tasks");
        eallTasks = gson.fromJson(taskJson,new TypeToken<ArrayList<Tasks>>(){}.getType());
        ecategories = gson.fromJson(catJson,new TypeToken<ArrayList<Category>>(){}.getType());
        evalues = new ArrayList<>();
        for(Category c:ecategories)
        {   evalues.add(c.getTitle());
        }
        edropdownValues = new ArrayAdapter<>(this, R.layout.category_list, evalues);
        edropdown.setAdapter(edropdownValues);
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
    {   edateSetListener = (datePicker, y, m, d) -> {
        m = m+1;
        edatetime = d+"/"+m+"/"+y;
        openTimePicker(view);
    };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = R.style.MySpinnerDatePickerStyle;

        edatePickerDialog = new DatePickerDialog(this,style,edateSetListener,year,month,day);
        edatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        edatePickerDialog.setTitle("Select Date");
        edatePickerDialog.show();
    }

    private void openTimePicker(View view) {
        etimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                edatetime += " "+i+":"+i1;
                SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                SimpleDateFormat tv = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                try {
                    Date setDate = sm.parse(edatetime);
                    TextView tt = findViewById(R.id.edit_date);
                    if(view.getId()==R.id.edit_date){
                        if(setDate.after(new Date()))
                            tt.setText(tv.format(setDate));
                        else
                            Toast.makeText(getApplicationContext(),"Set Proper Start Date",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {   Date start = tv.parse(tt.getText().toString());
                        tt = findViewById(R.id.edit_date_end);
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

        etimePickerDialog = new TimePickerDialog(this,style,etimeSetListener,hour,minute,false);
        etimePickerDialog.setTitle("Select Time");
        etimePickerDialog.show();
    }
}