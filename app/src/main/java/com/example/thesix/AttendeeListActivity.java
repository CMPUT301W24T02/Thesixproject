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
 * Handles button clicks to navigate to other activities (EventDetailsConnector, MapActivity, NotificationActivity)
 **/

public class AttendeeListActivity extends AppCompatActivity {
    private Button backButton;
    private Button mapButton;
    private Button notificationButton;
    private TextView totalcheckinNumber;
    CollectionReference QrRef;
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

    /**
     * Initializes UI components such as buttons and a list view in the onCreate method.
     * @param : @Nullable Bundle savedInstanceState
     * @return : void
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_list_screen);
        firestoreHelper = new QrCodeDB();
        //deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceID ="27150c669e8b1dc4";
        backButton = findViewById(R.id.backButton);
        mapButton = findViewById(R.id.mapButton);
        notificationButton = findViewById(R.id.notificationButton);
        attendeeList = findViewById(R.id.attendee_list_view);
        totalcheckinNumber = findViewById(R.id.totalCheckin);
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


        /**
         * Utilizes buttons (backButton, mapButton, notificationButton) to navigate to different activities.
         * @param :int position, @Nullable View convertView, @NonNull ViewGroup parent
         * @return : View
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeListActivity.this, EventDetailsAdapter.class));

            }
        });
        /*
        /**
         * Utilizes buttons (backButton, mapButton, notificationButton) to navigate to different activities.
         * @param : View
         * @return :
         */

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeListActivity.this, MapsActivity.class));
            }
        });

        /**
         * Utilizes buttons (backButton, mapButton, notificationButton) to navigate to different activities.
         * @param :View
         * @return :
         */

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeListActivity.this, NotificationActivity.class));
            }
        });
    }
    /**
     Callback Interface to get Attendees
     @param : ist<Attendee> list1
     @return
     **/

    private interface AttendeeCallback {
        void onAttendeeCallback(List<Attendee> list1);
    }
    /**
     * Sets Attendee List
     * @param :AttendeeCallback attendeeCallback
     * @return : void
     */

    public void setAttendeeList(AttendeeCallback attendeeCallback) {
        QrRef.whereEqualTo("eventNum", eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                attendeeString = (List<String>) document.get("attendeeList");
                                checkinCount = (List<Long>) document.get("checkIn");
                                totalCount = (long) document.get("totalCheckIn");
                                eventName = (String) document.get("name");
                                for (int i = 0; i < attendeeString.size(); i++) {
                                    Attendee attendee = new Attendee(attendeeString.get(i), checkinCount.get(i));
                                    dataList.add(attendee);
                                }
                            }
                        }
                        attendeeCallback.onAttendeeCallback(dataList);
                    }

                });
    }


}
