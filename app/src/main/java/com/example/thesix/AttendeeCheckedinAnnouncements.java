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
 * AttendeeCheckedinAnnouncements class displays all the announcements of an event
 * Lists all announcements in the listview created in the app.
 * Retrieves event details asynchronously from a Firebase Firestore database using the readData method.
 */

public class AttendeeCheckedinAnnouncements extends AppCompatActivity {
    private ArrayList<Long> eventNumList;
    private ArrayList<String> eventnameDataList;
    private ArrayAdapter<String> announcementArrayAdapter;
    private ArrayList<String> notificationDataList;
    private List<String> notification;
    private FirebaseFirestore firestore;
    private Button backButton;
    private CollectionReference eventsRef;

    private ArrayList<String> eventdescriptionDataList;


    /**
     * Creating Attendee Checkedinannouncement activity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_list);

        //creating permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        //creating arraylist
        eventnameDataList = new ArrayList<>();
        eventNumList = new ArrayList<>();
        notificationDataList = new ArrayList<>();

        eventdescriptionDataList = new ArrayList<>();
        backButton = findViewById(R.id.backButton);
        firestore = FirebaseFirestore.getInstance();

        eventsRef = firestore.collection("inviteQrCodes");
        announcementArrayAdapter = new ArrayAdapter<String>(
                AttendeeCheckedinAnnouncements.this,
                R.layout.event_list_textview, R.id.itemTextView, notificationDataList);
        //creating listviews
        ListView eventList = findViewById(R.id.announcement_list);
        eventList.setAdapter(announcementArrayAdapter);

        readData(new MyCallback() {
            /** Callback for firebase
             * @param list1 for announcements caallback
             */
            @Override
            public void onCallback(List<String> list1) {
                Log.d("callback", "3");
                notificationDataList = (ArrayList<String>) list1;
                Log.d("callback", "2");
                announcementArrayAdapter.notifyDataSetChanged();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /**Start Activity to AttendeeCheckedinEventDetails
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeCheckedinAnnouncements.this, AttendeeCheckedinEventDetails.class));
            }
        });

    }

    /**
     * Interface for callbacks
     */
    public interface MyCallback {
        /**
         * Callback for firebase
         *
         * @param list1 with callback to string
         */
        void onCallback(List<String> list1);
    }

    /**
     * Reading data in database
     *
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
                                notification = (List<String>) document.get("notificationList");
                                Log.d("Singh", "20" +notification.size());
                                //getting string eventnum

                                String description = document.getString("description");
                                String eventname = document.getString("name");
                                Long eventNum = document.getLong("eventNum");

                                for (int i = 0; i < notification.size(); i++) {
                                    String noti = notification.get(i);
                                    eventNumList.add(eventNum);
                                    notificationDataList.add(noti);
                                    eventdescriptionDataList.add(description);

                                }
                            }
                        }
                        myCallback.onCallback(notificationDataList);
                    }

                });
    }
}