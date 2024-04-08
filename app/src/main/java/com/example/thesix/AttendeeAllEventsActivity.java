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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
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

public class AttendeeAllEventsActivity extends AppCompatActivity {
    private ArrayList<Long> eventNumList;

    private ArrayList<String> eventnameDataList;
    private ArrayList<String> eventdescriptionDataList;
    private ArrayList<String> eventImageDataList;
    private ArrayAdapter<String> eventnameArrayAdapter;
    private FirebaseFirestore firestore;
    private Button backButton;
    private CollectionReference eventsRef;


    /** Creating AttendeeAllEvents
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        //asking for permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        //creating arraylist
        eventnameDataList = new ArrayList<>();
        eventdescriptionDataList = new ArrayList<>();
        eventNumList = new ArrayList<>();
        eventImageDataList = new ArrayList<>();

        //creating back buttons
        backButton = findViewById(R.id.backButton);
        firestore = FirebaseFirestore.getInstance();
        eventsRef = firestore.collection("inviteQrCodes");
        //event name array adapter
        eventnameArrayAdapter = new ArrayAdapter<String>(
                AttendeeAllEventsActivity.this,
                R.layout.event_list_textview, R.id.itemTextView, eventnameDataList);
        ListView eventdescriptionList = findViewById(R.id.event_list);
        eventdescriptionList.setAdapter(eventnameArrayAdapter);


        readData(new MyCallback() {
            /** Callback of public event void
             * @param list1 of string event data list1
             * @param list2 of string event num list2
             * @param list3 of string event num list3
             */
            @Override
            public void onCallback(List<String> list1, List<Long> list2, List<String> list3) {
                Log.d("callback", "3");
                //casting array lists
                eventnameDataList = (ArrayList<String>) list1;
                eventNumList = (ArrayList<Long>) list2;
                Log.d("callback", "2");
                eventnameArrayAdapter.notifyDataSetChanged();
            }
        });

        eventdescriptionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /** On clicking Item of eventNum
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //creating intent
                Intent i = new Intent(AttendeeAllEventsActivity.this, AttendeeAllEventDetails.class);
                String eventName = (String) (eventdescriptionList.getItemAtPosition(position));
                String eventDescription = "Singh";
                //running through list
                long eventNum = eventNumList.get(position);
                for (int j = 0; j < eventnameDataList.size(); j++) {
                    String eventName1 = (String) (eventnameDataList.get(j));
                    if(eventName.equalsIgnoreCase(eventName1))
                    {
                        eventDescription = (String) (eventdescriptionDataList.get(j));
                    }
                }
                //creating bundle of data
                Bundle bundle = new Bundle();
                bundle.putString("eventName", eventName);
                bundle.putString("eventDescription", eventDescription);
                bundle.putLong("eventNum", eventNum);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /** Starting AttendeeSelectEvents
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeAllEventsActivity.this, AttendeeSelectEvents.class));
            }
        });

    }

    /**
     * Interface for callbacks
     */

    public interface MyCallback {
        /** callback for firebase
         * @param list1 list for event callback
         * @param list2 list for event callback
         * @param list3 list for event callback
         */
        void onCallback(List<String> list1, List<Long> list2, List<String> list3);
    }

    /** Callback with events
     * @param myCallback with callback for events
     */
    public void readData(MyCallback myCallback) {
        eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            /** querying for firebase
             * @param queryDocumentSnapshots with comepleted firebase data
             */
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    // getting data from firebase
                    String description = document.getString("description");
                    String eventname = document.getString("name");
                    Long eventNum = document.getLong("eventNum");
                    String base64String = document.getString("qrImageData");
                    //adding to firebase
                    eventImageDataList.add(base64String);
                    eventNumList.add(eventNum);
                    eventnameDataList.add(eventname);
                    eventdescriptionDataList.add(description);
                    Log.d("list", document.getId() + "=>" + document.getData());
                    Log.d("list", eventnameDataList.get(0));
                }
                myCallback.onCallback(eventnameDataList, eventNumList, eventImageDataList);

            }
        });
    }
}