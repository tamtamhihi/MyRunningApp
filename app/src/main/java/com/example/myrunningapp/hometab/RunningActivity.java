package com.example.myrunningapp.hometab;

import com.example.myrunningapp.utils.MyDate;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RunningActivity {
    public double distance, time;
    public String name, title, location;
    public MyDate startDate;
    public ArrayList<LatLng> routes;

    public RunningActivity(double distance, double time, String name, String title, String location, MyDate startDate){
        this.distance = distance;
        this.time = time;
        this.name = name;
        this.title = title;
        this.location = location;
        this.startDate = startDate;
    }

    public RunningActivity(double distance, double time, String name, String title, String location, MyDate startDate, ArrayList<LatLng> routes){
        this.distance = distance;
        this.time = time;
        this.name = name;
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.routes = routes;
    }


}
