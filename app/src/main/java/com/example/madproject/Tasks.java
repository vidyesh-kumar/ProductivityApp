package com.example.madproject;

import java.util.Date;

public class Tasks {
    Date startDate;

    int timerId;
    Date endDate;
    boolean completed;
    String name;
    String Desc;
    int Caticon;
    String Catname;
    Tasks(Date start,Date end,String name,String Desc,String Cat,int Caticon,int timerId)
    {   this.startDate=start;
        this.endDate=end;
        this.name = name;
        this.Desc= Desc;
        this.completed=false;
        this.Catname=Cat;
        this.Caticon = Caticon;
        this.timerId = timerId;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getDesc() {
        return Desc;
    }

    public int getCaticon() {
        return Caticon;
    }

    public String getCatname() {
        return Catname;
    }

    public String getName() {
        return name;
    }

    public void setCaticon(int caticon) {
        Caticon = caticon;
    }

    public void setCatname(String catname) {
        Catname = catname;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getTimerId() {
        return timerId;
    }
}
