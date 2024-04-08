package com.example.thesix;

import static org.mockito.Mockito.*;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.List;

public class MapsActivityTest {

    @Mock
    private GoogleMap mockGoogleMap;

    @Mock
    private SupportMapFragment mockMapFragment;

    @Mock
    private AttendeeDB mockAttendeeDB;

    @Mock
    private AttendeeDB.LocationCallback mockLocationCallback;

    private MapsActivity mapsActivity;

    @Before
    public void setUp() {
        mapsActivity = new MapsActivity();
        mapsActivity.mMap = mockGoogleMap;

        // Mock behavior for SupportMapFragment
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                OnMapReadyCallback callback = invocation.getArgument(0);
                callback.onMapReady(mockGoogleMap);
                return null;
            }
        }).when(mockMapFragment).getMapAsync(any(OnMapReadyCallback.class));


        // Inject mockAttendeeDB into mapsActivity
        mapsActivity.database = mockAttendeeDB;
    }

    @Test
    public void testOnCreate() {
        Bundle bundle = mock(Bundle.class);
        when(bundle.getString("OrganizerdeviceID")).thenReturn("organizerDeviceId");
        when(bundle.getLong("eventNum")).thenReturn(123L);

        Intent mockIntent = mock(Intent.class);
        when(mockIntent.getExtras()).thenReturn(bundle);

        // Call onCreate
        mapsActivity.onCreate(bundle);

        // Verify that the necessary methods were called
        verify(mockAttendeeDB).getLocationDocRef(eq("OrganizerDeviceId"), eq(123L), any(AttendeeDB.LocationCallback.class));
    }
    @Test
    public void testMarkers() {
        List<GeoPoint> mockLocationList = Arrays.asList(
                new GeoPoint(40.7128, -74.0060),  // New York City
                new GeoPoint(34.0522, -118.2437)  // Los Angeles
        );
        assert (mockLocationList.size()==2);

        mapsActivity.addMarkers(mockLocationList);

        // Verify that the necessary methods were called
        verify(mockGoogleMap, times(2)).addMarker(any(MarkerOptions.class));
        verify(mockGoogleMap, times(2)).moveCamera(any(CameraUpdate.class));
    }

    @Test
    public void testAddMarkers() {
        List<GeoPoint> mockLocationList = Arrays.asList(
                new GeoPoint(40.7128, -74.0060),  // New York City
                new GeoPoint(34.0522, -118.2437)  // Los Angeles
        );

        mapsActivity.addMarkers(mockLocationList);

        // Verify that the necessary methods were called
        verify(mockGoogleMap, times(2)).addMarker(any(MarkerOptions.class));
        verify(mockGoogleMap, times(2)).moveCamera(any(CameraUpdate.class));
    }
}
