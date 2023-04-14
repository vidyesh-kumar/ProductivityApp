package com.example.madproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.RecyclerViewHolder> {

    private final ArrayList<Tasks> tasks;
    private final Context mcontext;
    private final TaskRecycleListener listener;

    public TaskAdapter(ArrayList<Tasks> recyclerDataArrayList, Context mcontext,TaskRecycleListener listener) {
        this.tasks = recyclerDataArrayList;
        this.mcontext = mcontext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder,int position) {
        // Set the data to textview and imageview.
        Tasks recyclerData = tasks.get(position);
        holder.task_title.setText(recyclerData.getName());
        holder.category_image.setImageResource(recyclerData.getCaticon());
        String cat = recyclerData.getCatname();
        switch(cat){
            case "Work":
            case "Study":
                                    holder.image_container.setCardBackgroundColor(Color.rgb(4,138,129));
                                    break;
            case "Urgent":          holder.image_container.setCardBackgroundColor(Color.rgb(163,0,0));
                                    break;
            case "Hobbies":         holder.image_container.setCardBackgroundColor(Color.rgb(255,200,87));
                                    break;
            case "Entertainment":   holder.image_container.setCardBackgroundColor(Color.rgb(0,175,181));
                                    break;
            case "Projects":        holder.image_container.setCardBackgroundColor(Color.rgb(143,45,86));
                                    break;
            case "Fitness":         holder.image_container.setCardBackgroundColor(Color.rgb(255,119,0));
                                    break;
        }
        Date today = new Date();
        Date endDate = recyclerData.getEndDate();
        long diffInMillies = Math.abs(endDate.getTime() - today.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if(diff>30){
            SimpleDateFormat sg = new SimpleDateFormat("dd/MM/yyyy");
            holder.date.setText(sg.format(endDate));
        }
        else if(diff<30&&diff>2)
        {   holder.date.setText(diff+" Days");
        }
        else if(diff<2&&diff>0)
            holder.date.setText("Today");
        else
            holder.date.setText("Soon");
        holder.container.setOnClickListener(view -> listener.onItemClicked(tasks.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return tasks.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView task_title,date;
        private ImageView category_image;

        private CardView container,image_container;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            task_title = itemView.findViewById(R.id.task_name_card);
            date = itemView.findViewById(R.id.deadline_date);
            category_image = itemView.findViewById(R.id.task_category_image);
            image_container = itemView.findViewById(R.id.task_image_container);
            container = itemView.findViewById(R.id.task_container_card);
        }
    }
}