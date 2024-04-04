package com.example.thesix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Looper;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;
import android.provider.Settings;

import java.util.List;


public class AttendeeMainActivity extends AppCompatActivity implements IbaseGpsListener{
    private static final int PERMISSION_LOCATION =1000;
    private Button scanButton;
    private Button viewProfile;
    private Button eventsButton;
    TextView testing;
    private QrCodeDB firestoreHelper;
    String contents;
    String[] contentsArray;
    String imageData,name,description;
    //private Button getLocation;
    //private TextView coordinates;

    private AttendeeDB database;
    private Location lastKnownLocation; // To store the latest location
    private String deviceID;
    private String finalDeviceID;
    private String organizerID;
    private Long eventNum;
    private List<Long> checkInCountList;
    private List<String> attendeeIDList;
    private List<Location> locationList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_main_activity);
        scanButton = findViewById(R.id.scanButton);
        viewProfile = findViewById(R.id.viewAttendeeProfile);
        eventsButton = findViewById(R.id.allEventButton);
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //getLocation = findViewById(R.id.locationButton);
        //coordinates = findViewById(R.id.locationinfo);
        database = new AttendeeDB();
        //ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        //testing = findViewById(R.id.textView);
        firestoreHelper = new QrCodeDB();

        //getting deviceID


        // Hi, I changed this part of code due to the failure of switch activity in the view profile button
        //Bundle bundle = getIntent().getExtras();
        //String deviceID = bundle.getString("deviceID");
        Bundle bundle = getIntent().getExtras();
        String deviceID = null;
        if (bundle != null) {
            deviceID = bundle.getString("deviceID");
        }
        finalDeviceID = deviceID;
        askNotificationPermission();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://www.youtube.com/watch?v=bWEt-_z7BOY
                IntentIntegrator intentIntegrator = new IntentIntegrator(AttendeeMainActivity.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan a QR Code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
                //TODO : remove the code here later
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                }
                else {
                    showLocation();
                    // Handle case where last known location is not available
                    if (lastKnownLocation != null) {
                        // Use the last known location
                        // hi, I changed this part due to the failure of switch activity in view profile button
                        //database.saveUserLocation(deviceID,lastKnownLocation);
                        database.saveUserLocation(finalDeviceID);
                        //Log.i("location1", lastKnownLocation.toString()+"yesss");
                        // Do something with latitude and longitude...
                    } else {

                    }

                }



            }
        });

        if (isFirstLaunch()) {
            clearAttendeeInfo();
        }

        viewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(AttendeeMainActivity.this, AttendeeProfileActivity.class);
            startActivity(intent);
        });

        eventsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AttendeeMainActivity.this, AttendeeSelectEvents.class);
            startActivity(intent);
        });

        //String finalDeviceID = deviceID;
        /*
        getLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //https://www.youtube.com/watch?v=UJwj5ywkcks&t=29s&ab_channel=TihomirRadev
                //checking Location permissions
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                }
                else {
                    showLocation();
                    // Handle case where last known location is not available
                    if (lastKnownLocation != null) {
                        // Use the last known location

                        // hi, I changed this part due to the failure of switch activity in view profile button
                        //database.saveUserLocation(deviceID,lastKnownLocation);
                        database.saveUserLocation(finalDeviceID,lastKnownLocation);
                        // Do something with latitude and longitude...
                    } else {

                    }

                }

            }
        }); */
    }


    //https://www.youtube.com/watch?v=bWEt-_z7BOY
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            contents = intentResult.getContents();
            Log.d("scanner", contents);
            if (contents != null) {
                //String inviteQrString = num+ "device id"+deviceID;

                if (contents.startsWith("promo")) {
                    contents = contents.replace("promo", "");
                    testing.setText(contents);
                    contentsArray = contents.split("device id", 2);
                    organizerID = contentsArray[1];
                    eventNum = Long.valueOf(contentsArray[0]);
                    Log.d("scanner", contentsArray[0] + contentsArray[1]);
                    promoData(new PromoDataCallback() {
                        @Override
                        public void onPromoDataCallback(String imageData, String name, String description) {
                            Log.d("qwert", name + description);
                            Intent i = new Intent(AttendeeMainActivity.this, AttendeePromoActivity.class);
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
                    contentsArray = contents.split("device id", 2);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                            && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                    } else {
                        showLocation();
                        // Handle case where last known location is not available
                        if (lastKnownLocation != null) {
                            // Use the last known location
                            // hi, I changed this part due to the failure of switch activity in view profile button
                            //database.saveUserLocation(deviceID,lastKnownLocation);
                            database.saveUserLocation(organizerID);
                            //Log.i("location1", lastKnownLocation.toString()+"yesss");
                            // Do something with latitude and longitude...
                        } else {

                        }
                        //https://cloud.google.com/firestore/docs/samples/firestore-data-set-array-operations

                        Log.d("scanner", contentsArray[0] + contentsArray[1]);
                        Bundle inviteBundle = new Bundle();
                        inviteBundle.putLong("num", Long.parseLong(contentsArray[0]));
                        inviteBundle.putString("organizerDeviceID", contentsArray[1]);
                        organizerID = contentsArray[1];
                        eventNum = Long.valueOf(contentsArray[0]);
                        Intent i = new Intent(AttendeeMainActivity.this, AttendeeProfileActivity.class);
                        updateInvite(new InviteCallback() {
                            @Override
                            public void onInviteCallback(List<String> attendeeIDList, List<Long> inviteCountList) {
                                if (attendeeIDList.contains(deviceID)) {
                                    int index = attendeeIDList.indexOf(deviceID);
                                    Long value = inviteCountList.get(index) + 1;
                                    inviteCountList.set(index, value);
                                } else {
                                    attendeeIDList.add(deviceID);
                                    inviteCountList.add(0L);
                                }
                                firestoreHelper.getDeviceDocRef(organizerID).collection("event").document(String.valueOf(eventNum))
                                        .update("attendeeIDList", attendeeIDList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("update", "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("update", "Error updating document", e);
                                            }
                                        });
                                firestoreHelper.getDeviceDocRef(organizerID).collection("event").document(String.valueOf(eventNum))
                                        .update("inviteCountList", inviteCountList)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("update", "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("update", "Error updating document", e);
                                            }
                                        });
                            }
                        });
                        updateLocation(new LocationCallback() {
                            @Override
                            public void onLocationCallback(List<Location> locationList) {
                                locationList.add(lastKnownLocation);
                                database.saveUserLocation(organizerID).document(String.valueOf(eventNum)).update("location",locationList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("location","success");
                                    }
                                });
                            }
                        });
                        i.putExtras(inviteBundle);
                        startActivity(i);


                    }

                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastKnownLocation = location;
        //coordinates.setText(hereLocation(location));

    }


    private String hereLocation(Location location) {
        //database.saveUserLocation(deviceID,location);
        return "Lat:" + location.getLatitude() + "Long" + location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onGpsStatusChanged(int event) {
    }
    private interface LocationCallback {
        void onLocationCallback(List<Location> locationList);
    }
    public void updateLocation(LocationCallback locationCallback) {
        database.saveUserLocation(organizerID).document(String.valueOf(eventNum))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("location", "DocumentSnapshot data: " + document.getData());
                                locationList = (List<Location>) document.get("location");
                                locationCallback.onLocationCallback(locationList);
                            } else {
                                Log.d("location", "No such document");
                            }
                        } else {
                            Log.d("location", "get failed with ", task.getException());
                        }
                    }
                });;

    }

    private interface PromoDataCallback {
        void onPromoDataCallback(String imageData, String name, String description);
    }
    private interface InviteCallback {
        void onInviteCallback(List<String> attendeeIDList, List<Long> inviteCountList);
    }
    public void updateInvite(InviteCallback inviteCallback) {
        firestoreHelper.getDeviceDocRef(organizerID).collection("event").document(String.valueOf(eventNum)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
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



    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showLocation();
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @SuppressLint({"MissingPermission", "SetTextI18n"})
    private void showLocation() {
        LocationManager locationManager =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //check if gps enabled
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //start locating
            //coordinates.setText("Loading location");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        }
        else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    public void promoData(PromoDataCallback promoDataCallback) {
        firestoreHelper.getDeviceDocRef(contentsArray[1])
                .collection("event")
                .whereEqualTo("eventNum",Long.parseLong(contentsArray[0]))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
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

    private boolean isFirstLaunch() {
        // Implement logic to determine if this is the first launch of the app.
        // This could be based on a specific flag in SharedPreferences that you set on launch and clear on exit.
        // For simplicity, this example always returns true, but you should implement this based on your app's lifecycle.
        return true;
    }

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
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

}
