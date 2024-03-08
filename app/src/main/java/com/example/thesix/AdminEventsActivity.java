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
 *AdminEventsActivity class manages event details display and navigation within an Android application.
 * Initializes UI components like lists, adapters, and buttons in the onCreate method.
 * Requests necessary permissions and sets up the layout for event listing.
 * Retrieves event details asynchronously from a Firebase Firestore database using the readData method.
 * Handles item clicks to navigate to another activity (EventDetailsConnector) with selected event details.
 * Provides a method (saveImageExternal) to save Bitmap images as PNG files in the app's external storage directory.
 */

public class AdminEventsActivity extends AppCompatActivity {
    ArrayList<Long> eventNumList;

    ArrayList<String> eventnameDataList;
    ArrayList<String> eventdescriptionDataList;
    ArrayList<String> eventImageDataList;
    private ArrayAdapter<String> eventnameArrayAdapter;
    private FirebaseFirestore firestore;
    private Button backButton;
    CollectionReference eventsRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        eventnameDataList = new ArrayList<>();
        eventdescriptionDataList = new ArrayList<>();
        eventNumList = new ArrayList<>();
        eventImageDataList = new ArrayList<>();
        backButton = findViewById(R.id.backButton);
        firestore = FirebaseFirestore.getInstance();
        eventsRef = firestore.collection("inviteQrCodes");
        eventnameArrayAdapter = new ArrayAdapter<String>(
                AdminEventsActivity.this,
                R.layout.event_list_textview, R.id.itemTextView, eventnameDataList);
        ListView eventdescriptionList = findViewById(R.id.event_list);
        eventdescriptionList.setAdapter(eventnameArrayAdapter);
        readData(new MyCallback() {
            @Override
            public void onCallback(List<String> list1, List<Long> list2, List<String> list3) {
                Log.d("callback", "3");
                eventnameDataList = (ArrayList<String>) list1;
                eventNumList = (ArrayList<Long>) list2;
                Log.d("callback", "1" + eventnameDataList.get(0));
                Log.d("callback", "2");
                eventnameArrayAdapter.notifyDataSetChanged();
            }
        });
        eventdescriptionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(AdminEventsActivity.this, EventDetailsConnector.class);
                String eventName = (String) (eventdescriptionList.getItemAtPosition(position));
                String eventDescription = "Singh";
                long eventNum = eventNumList.get(position);
                for (int j = 0; j < eventnameDataList.size(); j++) {
                    String eventName1 = (String) (eventnameDataList.get(j));
                    if(eventName.equalsIgnoreCase(eventName1))
                    {
                        eventDescription = (String) (eventdescriptionDataList.get(j));
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putString("eventName", eventName);
                bundle.putString("eventDescription", eventDescription);
                bundle.putLong("eventNum", eventNum);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminEventsActivity.this, AdminActivity.class));
            }
        });

    }

    public interface MyCallback {
        void onCallback(List<String> list1, List<Long> list2, List<String> list3);
    }

    public void readData(MyCallback myCallback) {
        eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String description = document.getString("description");
                    String eventname = document.getString("name");
                    Long eventNum = document.getLong("eventNum");
                    String base64String = document.getString("qrImageData");
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