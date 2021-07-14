package com.example.myrunningapp.data;

import android.content.Context;
import android.util.Log;

import com.example.myrunningapp.hometab.RunningActivity;
import com.example.myrunningapp.metab.challengestab.Challenge;
import com.example.myrunningapp.utils.MyDate;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class InternalStorage {
    private String fileDir;
    private static File file, challengeFile, activitiesFile;
    private static FileWriter challengeFileWriter, activitiesFileWriter;
    public static ArrayList<RunningActivity> myActivities;
    public static ArrayList<Challenge> allChallenges;

    public InternalStorage(Context context) throws IOException {
        file = context.getFilesDir();
        fileDir = file.getAbsolutePath();
        challengeFile = new File(file, "challenge");
        activitiesFile = new File(file, "activity");
        if (challengeFile.exists()) {
            challengeFileWriter = new FileWriter(fileDir + "/challenge", true);
        } else {
            challengeFileWriter = new FileWriter(fileDir + "/challenge");
        }
        if (activitiesFile.exists()) {
            activitiesFileWriter = new FileWriter(fileDir + "/activity", true);
        } else {
            activitiesFileWriter = new FileWriter(fileDir + "/activity");
        }
        myActivities = getAllActivities();
        allChallenges = getAllChallenges();
    }

    public void writeChallenge(Challenge challenge) throws IOException {
        String challengeStr =
                Integer.toString(challenge.challengeType) + " "
                        + challenge.challengeName + " "
                        + Double.toString(challenge.completed) + " "
                        + Integer.toString(challenge.total) + " "
                        + Integer.toString(challenge.endDate.day) + " "
                        + Integer.toString(challenge.endDate.month) + " "
                        + Integer.toString(challenge.endDate.year) + "\n";

        allChallenges.add(0, challenge);
        challengeFileWriter.append(challengeStr);
        challengeFileWriter.close();
        challengeFileWriter = new FileWriter(fileDir + "/challenge", true);
    }

    public void writeActivity(RunningActivity activity) throws IOException {
        String activityStr =
                Double.toString(activity.distance) + " "
                        + Double.toString(activity.time) + "\n"
                        + activity.title + "\n"
                        + activity.startDate.day + " "
                        + activity.startDate.month + " "
                        + activity.startDate.year + "\n"
                        + activity.location + "\n"
                        + activity.routes.size();
        for (int i = 0; i < activity.routes.size(); ++i) {
            activityStr += " " + activity.routes.get(i).latitude + " " + activity.routes.get(i).longitude;
        }
        activityStr += "\n";
        myActivities.add(0, activity);
        activitiesFileWriter.append(activityStr);
        activitiesFileWriter.close();
        activitiesFileWriter = new FileWriter(fileDir + "/activity", true);
    }

    public ArrayList<RunningActivity> getAllActivities() throws FileNotFoundException {
        ArrayList<RunningActivity> activities = new ArrayList<>();

        Scanner sc = new Scanner(activitiesFile);
        Log.d("ACTIVITIESSSSSSS", "BEFORE");
        while (sc.hasNext()) {
            //Log.d("hihi", sc.nextLine());

            double distance = sc.nextDouble();
            double time = sc.nextDouble();
            sc.nextLine();
            String title = sc.nextLine();
            int day = sc.nextInt(), month = sc.nextInt(), year = sc.nextInt();
            sc.nextLine();
            String location = sc.nextLine();
            int size = sc.nextInt();
            ArrayList<LatLng> routes = new ArrayList<>();
            for (int i = 0; i < size; ++i) {
                double lat = sc.nextDouble(), lng = sc.nextDouble();
                routes.add(new LatLng(lat, lng));
            }
            activities.add(0, new RunningActivity(distance, time, "none", title, location, new MyDate(day, month, year), routes));
        }
        return activities;
    }

    public ArrayList<Challenge> getAllChallenges() throws FileNotFoundException {
        ArrayList<Challenge> challenges = new ArrayList<>();

        Scanner sc = new Scanner(challengeFile);

        Log.d("get_all", "before");
        while (sc.hasNext()) {
            int type = sc.nextInt();
            String title = sc.next();
            double completed = sc.nextDouble();
            int total = sc.nextInt();
            int day = sc.nextInt(), month = sc.nextInt(), year = sc.nextInt();
            Log.d("hello", "helooooooo");
            challenges.add(0, new Challenge(title, type, total, completed, new MyDate(day, month, year)));
        }

        return challenges;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.d("Hello", "Goodbye");
        challengeFileWriter.close();
        activitiesFileWriter.close();
    }

}
