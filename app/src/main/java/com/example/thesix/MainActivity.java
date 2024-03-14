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
 * MainActivity checks if the user trying to enter the app is organizer or and admin and redirects accordingly..
 **/
public class MainActivity extends AppCompatActivity {
    private String adminId = "c4a3f3f0780278c1";//change this

    /**
     * If user is admin redirect to AdminActivity.
     * if user is organizer redirect to OrganizerMainActivity
     @param : Bundle savedInstanceState
     @return
     **/
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
