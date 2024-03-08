package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.common.reflection.qual.NewInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 *AttendeeListActivity class manages the display of a list of attendees within an Android application.
 *Initializes UI components such as buttons and a list view in the onCreate method.
 *Utilizes buttons (backButton, mapButton, notificationButton) to navigate to different activities.
 *Temporarily creates an Attendee object (testAttendee) for testing purposes.
 *Creates an array of Attendee objects (attendeeArray) and initializes an ArrayList (dataList) with these objects.
 *Initializes an ArrayAdapter (attendeeAdapter) to act as a communication bridge between the front-end UI and back-end data.
 *Sets the adapter to display the list of attendees (dataList) in the attendeeList ListView.
 *Handles button clicks to navigate to other activities (EventDetailsConnector, MapActivity, NotificationActivity)
 **/

public class AttendeeListActivity extends AppCompatActivity {
    private Button backButton;
    private Button mapButton;
    private Button notificationButton;
    CollectionReference QrRef;
    long eventNum;
    ListView attendeeList;
    AttendeeListAdapter attendeeAdapter;   // acts as a communication bridge between front and back end
    ArrayList<Attendee> dataList;
    List<String> attendeeString;
    List<Integer> checkinCount;

    private QrCodeDB firestoreHelper;
    String deviceID;
    String imageBaseString;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_list_screen);
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        backButton = findViewById(R.id.backButton);
        mapButton = findViewById(R.id.mapButton);
        notificationButton = findViewById(R.id.notificationButton);
        attendeeList = findViewById(R.id.attendee_list_view);
        Log.d("hi", deviceID );
        QrRef = firestoreHelper.getOldQrRef(deviceID);
        Intent mIntent = getIntent();
        eventNum = mIntent.getIntExtra("intVariableName", 0);

        setAttendeeList(new AttendeeCallback() {
            @Override
            public void onAttendeeCallback(List<String> list1, List<Integer> list2) {
                // Create the array to hold invited guests
                // Combine Lists
                for (int i =0; i < list1.size(); i++){
                    for (int j = 0; j < list2.size(); j++) {
                        Attendee attendee = new Attendee(list1.get(i), list2.get(j));
                        dataList.add(attendee);
                    }
                }
                dataList.notify();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeListActivity.this, EventDetailsConnector.class));
                // Find the ListView item from front-end to communicate with
                attendeeAdapter = new AttendeeListAdapter(AttendeeListActivity.this, dataList);

                // Set your adapter to show the List of Attendees
                attendeeList.setAdapter(attendeeAdapter);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeListActivity.this, MapActivity.class));
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeListActivity.this, NotificationActivity.class));
            }
        });
    }

    public void setAttendeeList(AttendeeCallback attendeeCallback) {
        QrRef.whereEqualTo("eventNum",eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                attendeeString = (List<String>) document.get("attendeeList");
                                checkinCount = (List<Integer>) document.get("checkedinCount");

                                Log.d("getevent", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("getevent", "Error getting documents: ", task.getException());
                        }
                        attendeeCallback.onAttendeeCallback(attendeeString, checkinCount);
                    }

                });
    }

    private interface AttendeeCallback{
        void onAttendeeCallback(List<String> list1,List<Integer> list2);
    }


}
