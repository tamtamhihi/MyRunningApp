package com.example.myrunningapp.hometab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrunningapp.R;
import com.example.myrunningapp.utils.MyDate;

import java.util.ArrayList;

public class RunningActivityAdapter extends RecyclerView.Adapter<RunningActivityAdapter.ViewHolder> {
    ArrayList<RunningActivity> myActivity;
    Context context;

    RunningActivityAdapter(ArrayList<RunningActivity> myActivity, Context context) {
        this.myActivity = myActivity;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.running_activity_items_layout, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RunningActivity currentActivity = myActivity.get(position);
        holder.name.setText(currentActivity.name);
        holder.distance.setText(Double.toString(currentActivity.distance) + " km");
        int second = (int) currentActivity.time % 60;
        int minute = (int) currentActivity.time / 60;
        holder.time.setText(Integer.toString(minute) + "m " + Integer.toString(second) + "s");
        double pacer = currentActivity.time / currentActivity.distance;
        int paceMinute = (int) pacer / 60;
        int paceSecond = (int) pacer % 60;
        holder.pace.setText(Integer.toString(paceMinute) +":" + Integer.toString(paceSecond) + "/km");
        holder.title.setText(currentActivity.title);
        holder.locationAndTime.setText(currentActivity.startDate.toString() + ", " + currentActivity.location);
    }

    @Override
    public int getItemCount() {
        return myActivity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, locationAndTime, title, distance, pace, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameID);
            locationAndTime = itemView.findViewById(R.id.location);
            title = itemView.findViewById(R.id.titleID);
            distance = itemView.findViewById(R.id.distanceID);
            pace = itemView.findViewById(R.id.paceID);
            time = itemView.findViewById(R.id.timeID);
        }
    }
}
