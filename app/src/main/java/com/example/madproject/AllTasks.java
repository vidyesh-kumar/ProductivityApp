package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
//import androidx.recyclerview.widget.GridLayoutManager;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
//import android.view.View;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
//import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Comparator;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class AllTasks extends AppCompatActivity implements TaskRecycleListener{
    AppCompatButton move;
    PreferenceManager preferenceManager;
    private RecyclerView courseRV;
    private TaskAdapter recyclerViewHolder;
    TaskRecycleListener listener;
    RecyclerView view;
    Category catSelected;
    ImageView profiledp;
    ArrayList<Category> categories;
    ArrayList<Tasks> allTasks;
    TextView startDate,endDate;
    EditText name,desc;
    ImageView home,pomo;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
        getGUI();
        move.setOnClickListener(view -> setActivity(AddTask.class));
        home.setOnClickListener(view -> setActivity(Home.class));
        pomo.setOnClickListener(view -> setActivity(Pomodoro.class));
        profiledp.setOnClickListener(view -> setActivity(Profile.class));
        // on below line we are creating a method to create item touch helper
        // method for adding swipe to delete functionality.
        // in this we are specifying drag direction and position to right
        courseRV=findViewById(R.id.alltaskRecycler);
        startDate = findViewById(R.id.task_date);
        endDate=findViewById(R.id.task_date_end);
        name = findViewById(R.id.task_name);
        desc = findViewById(R.id.task_note);
       recyclerViewHolder = new TaskAdapter(allTasks,this,listener);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP| ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if(direction==ItemTouchHelper.LEFT)
                {
                    setActivityWithContent(EditTask.class,position);
                }
               // this method is called when we swipe our item to right direction.
                else
                {   Tasks deletedCourse = allTasks.get(viewHolder.getAdapterPosition());

                    // below line is to get the position
                    // of the item at that position.


                    // this method is called when item is swiped.
                    // below line is to remove item from our array list.

                    allTasks.remove(viewHolder.getAdapterPosition());
                    allTasks.sort(Comparator.comparing(Tasks::getEndDate));
                    for (Category c : categories) {
                        if (deletedCourse.getCatname().equals(c.getTitle())) {
                            ArrayList<Tasks> catTasks = new ArrayList<Tasks>(c.getTasks());
                            for (Tasks T : catTasks) {
                                if (T.getName().equals(deletedCourse.getName())) {
                                    c.RemoveTasks(T);
                                }
                            }
                        }
                    }
                    Gson gson = new Gson();
                    String catJson = gson.toJson(categories);
                    preferenceManager.setString("Categories", catJson);
                    String taskJson = gson.toJson(allTasks);
                    preferenceManager.setString("Tasks", taskJson);
                    // below line is to notify our item is removed from adapter.
                    recyclerViewHolder.notifyItemRemoved(viewHolder.getAdapterPosition());

                    AlarmManager ealrm1 = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent iStart = new Intent(getApplicationContext(), TaskStartReciever.class);
                    PendingIntent piStart = PendingIntent.getBroadcast(getApplicationContext(), deletedCourse.getTimerId(), iStart, PendingIntent.FLAG_IMMUTABLE);
                    ealrm1.cancel(piStart);

                    AlarmManager ealrm2 = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent iEnd = new Intent(getApplicationContext(), TaskEndReciever.class);
                    PendingIntent piEnd = PendingIntent.getBroadcast(getApplicationContext(), deletedCourse.getTimerId(), iEnd, PendingIntent.FLAG_IMMUTABLE);
                    ealrm2.cancel(piEnd);


                    // below line is to display our snackbar with action.
                    Snackbar.make(courseRV, deletedCourse.getName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            allTasks.add(position, deletedCourse);
                            for (Category c : categories) {
                                if (deletedCourse.getCatname().equals(c.getTitle())) {
                                    c.AddTasks(deletedCourse);
                                }
                            }
                            Gson gson = new Gson();
                            String catJson = gson.toJson(categories);
                            preferenceManager.setString("Categories", catJson);
                            String taskJson = gson.toJson(allTasks);
                            preferenceManager.setString("Tasks", taskJson);
                            // below line is to notify item is
                            // added to our adapter class.
                        }

                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.


                    }).show();
                    new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long l) {
                        }

                        @Override
                        public void onFinish() {
                                recreate();
                        }
                    }.start();
                }

                }

            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,float dX, float dY,int actionState, boolean isCurrentlyActive){

                Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.inter_semi);
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(AllTasks.this, R.color.back))
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(AllTasks.this, R.color.back))
                        .setSwipeRightLabelTextSize(TypedValue.COMPLEX_UNIT_SP,22)
                        .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP,22)
                        .addSwipeLeftActionIcon(R.drawable.pencil_icon)
                        .addSwipeRightActionIcon(R.drawable.trash_icon)
                        .setSwipeLeftLabelColor(Color.WHITE)
                        .setSwipeRightLabelColor(Color.WHITE)
                        .setSwipeLeftLabelTypeface(typeface)
                        .setSwipeRightLabelTypeface(typeface)
                        .addSwipeRightLabel("  Delete")
                        .addSwipeLeftLabel("Edit   ")
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            // at last we are adding this
                // to our recycler view.

        }).attachToRecyclerView(courseRV);
    }

    private void setActivityWithContent(Class ctx,int position) {
        Intent i = new Intent(getApplicationContext(),ctx);
        Bundle b = new Bundle();
        b.putInt("TaskPosition",position);
        i.putExtras(b);
        startActivity(i);
        finish();
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
        String catJson=preferenceManager.getString("Categories");
        byte[] b = Base64.decode(preferenceManager.getString("image_data"), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        profiledp.setImageBitmap(bitmap);

        Gson gson = new Gson();
        allTasks = gson.fromJson(taskJson,new TypeToken<ArrayList<Tasks>>(){}.getType());
        categories = gson.fromJson(catJson,new TypeToken<ArrayList<Category>>(){}.getType());
        TaskAdapter adapter=new TaskAdapter(allTasks,this,this);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        view.setLayoutManager(layoutManager);
        ItemOffsetDecorator itemDecoration = new ItemOffsetDecorator(getApplicationContext(),R.dimen.gap);
        view.addItemDecoration(itemDecoration);
        view.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(Tasks c) {
        int position = allTasks.indexOf(c);
        setActivityWithContent(ViewTask.class,position);
        finish();
    }

    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),Home.class);
        startActivity(i);
        finish();
    }
}