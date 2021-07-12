package com.example.myrunningapp.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.Console;
import java.io.Serializable;
import java.time.LocalDate;

public class MyDate implements Serializable {
    public int day, month, year;
    static int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public MyDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int daysLeftFromNow() {
        LocalDate today = LocalDate.now();
        int cday = today.getDayOfMonth(), cmonth = today.getMonthValue(), cyear = today.getYear();

        if (cyear == year) {
            return getDayOfYear() - today.getDayOfYear() + 1;
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

    public static int getDaysOfMonth(int month, int year) {
        if (month != 2)
            return days[month-1];
        return checkLeapYear(year) ? 29 : 28;
    }
}
