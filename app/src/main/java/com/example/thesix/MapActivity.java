package com.example.thesix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * MapActivity class facilitates the display of a map screen within the application.
 */

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private Button back2AttendeesButton;

    GoogleMap gMap;
    FrameLayout map;

    /** Creating saved Map Activity
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_screen);

        //making instance back2attendee and maps
        back2AttendeesButton = findViewById(R.id.map2AttendeesButton);
        map =findViewById(R.id.map);
        SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        back2AttendeesButton.setOnClickListener(new View.OnClickListener() {
            /**Initializes a UI component, a Button named back2AttendeesButton
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapActivity.this, AttendeeListActivity.class));
            }
        });
    }

    /** making instance sample
     * @param googleMap  map instance
     */
    @Override
    //https://www.youtube.com/watch?v=JzxjNNCYt_o&ab_channel=AndroidKnowledge
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng coordinates = new LatLng(25.2048,55.2708);
        this.gMap.addMarker(new MarkerOptions().position(coordinates).title("dummy"));
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));

    }
}
