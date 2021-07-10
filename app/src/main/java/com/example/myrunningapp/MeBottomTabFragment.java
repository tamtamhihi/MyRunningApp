package com.example.myrunningapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MeBottomTabFragment extends Fragment {

    public MeBottomTabFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me_bottom_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyPagerAdapter adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        ViewPager2 viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        final TabLayout tabLayout = view.findViewById(R.id.tablayout);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){
                    case 0:
                        tab.setText("PROGRESS");
                        break;
                    case 1:
                        tab.setText("ACTIVITY");
                        break;
                    case 2:
                        tab.setText("PROFILE");
                        break;
                }
            }
        }).attach();
    }
}
