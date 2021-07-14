package com.example.myrunningapp.metab.challengestab;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrunningapp.R;
import com.example.myrunningapp.data.InternalStorage;
import com.example.myrunningapp.hometab.RunningActivity;
import com.example.myrunningapp.utils.MyDate;

import java.io.IOException;
import java.util.ArrayList;


public class ChallengesTabFragment extends Fragment {

    ArrayList<Challenge> myChallenges;
    ArrayList<RunningActivity> activities;
    ChallengesAdapter adapter;
    TextView warning;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ChallengesTabFragment(ArrayList<RunningActivity> activities) {
        this.activities = activities;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challenges_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            myChallenges = new InternalStorage(getContext()).getAllChallenges();
        } catch (IOException e) {
            e.printStackTrace();
            myChallenges = new ArrayList<>();
        }
        RecyclerView challengesRecyclerView = view.findViewById(R.id.challenges_recyclerview);
        adapter = new ChallengesAdapter(myChallenges, getContext(), activities);
        challengesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        challengesRecyclerView.setAdapter(adapter);
        warning = view.findViewById(R.id.warning);
        if (myChallenges.size() == 0)
            warning.setVisibility(View.VISIBLE);
        else
            warning.setVisibility(View.GONE);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.challenges_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                //Toast.makeText(getContext(), "You can't add a new challenge now.", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(getContext(), NewChallengeActivity.class), 1);
                //startActivityForResult(new Intent(getContext(), NewChallengeActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (myChallenges.size() == 0)
            warning.setVisibility(View.VISIBLE);
        else
            warning.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        Challenge c = (Challenge) data.getSerializableExtra("NEW_CHALLENGE");
        if (c != null) {
            myChallenges.add(c);
            adapter.notifyDataSetChanged();
        }
    }
}
