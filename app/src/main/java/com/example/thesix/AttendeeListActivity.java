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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    ArrayList<String> nameList;
    private Button mapButton; //making back buttom
    private Button notificationButton;
    private Button signInListButton;
    private TextView totalcheckinNumber;
    CollectionReference QrRef;
    long eventNum;
    ListView attendeeList;
    AttendeeListAdapter attendeeAdapter;   // acts as a communication bridge between front and back end
    ArrayList<Attendee> dataList;
    List<String> attendeeString;
    List<String> attendeeIDString;
    List<Long> checkinCount;
    private int totalCount;
    CollectionReference attendeeCol;
    private ProgressBar progressBar;

    private String eventName;

    private QrCodeDB firestoreHelper; //QrCodeDB
    String deviceID;
    String imageBaseString;
    String OrganizerdeviceID;
    private int count, abc;
    private OrganizerMainActivity oma = new OrganizerMainActivity();


    /**
     * Initializes UI components such as buttons and a list view in the onCreate method.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_list_screen);
        firestoreHelper = new QrCodeDB();

        //getting string device id
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //String deviceID ="27150c669e8b1dc4";
        backButton = findViewById(R.id.backButton);
        mapButton = findViewById(R.id.mapButton);
        signInListButton = findViewById(R.id.SigninList);
        nameList = new ArrayList<>();
        count = 1;
        abc = 1;

        //finding buttons ids
        notificationButton = findViewById(R.id.notificationButton);
        attendeeList = findViewById(R.id.attendee_list_view);
        totalcheckinNumber = findViewById(R.id.totalCheckin);
        progressBar = findViewById(R.id.progress_Bar);
        QrRef = firestoreHelper.getOldQrRef(deviceID);
        attendeeCol = firestoreHelper.getAttendeeDB();

        Bundle bundle = getIntent().getExtras();
        OrganizerdeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //OrganizerdeviceID = bundle.getString("OrganizerdeviceID");
        eventNum = bundle.getLong("eventNum");

        //Intent mIntent = getIntent();
        //dataList = new ArrayList<>();
        //eventNum = mIntent.getLongExtra("eventNum", 0);
        // Find the ListView item from front-end to communicate with
        dataList = new ArrayList<>();
        attendeeAdapter = new AttendeeListAdapter(AttendeeListActivity.this, dataList);

        // Set your adapter to show the List of Attendees
        attendeeList.setAdapter(attendeeAdapter);

        setAttendeeList(new AttendeeCallback() {
            /** Callback for firebase
             * @param list1 list of attendee
             */
            @Override
            public void onAttendeeCallback(List<Attendee> list1) {
                // Create the array to hold invited guests
                // Combine Lists

                attendeeAdapter.notifyDataSetChanged();
                progressBar.setProgress(totalCount);
                String total_count = totalCount + "/100";
                totalcheckinNumber.setText(total_count);
                /*
                String total_count = Long.toString(totalCount);
                totalcheckinNumber.setText(total_count);
                String text = +totalCount + " people have checked into " + eventName;
                if(totalCount == 5 ) {
                    Toast.makeText(AttendeeListActivity.this, text, Toast.LENGTH_SHORT).show();
                }
                if(totalCount == 10 ) {
                    Toast.makeText(AttendeeListActivity.this, text, Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /**Utilizes buttons (backButton, mapButton, notificationButton) to navigate to different activities.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeListActivity.this, EventDetailsAdapter.class));

            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            /**Utilizes buttons (backButton, mapButton, notificationButton) to navigate to different activities.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //making a bundle and setting required data
                Bundle bundle = new Bundle();
                bundle.putLong("eventNum", eventNum);
                bundle.putString("OrganizerdeviceID", OrganizerdeviceID);
                Intent myIntent = new Intent(AttendeeListActivity.this, MapsActivity.class);
                //creating log id
                Log.d("hihi", "Before ID: " + eventNum);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            /** Utilizes buttons (backButton, mapButton, notificationButton) to navigate to different activities.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("eventNum", eventNum);
                Intent myIntent = new Intent(AttendeeListActivity.this, NotificationActivity.class);
                Log.d("hihi", "Before ID: " + eventNum);
                //myIntent.putExtra("eventNum", eventNum);
                //startActivity(myIntent);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }
        });
        signInListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("eventNum", eventNum);
                Intent myIntent = new Intent(AttendeeListActivity.this, SiginListActivity.class);
                Log.d("hihi", "Before ID: " + eventNum);
                //myIntent.putExtra("eventNum", eventNum);
                //startActivity(myIntent);
                myIntent.putExtras(bundle);
                startActivity(myIntent);

            }
        });
    }

    private interface AttendeeCallback {
        /**
         * Callback Interface to get Attendees
         *
         * @param list1 list of attendees
         */
        void onAttendeeCallback(List<Attendee> list1);
    }

    /**
     * Sets Attendee List
     *
     * @param attendeeCallback Callback for firebase
     */
    public void setAttendeeList(AttendeeCallback attendeeCallback) {
        QrRef.whereEqualTo("eventNum", eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /**with successful data querying
                     * @param task query for database
                     */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        Log.d("attendlist", "1st");
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //querying for firebase
                                //attendeeString = (List<String>) document.get("attendeeList");
                                attendeeIDString = (List<String>) document.get("attendeeIDList");
                                checkinCount = (List<Long>) document.get("checkInCountList");
                                totalCount = attendeeIDString.size();
                                Log.d("Mel", "Size" + totalCount);
                                eventName = (String) document.get("name");
                                //adding data list of attendees
                                for (int i = 0; i < attendeeIDString.size(); i++) {
                                    Log.d("Mel1", "Loop");
                                    String ID = (String) (attendeeIDString.get(i));
                                    Log.d("attend", "Loop" + ID);
                                    DocumentReference docRef = attendeeCol.document(ID);
                                    Log.d("Mel1", "Loop" + docRef);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("attendlist", "successful");
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    String name = (String) document.get("name");
                                                    if (name == null) {
                                                        name = "Guest " + count;
                                                        count += 1;
                                                    }
                                                    Log.d("attendlist", name);
                                                    nameList.add(name);
                                                    abc = 0;
                                                    Attendee attendee;
                                                    attendee = new Attendee(nameList.get(nameList.size()-1), checkinCount.get(nameList.size()-1));

                                                    dataList.add(attendee);

                                                    attendeeAdapter.notifyDataSetChanged();
                                                } else {
                                                    String name = "Guest " + count;
                                                    Log.d("attendlist", name);
                                                    nameList.add(name);
                                                    abc = 0;
                                                    count += 1;
                                                    Attendee attendee;
                                                    attendee = new Attendee(nameList.get(nameList.size()-1), checkinCount.get(nameList.size()-1));
                                                    dataList.add(attendee);

                                                    attendeeAdapter.notifyDataSetChanged();
                                                }
                                            } else {
                                                Log.d("attendlist", "not successful");
                                            }
                                        }
                                    });


                                }
                            }
                        }
                        attendeeCallback.onAttendeeCallback(dataList);
                    }

                });
    }
}

