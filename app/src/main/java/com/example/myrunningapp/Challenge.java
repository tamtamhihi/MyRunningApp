package com.example.myrunningapp;

import java.util.Date;

public class Challenge {
    static int RUNNING_STEP_CHALLENGE = 1, RUNNING_DISTANCE_CHALLENGE = 2;

    public int challengeType;
    public String challengeName;
    public Date startDate, endDate;

    Challenge(String challengeName, int challengeType, Date startDate, Date endDate) {
        this.challengeName = challengeName;
        this.challengeType = challengeType;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
