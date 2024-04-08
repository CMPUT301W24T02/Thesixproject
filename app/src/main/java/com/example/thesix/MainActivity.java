package com.example.thesix;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * MainActivity checks if the user trying to enter the app is organizer or and admin and redirects accordingly..
 **/
public class MainActivity extends AppCompatActivity {
    QrCodeDB firestorehelper;
    List<String> adminList;
    List<String> organizerList;
    /**
     * If user is admin redirect to AdminActivity.
     * if user is organizer redirect to OrganizerMainActivity
     @param savedInstanceState bundle of instance state
     **/
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firestorehelper = new QrCodeDB();
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d(deviceID, "this is device id ");
        Log.d("DeviceId", deviceID);
        adminList = new ArrayList<>();
        organizerList = new ArrayList<>();


        getID(new IDCallback() {
            @Override
            public void onIDCallback(List<String> adminIDList, List<String> organizerIDList) {
                Log.d("asd",adminList.toString());
                Log.d("asd",organizerList.toString());
                if (adminIDList.contains(deviceID)) {
                    Log.d("asd","1");
                    startActivity(new Intent(MainActivity.this, AdminActivity.class));
                }
                else if (organizerIDList.contains(deviceID)) {
                    Log.d("asd","2");
                    startActivity(new Intent(MainActivity.this,OrganizerMainActivity.class));

                }
                else {
                    Log.d("asd","3");
                    startActivity(new Intent(MainActivity.this, AttendeeMainActivity.class));
                }
            }
        });

    }
    private interface IDCallback {
        void onIDCallback(List<String> adminIDList, List<String> organizerIDList);
    }

    public void getID(IDCallback idCallback) {
        firestorehelper.getFireStore().collection("AdmindeviceDB").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                adminList.add(document.getId());
                                Log.d("testing", "admin: "+document.getId());
                                Log.d("qwe",adminList.toString());

                            }


                        } else {
                            Log.d("no", "Error getting documents: ", task.getException());
                        }
                    }
                });
        firestorehelper.getFireStore().collection("OrganizerdevicesDB").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                organizerList.add(document.getId());
                                Log.d("testing","organizer: "+ document.getId());
                                Log.d("qwe",organizerList.toString());
                            }
                            idCallback.onIDCallback(adminList,organizerList);

                        } else {
                            Log.d("no", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }



}
