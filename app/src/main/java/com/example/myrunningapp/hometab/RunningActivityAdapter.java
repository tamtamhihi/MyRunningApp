package com.example.myrunningapp.hometab;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrunningapp.R;
import com.example.myrunningapp.data.InternalStorage;
import com.example.myrunningapp.data.UserPreference;
import com.example.myrunningapp.utils.MyDate;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RunningActivity currentActivity = myActivity.get(position);

        holder.name.setText(new UserPreference(context).getUserName());

        holder.distance.setText(String.format("%.2f km", currentActivity.distance / 1000));
        /*
        int km = 0;
        if(currentActivity.distance >= 1000)
            km = (int)currentActivity.distance/1000;
        int meter = (int)currentActivity.distance%1000;
        holder.distance.setText(Integer.toString(km) + "." + Integer.toString(meter));*/

        int second = (int) currentActivity.time % 60;
        int minute = (int) currentActivity.time / 60;
        holder.time.setText(Integer.toString(minute) + "m " + Integer.toString(second) + "s");
        double pacer = currentActivity.time / currentActivity.distance;
        int paceMinute = (int) pacer / 60;
        int paceSecond = (int) pacer % 60;
        if (paceMinute > 100)
            holder.pace.setText("00:00/km");
        else
            holder.pace.setText(Integer.toString(paceMinute) + ":" + Integer.toString(paceSecond) + "/km");
        holder.title.setText(currentActivity.title);
        int daysDiff = currentActivity.startDate.daysLeftFromNow() - 1;
        String dateString;
        if (daysDiff == 0) {
            dateString = "Today, ";
        } else if (daysDiff == 1) {
            dateString = "Yesterday, ";
        } else if (daysDiff <= 6) {
            dateString = Integer.toString(daysDiff) + " days ago, ";
        } else {
            dateString = currentActivity.startDate.toString() + ", ";
        }
        holder.locationAndTime.setText(dateString + currentActivity.location);

    }

    @Override
    public int getItemCount() {
        return myActivity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView avatar;
        public TextView name, locationAndTime, title, distance, pace, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            avatar.setImageBitmap(InternalStorage.avatar);
            name = itemView.findViewById(R.id.nameID);
            locationAndTime = itemView.findViewById(R.id.location);
            title = itemView.findViewById(R.id.titleID);
            distance = itemView.findViewById(R.id.distanceID);
            pace = itemView.findViewById(R.id.paceID);
            time = itemView.findViewById(R.id.timeID);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DetailedRunActivity.class);
            intent.putExtra("index", getAdapterPosition());
            context.startActivity(intent);
        }
    }
}
