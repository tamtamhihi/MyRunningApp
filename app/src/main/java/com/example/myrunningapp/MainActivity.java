package com.example.myrunningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myrunningapp.hometab.HomeBottomTabFragment;
import com.example.myrunningapp.metab.MeBottomTabFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: 3 tabs: HOME TAB (display all activities), RECORD TAB (record), ME TAB (profile)
        final HomeBottomTabFragment homeFragment = new HomeBottomTabFragment();
        final ProgressTabFragment activityFragment = new ProgressTabFragment();
        final MeBottomTabFragment meFragment = new MeBottomTabFragment();

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
                        setCurrentFragment(activityFragment);
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
