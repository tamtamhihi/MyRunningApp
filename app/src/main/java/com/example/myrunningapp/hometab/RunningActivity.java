package com.example.myrunningapp.hometab;

import com.example.myrunningapp.utils.MyDate;

public class RunningActivity {
    public double distance, time;
    public String name, title, location;
    public MyDate startDate;

    RunningActivity(double distance, double time, String name, String title, String location, MyDate startDate){
        this.distance = distance;
        this.time = time;
        this.name = name;
        this.title = title;
        this.location = location;
        this.startDate = startDate;
    }
}
