package com.example.myrunningapp.metab.challengestab;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrunningapp.R;
import com.example.myrunningapp.hometab.RunningActivity;

import java.util.ArrayList;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ViewHolder> {
    ArrayList<Challenge> myChallenges;
    ArrayList<RunningActivity> activities;
    Context context;

    ChallengesAdapter(ArrayList<Challenge> challenges, Context context, ArrayList<RunningActivity> activities) {
        this.myChallenges = challenges;
        this.context = context;
        this.activities = activities;
    }

    @NonNull
    @Override
    public ChallengesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.challenge_layout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ChallengesAdapter.ViewHolder holder, int position) {
        Challenge currentChallenge = myChallenges.get(position);
        holder.challengeTitle.setText(currentChallenge.challengeName);
        String distance;
        if (currentChallenge.challengeType == Challenge.RUNNING_DISTANCE_CHALLENGE) {
            holder.challengeImage.setImageResource(R.drawable.distance_challenge);
            holder.challengeType.setText("Distance challenge");
            distance = Double.toString(currentChallenge.completed) + " / " + Integer.toString(currentChallenge.total) + " km";
        } else {
            holder.challengeImage.setImageResource(R.drawable.step_challenge);
            holder.challengeType.setText("Steps challenge");
            distance = Integer.toString((int)currentChallenge.completed) + " / " + Integer.toString(currentChallenge.total) + " steps";
        }
        holder.distance.setText(distance);

        double completed = 0;

        for (RunningActivity runningActivity : activities) {
            int activityDay = runningActivity.startDate.daysLeftFromNow();
            int currentStartDay = currentChallenge.startDate.daysLeftFromNow();
            int currentEndDay = currentChallenge.endDate.daysLeftFromNow();
            if (currentEndDay <= activityDay && activityDay <= currentStartDay) {
                completed += runningActivity.distance;
            }
        }

        if (currentChallenge.challengeType == Challenge.RUNNING_STEP_CHALLENGE)
            completed = Math.floor(completed * 10 / 8);

        Double percentage = Math.min(100, completed * 100 / currentChallenge.total);
        holder.completenessBar.setProgress((int) Math.ceil(percentage));
        String percentageString = String.format("%.1f", percentage) + " %";
        holder.percentage.setText(percentageString);

        int startDiff = currentChallenge.startDate.daysLeftFromNow() - 1;

        if (startDiff == 0) {
            holder.startDate.setText("Starts today");
        } else if (startDiff == 1){
            holder.startDate.setText("Started yesterday");
        } else {
            holder.startDate.setText("Started " + startDiff + " days ago");
        }

        int deadlineDiff = currentChallenge.endDate.daysLeftFromNow();

        if (deadlineDiff < 0) {
            holder.deadline.setText("Challenge ended");
        } else {
            String deadlineText = "Ends in " + Integer.toString(deadlineDiff) + " day";
            if (deadlineDiff > 1)
                deadlineText += "s";
            holder.deadline.setText(deadlineText);
        }
    }

    @Override
    public int getItemCount() {
        return myChallenges.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView challengeImage;
        public TextView challengeTitle, challengeType, distance, percentage, startDate, deadline;
        public ProgressBar completenessBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            challengeImage = itemView.findViewById(R.id.challenge_img);
            challengeTitle = itemView.findViewById(R.id.challenge_title);
            challengeType = itemView.findViewById(R.id.challenge_type);
            distance = itemView.findViewById(R.id.distance);
            percentage = itemView.findViewById(R.id.percentage);
            startDate = itemView.findViewById(R.id.startdate);
            deadline = itemView.findViewById(R.id.deadline);
            completenessBar = itemView.findViewById(R.id.completeness_bar);
        }
    }
}
