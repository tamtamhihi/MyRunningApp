package com.example.myrunningapp.metab;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myrunningapp.ProgressTabFragment;
import com.example.myrunningapp.hometab.RunningActivity;
import com.example.myrunningapp.metab.challengestab.ChallengesTabFragment;
import com.example.myrunningapp.metab.profiletab.ProfileTabFragment;
import com.example.myrunningapp.metab.statisticstab.StatisticsTabFragment;

import java.sql.Array;
import java.util.ArrayList;

public class MyPagerAdapter extends FragmentStateAdapter {

    private ArrayList<RunningActivity> activities;

    public MyPagerAdapter(FragmentActivity fragmentActivity, ArrayList<RunningActivity> activities) {
        super(fragmentActivity);
        this.activities = activities;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            // TODO: Case 0 is STATISTIC TAB (personal record,etc.)
            case 0:
                return new StatisticsTabFragment(activities);
            case 1:
                return new ChallengesTabFragment(activities);
            case 2:
                return new ProfileTabFragment();
            default:
                return new ProfileTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
