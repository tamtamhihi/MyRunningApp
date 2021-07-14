package com.example.myrunningapp.metab.profiletab;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.myrunningapp.R;
import com.example.myrunningapp.data.InternalStorage;
import com.example.myrunningapp.data.UserPreference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileTabFragment extends Fragment {

    private TextView height, weight, name;
    private CircleImageView avatar;
    private ImageView editHeight, editWeight;


    public ProfileTabFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            getUserProfile(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
        editButtons(view);
        renderButtons();
    }


    private void getUserProfile(View view) throws IOException {
        name = view.findViewById(R.id.name);
        height = view.findViewById(R.id.height);
        weight = view.findViewById(R.id.weight);
        avatar = view.findViewById(R.id.avatar);
        UserPreference userPreference = new UserPreference(getContext());
        String userName = userPreference.getUserName();
        int userHeight = userPreference.getUserHeight();
        int userWeight = userPreference.getUserWeight();
        Uri uri = Uri.parse(userPreference.getAvatarUri());
        name.setText(userName);
        height.setText(userHeight + " cm");
        weight.setText(userWeight + " kg");
        avatar.setImageBitmap(InternalStorage.avatar);
    }


    private void editButtons(View view) {
        editHeight = view.findViewById(R.id.edit_height);
        editWeight = view.findViewById(R.id.edit_weight);
        editHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        editWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
