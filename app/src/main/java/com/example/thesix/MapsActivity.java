package com.example.thesix;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.thesix.databinding.ActivityMapsBinding;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Reference to the Google Map object
    private ActivityMapsBinding binding;  // View binding object for the activity_maps layout
    private long eventnum; // Event number associated with the map
    private String OrganizerdeviceID;
    private AttendeeDB database;

    /** On Creating Maps Activity
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database= new AttendeeDB();
        Log.i("wedatabase", "onCreate: ");
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
            /** On Location Received
             * @param locationList list of geopoints
             */
            @Override
            public void onLocationReceived(List<GeoPoint> locationList) {
                Log.i("ReceivedLocationList", locationList.toString());
                addMarkers(locationList);
            }

            /** On error Location Error
             * @param errorMessage to adding locaton error
             */
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
     * we just add markers .
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * @param locationList list of Geolocations
     */
    private void addMarkers(List<GeoPoint> locationList) {
        for (GeoPoint point : locationList) {
            Log.i("ReceivedLocationList1", locationList.toString());
            double latitude = point.getLatitude();
            double longitude = point.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title("Attendee"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((latLng),11));

        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        }

    }

