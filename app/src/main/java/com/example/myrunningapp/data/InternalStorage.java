package com.example.myrunningapp.data;

import android.content.Context;
import android.util.Log;

import com.example.myrunningapp.metab.challengestab.Challenge;
import com.example.myrunningapp.utils.MyDate;

import java.io.Console;
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

        Log.d("Helllooooooooooooo", challengeStr);
        challengeFileWriter.append(challengeStr);
        challengeFileWriter.close();
        challengeFileWriter = new FileWriter(fileDir + "/challenge", true);
        Log.d("Hellooooo", challengeFileWriter.getEncoding());
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
            challenges.add(new Challenge(title, type, total, completed, new MyDate(day, month, year)));
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
