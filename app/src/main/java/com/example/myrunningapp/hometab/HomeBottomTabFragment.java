package com.example.myrunningapp.hometab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myrunningapp.R;
import com.example.myrunningapp.utils.MyDate;

import java.util.ArrayList;


public class HomeBottomTabFragment extends Fragment {
    ArrayList<RunningActivity> myRunning;

    public HomeBottomTabFragment(ArrayList<RunningActivity> activities) {
        myRunning = activities;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView runningRecycleView = view.findViewById(R.id.home_recycleView);
        RunningActivityAdapter adapter = new RunningActivityAdapter(myRunning, getContext());
        runningRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        runningRecycleView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home_bottom_tab, container, false);
    }
}