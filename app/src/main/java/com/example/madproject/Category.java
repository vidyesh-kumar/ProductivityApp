package com.example.madproject;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Category {
    String categoryName;
    int categoryIconId;
    ArrayList<Tasks> Tasks = new ArrayList<>();
    public String getTitle() {
        return categoryName;
    }

    public void setTitle(String title) {
        this.categoryName = title;
    }

    public int getImgid() {
        return categoryIconId;
    }

    public void setImgid(int imgid) {
        this.categoryIconId= imgid;
    }

    public Category(String title, int imgid) {
        this.categoryName = title;
        this.categoryIconId = imgid;
    }

    public ArrayList<Tasks> getTasks(){
        return this.Tasks;
    }
    public void AddTasks(Tasks c) {
        Tasks.add(c);
        Tasks.sort(Comparator.comparing(com.example.madproject.Tasks::getEndDate));
    }
    public int getNoofTasks(){
        return Tasks.size();
    }
}
