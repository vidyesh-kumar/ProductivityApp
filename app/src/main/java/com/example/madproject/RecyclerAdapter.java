package com.example.madproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private final ArrayList<Category> categories;
    private final Context mcontext;
    private final RecycleListener listener;

    public RecyclerAdapter(ArrayList<Category> recyclerDataArrayList, Context mcontext,RecycleListener listener) {
        this.categories = recyclerDataArrayList;
        this.mcontext = mcontext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        Category recyclerData = categories.get(position);
        holder.category_title.setText(recyclerData.getTitle());
        holder.category_image.setImageResource(recyclerData.getImgid());
        holder.tasks.setText("Pending: "+recyclerData.getNoofTasks());
        holder.container.setOnClickListener(view -> listener.onItemClicked(categories.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return categories.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final TextView category_title;
        private final TextView tasks;
        private final ImageView category_image;

        private final CardView container;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            category_title = itemView.findViewById(R.id.category_name);
            category_image = itemView.findViewById(R.id.category_image);
            tasks = itemView.findViewById(R.id.number);
            container = itemView.findViewById(R.id.container_card);
        }
    }
}
