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
 * AttendeeAllEventsActivity class displays all event names display and navigation within an Android application.
 * Requests necessary permissions and sets up the layout for event listing.
 * Lists all events in the listview created in the app.
 * Retrieves event details asynchronously from a Firebase Firestore database using the readData method.
 * Handles item clicks to navigate to another activity (AdminEventDetails) with selected event details.
 */

public class AttendeeSignedupEventsActivity extends AppCompatActivity {
    private ArrayList<Long> eventNumList;
    private ArrayList<String> eventnameDataList;
    private ArrayAdapter<String> signedupeventnameArrayAdapter;
    private ArrayList<String> signedupEventDataList;
    private List<String> DEVICEID;
    private FirebaseFirestore firestore;
    private Button backButton;
    private CollectionReference eventsRef;

    /**
     * Initializes UI components like lists, adapters, and buttons.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        // Initialize variables
        eventnameDataList = new ArrayList<>();
        eventNumList = new ArrayList<>();
        signedupEventDataList = new ArrayList<>();
        backButton = findViewById(R.id.backButton);
        firestore = FirebaseFirestore.getInstance();
        eventsRef = firestore.collection("inviteQrCodes");
        signedupeventnameArrayAdapter = new ArrayAdapter<String>(
                AttendeeSignedupEventsActivity.this,
                R.layout.event_list_textview, R.id.itemTextView, signedupEventDataList);

        // Set up UI components and adapters
        // Set listeners
        ListView eventList = findViewById(R.id.event_list);
        eventList.setAdapter(signedupeventnameArrayAdapter);

        readData(new MyCallback() {
            /**
             * Handles the callback with the provided list.
             *
             * @param list1 The list of strings received in the callback.
             */
            @Override
            public void onCallback(List<String> list1) {
                Log.d("callback", "3");
                signedupEventDataList = (ArrayList<String>) list1;
                Log.d("callback", "2");
                signedupeventnameArrayAdapter.notifyDataSetChanged();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /** Start Activity to AttendeeSelect Events
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeSignedupEventsActivity.this, AttendeeSelectEvents.class));
            }
        });

    }

    public interface MyCallback {
        /**
         * @param list1 firebase callback
         */
        void onCallback(List<String> list1);
    }

    public void readData1(MyCallback myCallback) {
        eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            /** query for database
             * @param queryDocumentSnapshots with successful data querying
             */
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                    //device ID for document
                    DEVICEID = (List<String>) document.get("signUpIDList");
                    String eventname = document.getString("name");
                    Long eventNum = document.getLong("eventNum");

                    //getting device id
                    String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    for (int i = 0; i < DEVICEID.size(); i++) {
                        if (DEVICEID.get(i).equalsIgnoreCase(deviceID)) {
                            eventNumList.add(eventNum);
                            signedupEventDataList.add(eventname);
                        }
                    }
                }
                myCallback.onCallback(signedupEventDataList);

            }
        });
    }

    /** query for database
     * @param myCallback with successful data querying
     */
    public void readData(MyCallback myCallback) {
        eventsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /** with successful data querying
                     * @param task query for database
                     */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //signed IDuplist
                                DEVICEID = (List<String>) document.get("signUpIDList");
                                Log.d("Arjun", "20");

                                //event document collection
                                String eventname = document.getString("name");
                                Long eventNum = document.getLong("eventNum");
                                //getting string device id
                                String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                for (int i = 0; i < DEVICEID.size(); i++) {
                                    if (DEVICEID.get(i).equalsIgnoreCase(deviceID)) {
                                        eventNumList.add(eventNum);
                                        signedupEventDataList.add(eventname);
                                    }
                                }
                            }
                        }
                        myCallback.onCallback(signedupEventDataList);
                    }

                });
    }
}