package com.example.thesix;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.thesix.databinding.ActivityMapsBinding;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private long eventnum;
    private String OrganizerdeviceID;
    private AttendeeDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database= new AttendeeDB();

        Bundle bundle = getIntent().getExtras();
        OrganizerdeviceID = bundle.getString("OrganizerdeviceID");
        eventnum = bundle.getLong("eventNum");

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        database.getLocationDocRef(OrganizerdeviceID, eventnum, new AttendeeDB.LocationCallback() {
            @Override
            public void onLocationReceived(List<GeoPoint> locationList) {
                Log.i("ReceivedLocationList", locationList.toString());
                addMarkers(locationList);
            }

            @Override
            public void onLocationError(String errorMessage) {
                Log.e("LocationError", errorMessage);
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private void addMarkers(List<GeoPoint> locationList) {
        for (GeoPoint point : locationList) {
            double latitude = point.getLatitude();
            double longitude = point.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title("Attendee"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        }

    }

