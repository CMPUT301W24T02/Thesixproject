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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_screen);

        back2AttendeesButton = findViewById(R.id.map2AttendeesButton);
        map =findViewById(R.id.map);
        SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /**
         Initializes a UI component, a Button named back2AttendeesButton
         @param :
         @return
         **/
        back2AttendeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapActivity.this, AttendeeListActivity.class));
            }
        });
    }

    @Override
    //https://www.youtube.com/watch?v=JzxjNNCYt_o&ab_channel=AndroidKnowledge
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng coordinates = new LatLng(25.2048,55.2708);
        this.gMap.addMarker(new MarkerOptions().position(coordinates).title("dummy"));
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));

    }
}
