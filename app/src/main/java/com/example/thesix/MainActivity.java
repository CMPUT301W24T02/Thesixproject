package com.example.thesix;

import static android.content.ContentValues.TAG;

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
 * MainActivity class serves as the entry point of the application and provides options for users to navigate to different functionalities.
 * Initializes UI components such as Button (generateEventButton, generateViewEventButton, generateShareQRCode) in the onCreate method.
 * Sets click listeners for each button to handle navigation to corresponding activities.
 * Clicking generateEventButton navigates to the OrganizerCreateActivity, allowing users to create new events.
 * Clicking generateViewEventButton navigates to the EventDetailsAdapter activity, enabling users to view existing events.
 * Clicking generateShareQRCode navigates to the ShareQRCodeActivity, where users can share QR codes associated with events.
 **/
public class MainActivity extends AppCompatActivity {
    private String adminId = "c4a3f3f0780278c1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("DeviceId", deviceID);

        if (deviceID.equalsIgnoreCase(adminId)) {
            startActivity(new Intent(MainActivity.this, AdminActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, OrganizerMainActivity.class));
        }
    }

}
