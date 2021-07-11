package com.example.myrunningapp.metab.challengestab;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myrunningapp.utils.MyDate;
import com.example.myrunningapp.R;

import java.util.ArrayList;


public class ChallengesTabFragment extends Fragment {

    ArrayList<Challenge> myChallenges;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ChallengesTabFragment() {
        myChallenges = new ArrayList<>();
        // Required empty public constructor
        myChallenges.add(new Challenge(
                "Quarantine Walk",
                Challenge.RUNNING_STEP_CHALLENGE,
                50000,12341,
                new MyDate(24,7,2021)
        ));
        myChallenges.add(new Challenge(
                "Quarantine Run",
                Challenge.RUNNING_DISTANCE_CHALLENGE,
                100, 12.5,
                new MyDate(24,7,2021)
        ));
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
        RecyclerView challengesRecyclerView = view.findViewById(R.id.challenges_recyclerview);
        ChallengesAdapter adapter = new ChallengesAdapter(myChallenges, getContext());
        challengesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        challengesRecyclerView.setAdapter(adapter);
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
                Toast.makeText(getContext(), "You can't add a new challenge now.", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return true;
    }
}
