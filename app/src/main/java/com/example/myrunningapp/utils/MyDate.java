package com.example.myrunningapp.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.Console;
import java.time.LocalDate;

public class MyDate {
    public int day, month, year;
    static int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public MyDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int daysUntilNow() {
        LocalDate today = LocalDate.now();
        Log.d("HELLO", today.toString());
        int cday = today.getDayOfMonth(), cmonth = today.getMonthValue(), cyear = today.getYear();
        Log.d("HELLOOOOOOOOOOO", Integer.toString(cday) + "/" + Integer.toString(cmonth) + "/" + Integer.toString(cyear));
        Log.d("DIENNNNNNNN", Integer.toString(today.getDayOfYear()));

        if (cyear == year) {
            Log.d("TAMMMMMMMMMM", Integer.toString(getDayOfYear()));
            Log.d("TIANXIN", Integer.toString(getDayOfYear() - today.getDayOfYear()));
            return getDayOfYear() - today.getDayOfYear();
        }

        if (cyear < year) {
            int count = checkDaysOfYear(cyear) - today.getDayOfYear() + 1;
            for (int y = cyear + 1; y < year - 1; ++y) {
                count += checkDaysOfYear(y);
            }
            count += getDayOfYear();
            return count;
        }

        return -1;
    }

    private boolean isLeapYear() {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static boolean checkLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private int getDaysOfYear() {
        return isLeapYear() ? 366 : 365;
    }

    public static int checkDaysOfYear(int year) {
        return checkLeapYear(year) ? 366 : 365;
    }

    private int getDayOfYear() {
        int count = 0;
        for (int i = 0; i < month - 1; ++i) {
            count += days[i];
            if (i == 1 && isLeapYear())
                count++;
        }
        count += day;
        return count;
    }
}
