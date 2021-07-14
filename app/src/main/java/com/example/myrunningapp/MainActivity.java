package com.example.myrunningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myrunningapp.data.InternalStorage;
import com.example.myrunningapp.data.UserPreference;
import com.example.myrunningapp.hometab.HomeBottomTabFragment;
import com.example.myrunningapp.hometab.RunningActivity;
import com.example.myrunningapp.metab.MeBottomTabFragment;
import com.example.myrunningapp.metab.challengestab.Challenge;
import com.example.myrunningapp.recordtab.RunningMapsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<RunningActivity> myActivities;

    private ArrayList<Challenge> myChallenges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myActivities = InternalStorage.myActivities;
        myChallenges = InternalStorage.allChallenges;

        // TODO: 3 tabs: HOME TAB (display all activities), RECORD TAB (record), ME TAB (profile)
        final HomeBottomTabFragment homeFragment = new HomeBottomTabFragment(myActivities);
        final RunningMapsFragment recordFragment = new RunningMapsFragment(myChallenges);
        final MeBottomTabFragment meFragment = new MeBottomTabFragment(myActivities);

        setCurrentFragment(homeFragment);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        setCurrentFragment(homeFragment);
                        break;
                    case R.id.record:
                        setCurrentFragment(recordFragment);
                        break;
                    case R.id.me:
                        setCurrentFragment(meFragment);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });


    }
    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}
