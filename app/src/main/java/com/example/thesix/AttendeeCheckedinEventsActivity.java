package com.example.thesix;

import static android.app.PendingIntent.getActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AttendeeCheckedinEventsActivity class displays all event names display and navigation within an Android application.
 * Requests necessary permissions and sets up the layout for event listing.
 * Lists all events in the listview created in the app.
 * Retrieves event details asynchronously from a Firebase Firestore database using the readData method.
 * Handles item clicks to navigate to another activity (AdminEventDetails) with selected event details.
 */

public class AttendeeCheckedinEventsActivity extends AppCompatActivity {
    private ArrayList<Long> eventNumList;
    private ArrayList<String> eventnameDataList;
    private ArrayAdapter<String> checkedineventnameArrayAdapter;
    private ArrayList<String> checkedinEventDataList;
    private List<String> DEVICEID;
    private FirebaseFirestore firestore;
    private Button backButton;
    private CollectionReference eventsRef;


    /** Creating Attendee Checked in activity
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        //creating permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        //creating arraylist
        eventnameDataList = new ArrayList<>();
        eventNumList = new ArrayList<>();
        checkedinEventDataList = new ArrayList<>();
        backButton = findViewById(R.id.backButton);
        firestore = FirebaseFirestore.getInstance();

        eventsRef = firestore.collection("inviteQrCodes");
        checkedineventnameArrayAdapter = new ArrayAdapter<String>(
                AttendeeCheckedinEventsActivity.this,
                R.layout.event_list_textview, R.id.itemTextView, checkedinEventDataList);
        //creating listviews
        ListView eventList = findViewById(R.id.event_list);
        eventList.setAdapter(checkedineventnameArrayAdapter);

        readData(new MyCallback() {
            /** Callback for firebase
             * @param list1 for event data callback
             */
            @Override
            public void onCallback(List<String> list1) {
                Log.d("callback", "3");
                checkedinEventDataList = (ArrayList<String>) list1;
                Log.d("callback", "2");
                checkedineventnameArrayAdapter.notifyDataSetChanged();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /**Start Activity to AttendeeSelectEvents
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeCheckedinEventsActivity.this, AttendeeSelectEvents.class));
            }
        });

    }

    /**
     * Interface for callbacks
     */
    public interface MyCallback {
        /** Callback for firebase
         * @param list1 with callback to string
         */
        void onCallback(List<String> list1);
    }

    /** Reading Data 1
     * @param myCallback callback for firebase
     */
    public void readData1(MyCallback myCallback) {
        eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            /**query for database
             * @param queryDocumentSnapshots with successful data querying
             */
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    //personal documents
                    DEVICEID = (List<String>) document.get("signUpIDList");
                    String eventname = document.getString("name");
                    Long eventNum = document.getLong("eventNum");
                    //getting string device id
                    String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    for (int i = 0; i < DEVICEID.size(); i++) {
                        if (DEVICEID.get(i).equalsIgnoreCase(deviceID)) {
                            eventNumList.add(eventNum);
                            checkedinEventDataList.add(eventname);
                        }
                    }
                }
                myCallback.onCallback(checkedinEventDataList);

            }
        });
    }

    /**Reading data in database
     * @param myCallback Callback for firebase
     */
    public void readData(MyCallback myCallback) {
        eventsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /** query for database
                     * @param task with successful data querying
                     */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DEVICEID = (List<String>) document.get("attendeeIDList");
                                Log.d("Arjun", "20");
                                //getting string eventnum
                                String eventname = document.getString("name");
                                Long eventNum = document.getLong("eventNum");

                                //getting string device id
                                String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                for (int i = 0; i < DEVICEID.size(); i++) {
                                    if (DEVICEID.get(i).equalsIgnoreCase(deviceID)) {
                                        eventNumList.add(eventNum);
                                        checkedinEventDataList.add(eventname);
                                    }
                                }
                            }
                        }
                        myCallback.onCallback(checkedinEventDataList);
                    }

                });
    }
}