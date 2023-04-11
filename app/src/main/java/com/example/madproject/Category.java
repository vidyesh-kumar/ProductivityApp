package com.example.madproject;
import android.graphics.drawable.Drawable;

public class Category {
    String categoryName;
    int categoryIconId;
    int numberOfTasks;
    Tasks tasks[] = null;
    Category(String name, int iconId, int tasks)
    {   this.categoryName=name;
        this.categoryIconId=iconId;
        this.numberOfTasks=tasks;
    }
    public String getName(){
        return this.categoryName;
    }

    public int getNumberOfTasks(){
        return this.numberOfTasks;
    }

    public Tasks[] getTasks() {
        return this.tasks;
    }
}
