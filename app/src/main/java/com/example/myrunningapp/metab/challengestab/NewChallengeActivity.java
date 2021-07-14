package com.example.myrunningapp.metab.challengestab;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrunningapp.R;
import com.example.myrunningapp.data.InternalStorage;
import com.example.myrunningapp.utils.MyDate;
import com.google.android.material.slider.RangeSlider;

import java.io.IOException;
import java.time.LocalDate;

public class NewChallengeActivity extends AppCompatActivity {

    private String[] challengesType = {"Running distance", "Running steps"};
    private int currentChallenge;
    private EditText challengeTitle, targetValue;
    private TextView targetUnit;
    private int backupTarget;
    private RangeSlider slider;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_challenge);
        backupTarget = -1;
        currentChallenge = Challenge.RUNNING_DISTANCE_CHALLENGE;
        retrieveViews();
        initToolbar();
        initRadioGroup();
        initTargetEditText();
        initSlider();
        initDeadlinePicker();
        initAddButton();
    }

    private void retrieveViews() {
        challengeTitle = findViewById(R.id.challenge_title);
        targetValue = findViewById(R.id.challenge_target);
        targetUnit = findViewById(R.id.target_unit);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog warning = new AlertDialog.Builder(
                        NewChallengeActivity.this,
                        R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Me).create();
                warning.setTitle("Are you sure you want to go back?");
                warning.setMessage("The challenge you created has not been saved. Once you go back, the information is deleted.");
                warning.setButton(AlertDialog.BUTTON_POSITIVE, "Go back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Go back to previous activity
                        setResult(RESULT_CANCELED);
                        onBackPressed();
                        dialog.dismiss();
                    }
                });
                warning.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Stay here
                        dialog.dismiss();
                    }
                });
                warning.show();
            }
        });
    }

    private void initRadioGroup() {
        RadioGroup challengesType = findViewById(R.id.challenge_type);
        challengesType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                toggleChallenge();
            }
        });
    }

    private void initTargetEditText() {
        targetValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Change slider to reflect the inputed text
                if (s.toString().isEmpty()) {
                    targetValue.setText("0");
                    return;
                }
                int inputTarget = Integer.parseInt(s.toString());
                int minTarget = (int) slider.getValueFrom();
                int maxTarget = (int) slider.getValueTo();

                if (inputTarget > maxTarget) {
                    slider.setValues((float) maxTarget);
                } else if (inputTarget < minTarget) {
                    slider.setValues((float) minTarget);
                } else {
                    int base = (int) Math.floor(inputTarget / 10);
                    slider.setValues((float) base * 10);
                }
            }
        });
    }

    private void toggleChallenge() {
        toggleSlider();
        if (currentChallenge == Challenge.RUNNING_DISTANCE_CHALLENGE) {
            currentChallenge = Challenge.RUNNING_STEP_CHALLENGE;
            challengeTitle.setHint("My running steps challenge");
            targetUnit.setText("steps");
        } else {
            currentChallenge = Challenge.RUNNING_DISTANCE_CHALLENGE;
            challengeTitle.setHint("My running distance challenge");
            targetUnit.setText("km");
        }
    }

    private void initSlider() {
        slider = findViewById(R.id.slider);

        // Default slider
        setSlider(Challenge.RUNNING_DISTANCE_CHALLENGE, 10);

        slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                if (fromUser)
                    targetValue.setText(Integer.toString((int) value));
            }
        });
    }

    private void toggleSlider() {
        int currentTarget = Integer.parseInt(targetValue.getText().toString());
        if (currentChallenge == Challenge.RUNNING_DISTANCE_CHALLENGE) {
            setSlider(Challenge.RUNNING_STEP_CHALLENGE, backupTarget);
        } else {
            setSlider(Challenge.RUNNING_DISTANCE_CHALLENGE, backupTarget);
        }
        backupTarget = currentTarget;
    }

    private void setSlider(int type, int value) {
        if (type == Challenge.RUNNING_DISTANCE_CHALLENGE) {
            slider.setValueFrom(10);
            slider.setValueTo(200);
            slider.setStepSize(10);
            // Default value set
            if (value == -1)
                value = 10;
        } else {
            slider.setValueFrom(10000);
            slider.setValueTo(200000);
            slider.setStepSize(10000);
            // Default value set
            if (value == -1)
                value = 10000;
        }
        int sliderValue = Math.max(
                (int)slider.getValueFrom(),
                Math.min((int)slider.getValueTo(), value)
        );
        slider.setValues((float)sliderValue);
        targetValue.setText(Integer.toString(value));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initDeadlinePicker() {
        final NumberPicker day = findViewById(R.id.day);
        final NumberPicker month = findViewById(R.id.month);
        final NumberPicker year = findViewById(R.id.year);

        LocalDate today = LocalDate.now();

        day.setMinValue(1);
        day.setMaxValue(MyDate.getDaysOfMonth(today.getMonthValue(), today.getYear()));
        day.setValue(today.getDayOfMonth());

        month.setMinValue(1);
        month.setMaxValue(12);
        month.setValue(today.getMonthValue());
        month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                day.setMaxValue(MyDate.getDaysOfMonth(newVal, year.getValue()));
                if (day.getValue() > day.getMaxValue())
                    day.setValue(day.getMaxValue());
            }
        });

        year.setMinValue(today.getYear());
        year.setMaxValue(today.getYear() + 5);
        year.setValue(today.getYear());
    }

    private void initAddButton() {
        final NumberPicker day = findViewById(R.id.day);
        final NumberPicker month = findViewById(R.id.month);
        final NumberPicker year = findViewById(R.id.year);
        Button button = findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                MyDate date = new MyDate(day.getValue(), month.getValue(), year.getValue());
                if (date.daysLeftFromNow() <= 0) {
                    Toast.makeText(NewChallengeActivity.this, "The deadline is in the past!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String title = challengeTitle.getText().toString().trim();

                if (title.isEmpty())
                    title = currentChallenge == Challenge.RUNNING_DISTANCE_CHALLENGE ? "My running distance challenge" : "My running steps challenge";
                Challenge challenge = new Challenge(
                        title,
                        currentChallenge,
                        Integer.parseInt(targetValue.getText().toString()),
                        date
                );
                try {
                    new InternalStorage(getApplicationContext()).writeChallenge(challenge);
                    Intent intent = new Intent();
                    intent.putExtra("NEW_CHALLENGE", challenge);
                    setResult(RESULT_OK, intent);
                    onBackPressed();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(NewChallengeActivity.this, "Error occured. Cannot create new challenge.", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        });
    }
}
