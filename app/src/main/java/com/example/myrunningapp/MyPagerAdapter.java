package com.example.myrunningapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyPagerAdapter extends FragmentStateAdapter {


    public MyPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProgressTabFragment();
            case 1:
                return new ChallengesTabFragment();
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
