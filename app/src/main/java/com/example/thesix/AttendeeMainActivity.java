package com.example.thesix;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * AttendeeMainActivity class is responsible for managing the main activity for attendees.
 * It handles various functionalities such as scanning QR codes, viewing profiles, and accessing events.
 * Implements IbaseGpsListener interface for location-related operations.
 */

public class AttendeeMainActivity extends AppCompatActivity implements IbaseGpsListener{
    // Flag to track location permission request
    private boolean request =false;
    //requesting permission locations
    private static final int PERMISSION_LOCATION =1000;
    // UI elements
    private Button scanButton;
    private Button viewProfile;
    private Button eventsButton;
    TextView testing;
    private QrCodeDB firestoreHelper;
    String contents;
    String[] contentsArray;
    private LocationRequest locationRequest;
    String imageData,name,description;
    //private Button getLocation;
    //private TextView coordinates;
    // Database helper objects
    private AttendeeDB database;
    private Location lastKnownLocation; // To store the latest location
    private String deviceID;
    private String finalDeviceID;
    private String organizerID;
    private Long eventNum;
    private List<Long> checkInCountList;
    private List<String> attendeeIDList;
    private List<GeoPoint> locationList;
    private LocationManager locationManager;

    private LocationCallback locationCallback;
    private Button editButton;

