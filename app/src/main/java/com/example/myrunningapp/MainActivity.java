package com.example.myrunningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton reminderButton = findViewById(R.id.reminder);
        reminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
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
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        FloatingActionButton contactButton = findViewById(R.id.contact);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_DialogWhenLarge_NoActionBar);
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
                        if (intent.resolveActivity(getPackageManager()) != null) {
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
