package com.example.myrunningapp.hometab;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrunningapp.R;
import com.example.myrunningapp.data.InternalStorage;
import com.example.myrunningapp.data.UserPreference;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailedRunActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private RunningActivity currentActivity;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_run);

        Intent intent = getIntent();
        int index = intent.getIntExtra("index", 0);
        if (index >= InternalStorage.myActivities.size()) {
            Toast.makeText(this, "Running activity does not exist", Toast.LENGTH_LONG).show();
            this.finish();
        } else {

            currentActivity = InternalStorage.myActivities.get(index);
            CircleImageView avatar = findViewById(R.id.avatar);
            TextView name = findViewById(R.id.nameID), locationAndTime = findViewById(R.id.location);
            TextView title = findViewById(R.id.titleID);
            TextView distance = findViewById(R.id.distanceID), time = findViewById(R.id.timeID), pace = findViewById(R.id.paceID);

            avatar.setImageBitmap(InternalStorage.avatar);
            name.setText(new UserPreference(this).getUserName());
            distance.setText(String.format("%.2f km", currentActivity.distance / 1000));
            int second = (int) currentActivity.time % 60;
            int minute = (int) currentActivity.time / 60;
            time.setText(Integer.toString(minute) + "m " + Integer.toString(second) + "s");
            double pacer = currentActivity.time / currentActivity.distance;
            int paceMinute = (int) pacer / 60;
            int paceSecond = (int) pacer % 60;
            if (paceMinute > 100)
                pace.setText("00:00/km");
            else
                pace.setText(Integer.toString(paceMinute) + ":" + Integer.toString(paceSecond) + "/km");
            title.setText(currentActivity.title);
            int daysDiff = currentActivity.startDate.daysLeftFromNow() - 1;
            String dateString;
            if (daysDiff == 0) {
                dateString = "Today, ";
            } else if (daysDiff == 1) {
                dateString = "Yesterday, ";
            } else if (daysDiff <= 6) {
                dateString = Integer.toString(daysDiff) + " days ago, ";
            } else {
                dateString = currentActivity.startDate.toString() + ", ";
            }
            locationAndTime.setText(dateString + currentActivity.location);

            final SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        //final Polyline polyline = null;
        PolylineOptions options = new PolylineOptions().clickable(true).width(7)
                .color(ContextCompat.getColor(this, R.color.colorPrimary));
        double lat = 0, lng = 0;
        for (LatLng point : currentActivity.routes) {
            options.add(point);
            lat += point.latitude;
            lng += point.longitude;
        }
        lat /= currentActivity.routes.size();
        lng /= currentActivity.routes.size();
        map.addPolyline(options);
        map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(lat, lng), 16, 0, 0)));
    }
}
