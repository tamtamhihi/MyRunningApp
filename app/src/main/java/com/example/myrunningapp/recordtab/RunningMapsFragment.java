package com.example.myrunningapp.recordtab;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrunningapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RunningMapsFragment extends Fragment
        implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean isRunning = true;

    private Timer timer;
    private TimerTask timerTask, distanceTimeTask;
    private int second = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_running_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        final FloatingActionButton startStopBtn = (FloatingActionButton)view.findViewById(R.id.startrecord);
        final GridLayout gridLayoutRecord = (GridLayout)view.findViewById(R.id.infor);
        final TextView timeIndex = (TextView)view.findViewById(R.id.index_time);
        final TextView distanceIndex = (TextView)view.findViewById(R.id.index_distance);
        final TextView paceIndex = (TextView)view.findViewById(R.id.index_pace);
        final TextView stepIndex = (TextView)view.findViewById(R.id.index_step);

        final ArrayList<LatLng> mLatLng = new ArrayList<>();
        final double[] mDistance = {0};

        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRunning) {
                    gridLayoutRecord.setVisibility(View.VISIBLE);
                    startStopBtn.setImageResource(R.drawable.stop);

                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(isRunning)
                                        second = 0;
                                    else
                                        ++second;
                                    Log.d("Hello", Integer.toString(second));
                                    int mMinute = (int)second/60;
                                    int mSecond = (int)second%60;
                                    timeIndex.setText(Integer.toString(mMinute) + "m " + Integer.toString(mSecond)+"s");
                                }
                            });
                        }
                    };
                    timer = new Timer();
                    timer.schedule(timerTask, 1000, 1000);

                    distanceTimeTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (ActivityCompat.checkSelfPermission(getActivity(),
                                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            fusedLocationProviderClient.getLastLocation()
                                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(final Location location) {
                                            if(location != null){
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                                                        mLatLng.add(current);
                                                        int n = mLatLng.size();
                                                        float[] results = new float[3];
                                                        if(n > 1) {
                                                            Location.distanceBetween(mLatLng.get(n - 1).latitude,
                                                                    mLatLng.get(n - 1).longitude, mLatLng.get(n - 2).latitude,
                                                                    mLatLng.get(n - 2).longitude, results);
                                                            mDistance[0] += results[0];
                                                        }

                                                        String km = String.format("%.2f km", mDistance[0]/1000);
                                                        distanceIndex.setText(km);

                                                        double mPace = second/mDistance[0]*1000;
                                                        int mi = (int)mPace/60;
                                                        int se = (int)mPace%60;
                                                        if(mi > 100)
                                                            paceIndex.setText("---");
                                                        else
                                                            paceIndex.setText(Integer.toString(mi) + ":" + Integer.toString(se) + " /km");

                                                        int steps = (int)mDistance[0]/8;
                                                        stepIndex.setText(Integer.toString(steps));

                                                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(current, 18, 0 ,0)));
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(getActivity(), "No location", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    };

                    timer.schedule(distanceTimeTask, 2000,2000);
                    isRunning = false;

                } else {
                    gridLayoutRecord.setVisibility(View.GONE);
                    startStopBtn.setImageResource(R.drawable.record_icon);
                    isRunning = true;
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        Log.d("Hello", "Dientran");
        enableMyLocation();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions()
                                    .position(current)
                                    .title("Marker in current location"));
                            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(current, 18, 0 ,0)));
                        } else {
                            Toast.makeText(getActivity(), "No location", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void enableMyLocation() {
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
            Log.d("Hello", "TianXin");
            if(mMap != null){
                mMap.setMyLocationEnabled(true);
                Log.d("Hello", "Tam");
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
            , LOCATION_PERMISSION_REQUEST_CODE );
            Log.d("Hello", "Dien");
        }
    }
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT ).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode != LOCATION_PERMISSION_REQUEST_CODE){
            return;
        }
        if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            enableMyLocation();
        } else {
            permissionDenied = true;
        }
    }

}