package com.example.myrunningapp.metab.statisticstab;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myrunningapp.R;
import com.example.myrunningapp.hometab.RunningActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class StatisticsTabFragment extends Fragment {
    ArrayList<RunningActivity> activities;

    private TextView weekRuns, weekTime, weekDistance;
    private TextView yearRuns, yearTime, yearDistance;
    private TextView allRuns, allTime, allDistance;

    public StatisticsTabFragment(ArrayList<RunningActivity> activities) {
        this.activities = activities;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics_tab, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrieveViews(view);
        displayStatistics();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayStatistics() {
        int weekRun = 0, yearRun = 0, allRun = activities.size();
        double weekTimeVal = 0, yearTimeVal = 0, allTimeVal = 0;
        double weekDistVal = 0, yearDistVal = 0, allDistVal = 0;
        for (RunningActivity activity : activities) {
            if (activity.startDate.inCurrentWeek()) {
                weekRun += 1;
                weekTimeVal += activity.time;
                weekDistVal += activity.distance;
            }
            if (activity.startDate.inCurrentYear()) {
                yearRun += 1;
                yearTimeVal += activity.time;
                yearDistVal += activity.distance;
            }
            allTimeVal += activity.time;
            allDistVal += activity.distance;
        }
        setRun(weekRuns, weekRun);
        setRun(yearRuns, yearRun);
        setRun(allRuns, allRun);
        setTime(weekTime, weekTimeVal);
        setTime(yearTime, yearTimeVal);
        setTime(allTime, allTimeVal);
        setDist(weekDistance, weekDistVal);
        setDist(yearDistance, yearDistVal);
        setDist(allDistance, allDistVal);
    }

    private void setRun(TextView view, int runNum) {
        view.setText(Integer.toString(runNum));
    }

    private void setTime(TextView view, double time) {
        int minute = (int) time / 60;
        int second = (int) time % 60;
        view.setText(minute + "m " + second + " s");
    }

    private void setDist(TextView view, double dist) {
        view.setText(String.format("%.1f km", dist / 1000));
    }

    private void retrieveViews(View view) {
        weekRuns = view.findViewById(R.id.week_runs);
        weekTime = view.findViewById(R.id.week_time);
        weekDistance = view.findViewById(R.id.week_dist);
        yearRuns = view.findViewById(R.id.year_runs);
        yearTime = view.findViewById(R.id.year_time);
        yearDistance = view.findViewById(R.id.year_dist);
        allRuns = view.findViewById(R.id.all_runs);
        allTime = view.findViewById(R.id.all_time);
        allDistance = view.findViewById(R.id.all_dist);
    }
}