package com.example.myrunningapp.recordtab;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrunningapp.MainActivity;
import com.example.myrunningapp.R;
import com.example.myrunningapp.data.InternalStorage;
import com.example.myrunningapp.data.UserPreference;
import com.example.myrunningapp.hometab.RunningActivity;
import com.example.myrunningapp.metab.challengestab.Challenge;
import com.example.myrunningapp.utils.MyDate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class RunningMapsFragment extends Fragment
        implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean isRunning = true;

    private Timer timer;
    private TimerTask timerTask, distanceTimeTask;
    private int second = 0;

    private String locationInfor;
    private ArrayList<Challenge> myChallenges;

    public RunningMapsFragment(ArrayList<Challenge> myChallenges) {
        this.myChallenges = myChallenges;
    }

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
        enableMyLocation();

        final FloatingActionButton startStopBtn = (FloatingActionButton) view.findViewById(R.id.startrecord);
        final GridLayout gridLayoutRecord = (GridLayout) view.findViewById(R.id.infor);
        final TextView timeIndex = (TextView) view.findViewById(R.id.index_time);
        final TextView distanceIndex = (TextView) view.findViewById(R.id.index_distance);
        final TextView paceIndex = (TextView) view.findViewById(R.id.index_pace);
        final TextView stepIndex = (TextView) view.findViewById(R.id.index_step);
        final Polyline[] polyline = new Polyline[1];
        final PolylineOptions options = new PolylineOptions().clickable(true).width(7)
                .color(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        final ArrayList<LatLng> mLatLng = new ArrayList<>();
        final double[] mDistance = {0};

        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    gridLayoutRecord.setVisibility(View.VISIBLE);
                    startStopBtn.setImageResource(R.drawable.stop);

                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isRunning)
                                        second = 0;
                                    else
                                        ++second;
                                    Log.d("Hello", Integer.toString(second));
                                    int mMinute = (int) second / 60;
                                    int mSecond = (int) second % 60;
                                    timeIndex.setText(Integer.toString(mMinute) + "m " + Integer.toString(mSecond) + "s");
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
                                            if (location != null) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());

                                                        mLatLng.add(current);
                                                        int n = mLatLng.size();
                                                        float[] results = new float[3];
                                                        if(n==1){
                                                            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                                                            List<Address> addresses = null;
                                                            try {
                                                                addresses = gcd.getFromLocation(location.getLatitude(),
                                                                        location.getLongitude(), 5);
                                                            } catch (IOException e) {
                                                                addresses = new ArrayList<>();
                                                                e.printStackTrace();
                                                            }
                                                            if (addresses.size() > 0) {
                                                                int i;
                                                                for(i=0; i< addresses.size();i++)
                                                                    if(addresses.get(i).getLocality() != null)
                                                                        break;
                                                                locationInfor = addresses.get(i).getLocality();
                                                            }
                                                            else {
                                                                locationInfor = "Earth";
                                                            }
                                                        }
                                                        if (n > 1) {
                                                            Location.distanceBetween(mLatLng.get(n - 1).latitude,
                                                                    mLatLng.get(n - 1).longitude, mLatLng.get(n - 2).latitude,
                                                                    mLatLng.get(n - 2).longitude, results);
                                                            mDistance[0] += results[0];
                                                        }

                                                        options.add(mLatLng.get(n-1));
                                                        polyline[0] = mMap.addPolyline(options);

                                                        String km = String.format("%.2f km", mDistance[0] / 1000);
                                                        distanceIndex.setText(km);

                                                        double mPace = second / mDistance[0] * 1000;
                                                        int mi = (int) mPace / 60;
                                                        int se = (int) mPace % 60;
                                                        if (mi > 100)
                                                            paceIndex.setText("---");
                                                        else
                                                            paceIndex.setText(Integer.toString(mi) + ":" + Integer.toString(se) + " /km");

                                                        int steps = (int) mDistance[0] * 10 / 8;

                                                        stepIndex.setText(Integer.toString(steps));

                                                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(current, 18, 0, 0)));
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(getContext(), "No location", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    };

                    timer.schedule(distanceTimeTask, 2000, 2000);
                    isRunning = false;

                } else {
                    final String[] mText = new String[1];
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Title");
                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mText[0] = input.getText().toString();
                            Log.d("hello", mText[0]);
                            gridLayoutRecord.setVisibility(View.GONE);
                            startStopBtn.setImageResource(R.drawable.record_icon);
                            LocalDate today = LocalDate.now();

                            RunningActivity newActivity = new RunningActivity(
                                    mDistance[0], second, "none", mText[0],
                                    locationInfor, new MyDate(today.getDayOfMonth(), today.getMonthValue(), today.getYear()),
                                    mLatLng);
                            try {
                                new InternalStorage(getContext()).writeActivity(newActivity);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            isRunning = true;
                            timerTask.cancel();
                            distanceTimeTask.cancel();
                            getActivity().finish();
                            startActivity(new Intent(getContext(), MainActivity.class));
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mText[0] = "New Activity";
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();


                    //getParentFragmentManager().beginTransaction().detach(RunningMapsFragment.this).attach(RunningMapsFragment.this).commit();
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());



        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        showLocation();
    }

    private void showLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                            //mMap.addMarker(new MarkerOptions()
                            //        .position(current)
                            //        .title("Marker in current location"));
                            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(current, 18, 0, 0)));
                        } else {
                            Toast.makeText(getActivity(), "No location", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
            showLocation();
        } else {
            permissionDenied = true;
        }
    }


}
