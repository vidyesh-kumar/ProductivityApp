package com.example.madproject;

import java.util.Date;

public class Tasks {
    Date startDate;
    Date endDate;
    boolean completed;
    String name;
    String Desc;
    String Cat;
    Tasks(Date start,Date end,String name,String Desc,String Cat)
    {   this.startDate=start;
        this.endDate=end;
        this.name = name;
        this.Desc= Desc;
        this.completed=false;
        this.Cat=Cat;
    }
    public Date getEndDate(){
        return this.endDate;
    }
    public Date getStartDate(){
        return this.startDate;
    }
    public String[] getTask()
    {   return new String[]{this.name, this.Desc, this.Cat};
    }
    public boolean getCompleted(){
        return this.completed;
    }
}
