package com.example.myrunningapp.data;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {
    private static SharedPreferences userPreference;
    private String packageName = "com.example.myrunningapp";

    public UserPreference(Context context) {
        userPreference = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
    }

    public boolean haveUserRegistered() {
        return userPreference.contains("name");
    }

    public String getUserName() {
        return userPreference.getString("name", "none");
    }

    public int getUserHeight() {
        return userPreference.getInt("height", 160);
    }

    public int getUserWeight() {
        return userPreference.getInt("weight", 60);
    }

    public void setUserName(String name) {
        userPreference.edit().putString("name", name).apply();
    }

    public void setHeight(int height) {
        userPreference.edit().putInt("height", height).apply();
    }

    public void setWeight(int weight) {
        userPreference.edit().putInt("weight", weight).apply();
    }

}
