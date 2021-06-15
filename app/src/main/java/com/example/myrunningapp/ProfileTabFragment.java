package com.example.myrunningapp;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileTabFragment extends Fragment {

    public ProfileTabFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        renderButtons();
        RecyclerView recyclerView = view.findViewById(R.id.activities);
        double[] distance = {3.75, 4.00, 1.91, 2.08, 3.00};
        double[] pace = {7.34, 8.03, 8.07, 7.58, 7.55};
        int[] time = {1705, 1925, 934, 996, 1426};
        RunningActivityAdapter adapter = new RunningActivityAdapter(distance, pace, time, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void renderButtons() {
        FloatingActionButton reminderButton = getView().findViewById(R.id.reminder);
        reminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Material_Light_Dialog_NoActionBar);
                dialog.setContentView(R.layout.reminder_dialog);
                dialog.setCancelable(false);

                final EditText message = dialog.findViewById(R.id.message);
                final TimePicker timePicker = dialog.findViewById(R.id.time_picker);
                Button cancelButton = dialog.findViewById(R.id.cancel_button);
                Button okButton = dialog.findViewById(R.id.ok_button);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int minute = timePicker.getMinute();
                        int hour = timePicker.getHour();
                        String myMessage = message.getText().toString();
                        if (myMessage.trim().isEmpty())
                            myMessage = "My Workout";

                        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                                .putExtra(AlarmClock.EXTRA_MESSAGE, myMessage)
                                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                                .putExtra(AlarmClock.EXTRA_MINUTES, minute);
                        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        FloatingActionButton contactButton = getView().findViewById(R.id.contact);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Material_Light_DialogWhenLarge_NoActionBar);
                dialog.setContentView(R.layout.contact_dialog);
                dialog.setCancelable(false);

                final EditText emailstr = dialog.findViewById(R.id.editEmail);
                final EditText subjectstr = dialog.findViewById(R.id.editSubject);
                final EditText contentstr = dialog.findViewById(R.id.editContent);
                Button cancelBtn = dialog.findViewById(R.id.btnCancel);
                Button okBtn = dialog.findViewById(R.id.btnOK);

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = emailstr.getText().toString();
                        String subject = subjectstr.getText().toString();
                        String content = contentstr.getText().toString();

                        Intent intent = new Intent(Intent.ACTION_SENDTO)
                                .setData(Uri.parse("mailto:"))
                                .putExtra(Intent.EXTRA_EMAIL, email)
                                .putExtra(Intent.EXTRA_SUBJECT, subject)
                                .putExtra(Intent.EXTRA_TEXT, content);
                        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}
