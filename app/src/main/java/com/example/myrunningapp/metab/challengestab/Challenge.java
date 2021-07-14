package com.example.myrunningapp.metab.challengestab;

import com.example.myrunningapp.utils.MyDate;

import java.io.Serializable;

public class Challenge implements Serializable {
    static int RUNNING_STEP_CHALLENGE = 1, RUNNING_DISTANCE_CHALLENGE = 2;


    public int challengeType;
    public String challengeName;
    public double completed;
    public int total;
    public MyDate endDate;

    Challenge(String challengeName, int challengeType, int total, MyDate endDate) {
        this(challengeName, challengeType, total, 0, endDate);
    }

    public Challenge(String challengeName, int challengeType, int total, double completed, MyDate endDate) {
        this.challengeName = challengeName;
        this.challengeType = challengeType;
        this.completed = completed;
        this.total = total;
        this.endDate = endDate;
    }

}
