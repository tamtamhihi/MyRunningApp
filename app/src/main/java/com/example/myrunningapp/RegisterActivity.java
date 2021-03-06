package com.example.myrunningapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myrunningapp.data.InternalStorage;
import com.example.myrunningapp.data.UserPreference;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    EditText name, height, weight;
    ImageButton newAvatar;
    Button continueButton;
    CircleImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        retrieveViews();
        setupNewAvatarButton();
        setupContinueButton();
    }


    private void retrieveViews() {
        name = findViewById(R.id.name);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        newAvatar = findViewById(R.id.new_ava_button);
        continueButton = findViewById(R.id.continue_button);
        avatar = findViewById(R.id.avatar);
    }

    private void setupNewAvatarButton() {
        newAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri avatarUri = data.getData();
            new UserPreference(this).setAvatarUri(avatarUri);
            try {
                Bitmap avatarBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), avatarUri);
                avatar.setImageBitmap(avatarBitmap);
                InternalStorage.addAvatar(avatarBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void setupContinueButton() {
        final UserPreference userPreference = new UserPreference(this);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString().trim();
                int userHeight = Integer.parseInt(height.getText().toString().trim());
                int userWeight = Integer.parseInt(weight.getText().toString().trim());
                if (userName.isEmpty() || userHeight <= 0 || userWeight <= 0) {
                    Toast.makeText(RegisterActivity.this, "The information is not suitable! Try again.", Toast.LENGTH_LONG).show();
                    return;
                }
                userPreference.setUserName(userName);
                userPreference.setHeight(userHeight);
                userPreference.setWeight(userWeight);
                RegisterActivity.this.finish();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }
}
