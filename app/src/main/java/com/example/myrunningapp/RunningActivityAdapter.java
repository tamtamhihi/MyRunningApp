package com.example.myrunningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RunningActivityAdapter extends RecyclerView.Adapter<RunningActivityAdapter.ViewHolder> {
    private int[]  time;
    private double[] distance, pace;
    private Context context;
    public RunningActivityAdapter(double[] distance, double[] pace, int[] time, Context context){
        this.distance=distance;
        this.pace = pace;
        this.time = time;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.running_activity_items_layout, parent, false);
        return new ViewHolder((view));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.distanceView.setText(Double.toString(distance[position]) + " km");
        holder.paceView.setText(Double.toString(pace[position]) + " /km");
        int min = time[position]/60;
        int sec = time[position] % 60;
        holder.timeView.setText(Integer.toString(min) + "m " + Integer.toString(sec) + "s");
        int resID = context.getResources().getIdentifier("p" + Integer.toString(position+1) + ".jpg",
                "drawable", context.getPackageName());
        holder.imageView.setImageResource(resID);
    }

    @Override
    public int getItemCount() {
        return time.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView distanceView, paceView, timeView;
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            distanceView= itemView.findViewById(R.id.distanceID);
            paceView = itemView.findViewById(R.id.paceID);
            timeView = itemView.findViewById(R.id.timeID);
            imageView = itemView.findViewById(R.id.p1ID);
        }
    }
}