    /**
     * Initializes the activity when created.
     *
     * @param savedInstanceState Bundle containing the activity's previously saved state.
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_main_activity);
        // Initialize UI elements
        scanButton = findViewById(R.id.scanButton);
        viewProfile = findViewById(R.id.viewAttendeeProfile);
        eventsButton = findViewById(R.id.allEventButton);
        //get deviceId
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        editButton = findViewById(R.id.editAttendeeProfile);

        //getLocation = findViewById(R.id.getLocationButton);
        //welcomeVIP=findViewById(R.id.welcome_vip);
        //coordinates = findViewById(R.id.locationinfo);

        //// Initialize device ID
        database = new AttendeeDB();
        //ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        //testing = findViewById(R.id.textView);
        firestoreHelper = new QrCodeDB();

      // Set click listener for edit button to navigate to attendee profile update activity
        editButton.setOnClickListener(new View.OnClickListener() {
            /**Set click listener for edit button to navigate to attendee profile update activity
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeMainActivity.this, AttendeeProfileUpdate.class));
            }
        });

        // Hi, I changed this part of code due to the failure of switch activity in the view profile button
        //Bundle bundle = getIntent().getExtras();
        //String deviceID = bundle.getString("deviceID");

        // Retrieve device ID from intent extras
        Bundle bundle = getIntent().getExtras();
        String deviceID = null;
        if (bundle != null) {
            deviceID = bundle.getString("deviceID");
        }
        finalDeviceID = deviceID;

        // Request notification permission
        askNotificationPermission();
        //// Check if GPS is enabled, if not prompt user to enable it
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
        }
        else {
            // GPS is not enabled, prompt user to enable it
            new AlertDialog.Builder(this)
                    .setMessage("GPS is not enabled. Do you want to enable it?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // User clicked Yes, open location settings
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // User clicked No, inform the user
                        Toast.makeText(this, "Enable Location services to track location", Toast.LENGTH_LONG).show();
                    })
                    .show();
        }
        scanButton.setOnClickListener(new View.OnClickListener() {
            /** // Set click listener for scan button to initiate QR code scanning
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //https://www.youtube.com/watch?v=bWEt-_z7BOY

                //IntentIntegrator intentIntegrator = new IntentIntegrator(AttendeeMainActivity.this);
                //intentIntegrator.setOrientationLocked(true);
                //intentIntegrator.setPrompt("Scan a QR Code");
                //intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                //intentIntegrator.initiateScan();

                // Request location permission if not granted
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                }
                else {
                    // Handle case where last known location is not available
                    //lastKnownLocation
                    showLocation();
                    IntentIntegrator intentIntegrator = new IntentIntegrator(AttendeeMainActivity.this);
                    intentIntegrator.setOrientationLocked(true);
                    intentIntegrator.setPrompt("Scan a QR Code");
                    intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                    intentIntegrator.initiateScan();

                }

            }
        });

        if (isFirstLaunch()) {
            clearAttendeeInfo();
        }
        // Set OnClickListener for viewing profile
        viewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(AttendeeMainActivity.this, AttendeeProfileActivity.class);
            startActivity(intent);
        });
        // Set OnClickListener for events button
        eventsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AttendeeMainActivity.this, AttendeeSelectEvents.class);
            startActivity(intent);
        });

        //String finalDeviceID = deviceID;
        ;
    }
    /** This method is called when startActivityForResult() from another activity finishes
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     */
    //https://www.youtube.com/watch?v=bWEt-_z7BOY
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_CANCELED) {
            super.onActivityResult(requestCode, resultCode, data);
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (intentResult != null) {
                // Handle QR code scan result
                contents = intentResult.getContents();
                Log.d("scanner", contents);
                if (contents != null) {
                    //String inviteQrString = num+ "device id"+deviceID;

                    if (contents.startsWith("promo")) {
                        // Handle promo QR code

                        contents = contents.replace("promo", "");
                        testing.setText(contents);
                        contentsArray = contents.split("device id", 2);

                        //organizer id
                        organizerID = contentsArray[1];
                        eventNum = Long.valueOf(contentsArray[0]);
                        Log.d("scanner", contentsArray[0] + contentsArray[1]);

                        promoData(new PromoDataCallback() {
                            /**Callback for firebase
                             * @param imageData of qrcode image data
                             * @param name of event promo data
                             * @param description of event data
                             */
                            @Override
                            public void onPromoDataCallback(String imageData, String name, String description) {
                                Log.d("qwert", name + description);
                                Intent i = new Intent(AttendeeMainActivity.this, AttendeePromoActivity.class);

                                //starting bundle intent
                                Bundle bundle = new Bundle();
                                bundle.putString("imageData", imageData);
                                bundle.putString("name", name);
                                bundle.putString("description", description);
                                bundle.putString("organizerID", organizerID);
                                bundle.putLong("eventNum", eventNum);
                                i.putExtras(bundle);
                                startActivity(i);
                            }
                        });


                    } else {
                        //split content array
                        contentsArray = contents.split("device id", 2);
                        if (1 == 1) {
                            showLocation();
                            // Handle case where last known location is not available
                            //https://cloud.google.com/firestore/docs/samples/firestore-data-set-array-operations

                            Log.d("scanner", contentsArray[0] + contentsArray[1]);

                            //make bundle to send to maps activity
                            Bundle inviteBundle = new Bundle();
                            inviteBundle.putLong("num", Long.parseLong(contentsArray[0]));
                            inviteBundle.putString("organizerDeviceID", contentsArray[1]);
                            organizerID = contentsArray[1];
                            eventNum = Long.valueOf(contentsArray[0]);
                            //starting events
                            Intent i = new Intent(AttendeeMainActivity.this, AttendeeProfileActivity.class);
                            updateInvite(new InviteCallback() {
                                /** Callback for firebase
                                 * @param attendeeIDList list of attendees
                                 * @param inviteCountList list of inviteCounts
                                 */
                                @Override
                                public void onInviteCallback(List<String> attendeeIDList, List<Long> inviteCountList) {
                                    if (attendeeIDList.contains(deviceID)) {
                                        //getting indexes
                                        int index = attendeeIDList.indexOf(deviceID);
                                        Long value = inviteCountList.get(index) + 1;
                                        inviteCountList.set(index, value);
                                        Log.d("asd","test"+inviteCountList.toString());

                                    } else {
                                        Log.d("asd",inviteCountList.toString());
                                        //adding to invite list
                                        attendeeIDList.add(deviceID);
                                        inviteCountList.add(1L);
                                        Log.d("asd","test1"+inviteCountList.toString());
                                    }
                                    //getting document reference
                                    firestoreHelper.getDeviceDocRef(organizerID).collection("event").document(String.valueOf(eventNum))
                                            .update("attendeeIDList", attendeeIDList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                /** DocumentSnapshot successfully updated
                                                 * @param aVoid successfully get data
                                                 */
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("update", "DocumentSnapshot successfully updated!");
                                                }
                                            })
                                            //add on failure listener
                                            .addOnFailureListener(new OnFailureListener() {
                                                /** DocumentSnapshot not successfully updated
                                                 * @param e error that was caught
                                                 */
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("update", "Error updating document", e);
                                                }
                                            });
                                    //getting document reference
                                    firestoreHelper.getDeviceDocRef(organizerID).collection("event").document(String.valueOf(eventNum))
                                            .update("checkInCountList", inviteCountList)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                /**DocumentSnapshot successfully updated
                                                 * @param aVoid successfully get data
                                                 */
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("update", "DocumentSnapshot successfully updated!");
                                                }
                                            })

                                            .addOnFailureListener(new OnFailureListener() {
                                                /** DocumentSnapshot not successfully updated
                                                 * @param e error that was caught
                                                 */
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("update", "Error updating document", e);
                                                }
                                            });
                                    //gte all events from firebase and update
                                    firestoreHelper.getAllEvent().document(String.valueOf(eventNum))
                                            .update("attendeeIDList", attendeeIDList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                /**DocumentSnapshot successfully updated
                                                 * @param aVoid  successfully get data
                                                 *
                                                 */
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("update", "DocumentSnapshot successfully updated!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                /** DocumentSnapshot not successfully updated

                                                 * @param e error that was caught
                                                 */
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("update", "Error updating document", e);
                                                }
                                            });
                                    //get all events from firebase and update eventnum
                                    firestoreHelper.getAllEvent().document(String.valueOf(eventNum))
                                            .update("checkInCountList", inviteCountList)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                /** DocumentSnapshot successfully updated
                                                 * @param aVoid successfully get data
                                                 */
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("update", "DocumentSnapshot successfully updated!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                /** DocumentSnapshot not successfully updated
                                                 * @param e error that was caught
                                                 */
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("update", "Error updating document", e);
                                                }
                                            });
                                }
                            });

                            if (lastKnownLocation != null) {
                                // Use the last known location
                                //testing using logs

                                Log.i("location1", lastKnownLocation.toString() + "yesss");
                                if(organizerID!=null) {
                                    Log.i("location1.2", lastKnownLocation.toString() + "yesss");
                                    database.saveUserLocation(organizerID);
                                    Log.i("location1.3", lastKnownLocation.toString() + "yesss");
                                }
                                // Do something with latitude and longitude...
                            } else {
                                lastKnownLocation = null;
                            }
                            updateLocation(new CustomLocationCallback() {
                                /** Callback for firebase to update location
                                 * @param locationList Geolocations list of locations
                                 */
                            @Override
                            public void onLocationCallback(List<GeoPoint> locationList) {
                                if(lastKnownLocation!=null){
                                    //https://stackoverflow.com/questions/13781514/correctly-draw-on-google-maps-a-point-which-exceeds-90-degrees-of-latitude
                                    //type casting to be safe
                                    double lat = (double) (lastKnownLocation.getLatitude() );
                                    double lng = (double) (lastKnownLocation.getLongitude());
                                    double[] offsetCoordinates = offsetCoordinates(lat, lng);

                                    //calling helper function
                                    Log.i("offsetCoordinates",String.valueOf(lastKnownLocation.getLatitude()));
                                    Log.i("offsetCoordinates",String.valueOf(offsetCoordinates[0]));

                                    //creating geolocation
                                    GeoPoint point = new GeoPoint(offsetCoordinates[0], offsetCoordinates[1]);
                                    locationList.add(point);

                                database.saveUserLocation(organizerID).document(String.valueOf(eventNum)).update("location", locationList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    /** Saving user Locations
                                     * @param unused for firebase
                                     */
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("location", "success");

                                    }
                                });
                                }
                            }
                       });


                        }

                    }
                }
            }
        }
    }

    /**
     * @param latitude latitude of geolocation
     * @param longitude longitude of geolocation
     * @return static double[] list of coordinates
     */
    public static double[] offsetCoordinates(double latitude, double longitude) {
        // Offset latitude if necessary
        double lat = 0;
        double lng = 0;

        if (latitude > 90) {
            lat = 180 - latitude;
            latitude = lat;
        } else if (latitude < -90) {
            lat = -180 - latitude;
            latitude = lat;
        }

        // Offset longitude if necessary
        if (longitude > 180) {
            lng = longitude - 360;
            longitude =lng;
        } else if (longitude < -180) {
            lng = longitude + 360;
            longitude =lng;
        }

        return new double[]{latitude, longitude};
    }

    /** Accessing location object and do necessary changes
     * @param location the updated location
     */
    @Override
    public void onLocationChanged(Location location) {

        lastKnownLocation=location;
        lastKnownLocation = getLastKnownLocation(location);

        Log.i("LastLocation", "Last Location is not empty.");

        if (lastKnownLocation.hasAltitude()) {
            double altitude = location.getAltitude();
            Log.i("LocationAltitude", "Location has altitude.");
            lastKnownLocation.setAltitude(altitude);
            // Altitude information is available, can use it if needed
            Log.d("LastLocationAltitude", "Altitude: " + altitude + " meters");
        }
        else {
            // Altitude information is not available for this location
            Log.w("LocationAltitudeWarning", "Altitude information is not available for this location");
        }

        // Check if altitude accuracy is available (Note: Altitude accuracy may not be available for the network provider)
        if (lastKnownLocation.hasAccuracy()) {
            // Check if altitude accuracy is set
            Log.i("LastLocationhasAccuracy", "Last known Location has Accuracy");

            if (lastKnownLocation.getVerticalAccuracyMeters()<0) {
                // Altitude accuracy is not set, handle this case gracefully
                Log.i("LastLocationNOVerticalAccuracy", "Last known Location has no Accuracy");
                float altitudeAccuracy = location.getVerticalAccuracyMeters();
                lastKnownLocation.setVerticalAccuracyMeters(altitudeAccuracy);
                Log.d("AltitudeAccuracy", "Altitude accuracy: " + altitudeAccuracy + " meters");
            }
            else {
                // Altitude accuracy is available, you can use it if needed
                float Accuracy =location.getAccuracy();
                lastKnownLocation.setAccuracy(Accuracy);
                Log.i("LastKnownLocationAcurracy ", "Last known Location has no Accuracy ");
                float altitudeAccuracy = location.getVerticalAccuracyMeters();

                Log.i("LastKnownLocationVerticalAccuracy!", "Last known Location Vertical has no Accuracy ");
                lastKnownLocation.setVerticalAccuracyMeters(altitudeAccuracy);
                Log.d("AltitudeAccuracy", "Altitude accuracy: " + altitudeAccuracy + " meters");
            }

        } else {
            // Altitude information is not available for this location
            Log.w("AltitudeWarning", "Altitude information is not available for this location");
        }

    }

    /** Accesing last known Location
     * @param location of last known
     * @return location object of last known location
     */
    public Location getLastKnownLocation(Location location){
        return location;
    }

    /** check whether provider disabled
     * @param provider the name of the location provider
     */
    @Override
    public void onProviderDisabled(String provider) {
    }

    /** check whether provider enabled
     * @param provider the name of the location provider
     */
    @Override
    public void onProviderEnabled(String provider) {
    }

    /** check whether status changed
     * @param provider name of provider
     * @param status int status of location
     * @param extras to send data
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    /** When GPS status changed
     * @param event event number for this notification
     */
    @Override
    public void onGpsStatusChanged(int event) {
    }
    private interface CustomLocationCallback {
        /** Custom location Callback for firebase
         * @param locationList list of geolocations
         */
        void onLocationCallback(List<GeoPoint> locationList);
    }

    /**
     Callback for firebase
     * @param locationCallback location Callback for firebase
     */
    public void updateLocation(CustomLocationCallback locationCallback) {
        if (lastKnownLocation!=null && eventNum!=null) {
            //log to update
            Log.i("updatee", "updateLocation: ");
            //getting location
            database.saveUserLocation(organizerID).document(String.valueOf(eventNum))
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        /** When task is complete
                         * @param task query for database
                         */
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("location", "DocumentSnapshot data: " + document.getData());
                                    locationList = (List<GeoPoint>) document.get("location");
                                    locationCallback.onLocationCallback(locationList);
                                } else {
                                    Log.d("location55", "No such document");
                                }
                            } else {
                                Log.d("location6", "get failed with ", task.getException());
                            }
                        }
                    });
            ;
        }

    }

    private interface PromoDataCallback {
        /** promo data base Callback for firebase
         * @param imageData event promo image data
         * @param name name of event
         * @param description event description
         */
        void onPromoDataCallback(String imageData, String name, String description);
    }
    private interface InviteCallback {
        /** invite data base Callback for firebase
         * @param attendeeIDList attendee IDList
         * @param inviteCountList attendee invite count list
         */
        void onInviteCallback(List<String> attendeeIDList, List<Long> inviteCountList);
    }


    /** updating invite invite call back
     * @param inviteCallback invite call back
     */
    public void updateInvite(InviteCallback inviteCallback) {
        firestoreHelper.getDeviceDocRef(organizerID).collection("event").document(String.valueOf(eventNum)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            /** successful data querying
             * @param task query for database
             */
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // getting document fields
                        Log.d("scan1", "DocumentSnapshot data: " + document.getData());
                        attendeeIDList = (List<String>) document.get("attendeeIDList");
                        checkInCountList = (List<Long>) document.get("checkInCountList");
                        inviteCallback.onInviteCallback(attendeeIDList,checkInCountList);
                    } else {
                        Log.d("scan1", "No such document");
                    }
                } else {
                    Log.d("scan1", "get failed with ", task.getException());
                }
            }
        });
    }


    /** Requesting Permission
     * @param requestCode  The request code passed in link # requestPermissions(
     * android.app.Activity, String[], int)}
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *                     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_LOCATION) {
            //if permission is granted
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showLocation();

            } else {
                //permission not granted
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @SuppressLint({"MissingPermission", "SetTextI18n"})
    private void showLocation() {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //check if gps enabled
            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                //start locating
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
            }
            else {
                // do nothing
            }
    }

    /** Callback for firebase
     * @param promoDataCallback promo dat Callback for firebase
     */
    public void promoData(PromoDataCallback promoDataCallback) {
        firestoreHelper.getDeviceDocRef(contentsArray[1])
                .collection("event")
                .whereEqualTo("eventNum",Long.parseLong(contentsArray[0]))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /** with successful data querying
                     * @param task query for database
                     */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // getting document string
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                imageData = document.getString("eventImageData");
                                name = document.getString("name");
                                description = document.getString("description");
                                Log.d("promo", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("promo", "Error getting documents: ", task.getException());
                        }
                        promoDataCallback.onPromoDataCallback(imageData,name,description);
                    }
                });
    }

    /** first launch of the app.
     * @return boolean isFirstLaunch()
     */
    private boolean isFirstLaunch() {
        // Implement logic to determine if this is the first launch of the app.
        // This could be based on a specific flag in SharedPreferences that you set on launch and clear on exit.
        // For simplicity, this example always returns true, but you should implement this based on your app's lifecycle.
        return true;
    }

    /**
     * clearing attendee info
     */
    private void clearAttendeeInfo() {
        SharedPreferences sharedPrefs = getSharedPreferences("AttendeePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
    }
    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    Toast.makeText(this,"Notification request not granted",Toast.LENGTH_LONG);
                }
            });

    /**
     * Asking Notification Permission
     */
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
//            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            }else {
                // Directly ask for the permission
                if(request==true) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
                else{
                    Toast.makeText(this,"Location request has not been granted.",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
