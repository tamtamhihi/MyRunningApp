package com.example.myrunningapp.metab.challengestab;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrunningapp.R;

import java.util.ArrayList;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ViewHolder> {
    ArrayList<Challenge> myChallenges;
    Context context;

    ChallengesAdapter(ArrayList<Challenge> challenges, Context context) {
        this.myChallenges = challenges;
        this.context = context;
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
        Double percentage = currentChallenge.completed * 100 / currentChallenge.total;
        holder.completenessBar.setProgress((int) Math.ceil(percentage));
        String percentageString = String.format("%.1f", percentage) + " %";
        holder.percentage.setText(percentageString);
        int deadlineDiff = currentChallenge.endDate.daysUntilNow();

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
        public TextView challengeTitle, challengeType, distance, percentage, deadline;
        public ProgressBar completenessBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            challengeImage = itemView.findViewById(R.id.challenge_img);
            challengeTitle = itemView.findViewById(R.id.challenge_title);
            challengeType = itemView.findViewById(R.id.challenge_type);
            distance = itemView.findViewById(R.id.distance);
            percentage = itemView.findViewById(R.id.percentage);
            deadline = itemView.findViewById(R.id.deadline);
            completenessBar = itemView.findViewById(R.id.completeness_bar);
        }
    }
}
