package com.example.thesix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
 * AttendeeListActivity class manages the display of a list of attendees within an Android application.
 * Initializes UI components such as buttons and a list view in the onCreate method.
 * Utilizes buttons (backButton, mapButton, notificationButton) to navigate to different activities.
 * Temporarily creates an Attendee object (testAttendee) for testing purposes.
 * Creates an array of Attendee objects (attendeeArray) and initializes an ArrayList (dataList) with these objects.
 * Initializes an ArrayAdapter (attendeeAdapter) to act as a communication bridge between the front-end UI and back-end data.
 * Sets the adapter to display the list of attendees (dataList) in the attendeeList ListView.
 * Handles button clicks to navigate to other activities (EventDetailsConnector, MapActivity, NotificationActivity)
 **/

public class AttendeeListActivity extends AppCompatActivity {
    private Button backButton;
    private Button mapButton;
    private Button notificationButton;
    private TextView totalcheckinNumber;
    CollectionReference QrRef,checkedinRef;
    long eventNum;
    ListView attendeeList;
    AttendeeListAdapter attendeeAdapter;   // acts as a communication bridge between front and back end
    ArrayList<Attendee> dataList;
    List<String> attendeeString;
    List<Long> checkinCount;
    private long totalCount;

    private String eventName;

    private QrCodeDB firestoreHelper;
    String deviceID;
    String imageBaseString;
    private OrganizerMainActivity oma = new OrganizerMainActivity();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("hihi", "here1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_list_screen);
        firestoreHelper = new QrCodeDB();
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        backButton = findViewById(R.id.backButton);
        mapButton = findViewById(R.id.mapButton);
        notificationButton = findViewById(R.id.notificationButton);
        attendeeList = findViewById(R.id.attendee_list_view);
        totalcheckinNumber = findViewById(R.id.totalCheckin);
        Log.d("hihi", deviceID);
        QrRef = firestoreHelper.getOldQrRef(deviceID);
        Intent mIntent = getIntent();
        dataList = new ArrayList<>();
        eventNum = mIntent.getLongExtra("eventNum", 0);
        // Find the ListView item from front-end to communicate with
        attendeeAdapter = new AttendeeListAdapter(AttendeeListActivity.this, dataList);

        // Set your adapter to show the List of Attendees
        attendeeList.setAdapter(attendeeAdapter);


        setAttendeeList(new AttendeeCallback() {
            @Override
            public void onAttendeeCallback(List<Attendee> list1) {
                // Create the array to hold invited guests
                // Combine Lists


                attendeeAdapter.notifyDataSetChanged();

                Log.d("here", "here2 " + totalCount);
                String total_count = Long.toString(totalCount);
                totalcheckinNumber.setText(total_count);
                String text = +totalCount + " people have checked into " + eventName;
                if(totalCount == 5 ) {
                    Toast.makeText(AttendeeListActivity.this, text, Toast.LENGTH_SHORT).show();
                }
                if(totalCount == 10 ) {
                    Toast.makeText(AttendeeListActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeListActivity.this, EventDetailsAdapter.class));

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

    private interface AttendeeCallback {
        void onAttendeeCallback(List<Attendee> list1);
    }

    public void setAttendeeList(AttendeeCallback attendeeCallback) {
        Log.d("hihi", "here3: " + eventNum);
        QrRef.whereEqualTo("eventNum", eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("hihi", "here4");
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("hihi", "here5");
                                attendeeString = (List<String>) document.get("attendeeList");
                                checkinCount = (List<Long>) document.get("checkIn");
                                totalCount = (long) document.get("totalCheckIn");
                                eventName = (String) document.get("name");
                                Log.d("hih", "here6 " +totalCount);
                                for (int i = 0; i < attendeeString.size(); i++) {
                                    Attendee attendee = new Attendee(attendeeString.get(i), checkinCount.get(i));
                                    dataList.add(attendee);
                                }
                                Log.d("hihi", document.getId() + " => " + document.getData());
                            }
                            Log.d("hihi", "here6");
                        } else {
                            Log.d("getevent", "Error getting documents: ", task.getException());
                            Log.d("hihi", "here7");
                        }
                        Log.d("hihi", "here8");
                        attendeeCallback.onAttendeeCallback(dataList);
                    }

                });
    }


}
