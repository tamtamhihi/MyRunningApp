package com.example.myrunningapp.hometab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrunningapp.R;

public class RunningActivityAdapter extends RecyclerView.Adapter<RunningActivityAdapter.ViewHolder> {
    private int[] time;
    private double[] distance;
    private Context context;

    public RunningActivityAdapter(double[] distance, int[] time, Context context) {
        this.distance = distance;
        this.time = time;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.running_activity_items_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.distanceView.setText(distance[position] + " km");

        // Calculate pace
        int pace = (int)((double)time[position] / distance[position]);
        int paceMin = pace / 60;
        int paceSec = pace % 60;
        holder.paceView.setText(paceMin + ":" + paceSec + " /km");

        // Calculate time
        int min = time[position] / 60;
        int sec = time[position] % 60;
        holder.timeView.setText(min + "m " + sec + "s");

        // Set image
        holder.imageView.setImageResource(R.drawable.p1 + position);
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
            distanceView = itemView.findViewById(R.id.distanceID);
            paceView = itemView.findViewById(R.id.paceID);
            timeView = itemView.findViewById(R.id.timeID);
            imageView = itemView.findViewById(R.id.p1ID);
        }
    }
}
