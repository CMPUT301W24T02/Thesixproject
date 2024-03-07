package com.example.thesix;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private String adminId = "5b7e97595edb9027";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("DeviceId", deviceID);

        if (deviceID.equalsIgnoreCase(adminId))
        {
            startActivity(new Intent(MainActivity.this, AdminActivity.class));
        }
        else {
            startActivity(new Intent(MainActivity.this, OrganizerMainActivity.class));
        }
    }

}