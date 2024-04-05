package com.example.thesix;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * MainActivity checks if the user trying to enter the app is organizer or and admin and redirects accordingly..
 **/
public class MainActivity extends AppCompatActivity {
    private String adminId = "713972a529878c33";//change this
    /**
     * If user is admin redirect to AdminActivity.
     * if user is organizer redirect to OrganizerMainActivity
     @param : Bundle savedInstanceState
     @return
     **/
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //deviceID ="27150c669e8b1dc4";
        Log.d(deviceID, "this is device id ");
        Log.d("DeviceId", deviceID);

        //adminId = deviceID;
        //27150c669e8b1dc4

        if (deviceID.equalsIgnoreCase(adminId)) {
            startActivity(new Intent(MainActivity.this, AdminActivity.class));
        }
         else {
            //sending device id to attendeeactivitypage
            //startActivity(new Intent(MainActivity.this, OrganizerMainActivity.class));
            //Intent intent =new Intent(MainActivity.this,OrganizerMainActivity.class);
            //intent.putExtra("deviceID",deviceID);
            //startActivity(intent);
            startActivity(new Intent(MainActivity.this, AttendeeMainActivity.class));

            //startActivity(new Intent(MainActivity.this, OrganizerMainActivity.class));
            //startActivity(new Intent(MainActivity.this, AdminActivity.class));
            //startActivity(new Intent(MainActivity.this, AttendeeProfileActivity.class));
            //startActivity(new Intent(MainActivity.this, AttendeeMainActivity.class));
        }
    }



}
