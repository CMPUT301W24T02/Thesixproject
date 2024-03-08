package com.example.thesix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * AdminEventDetails class manages the display of event details and provides options for generating guest lists within an Android application.
 * Initializes UI components such as TextView (eventName, eventDescription), ImageView (eventPoster), and Button (backButton, generateGuestButton) in the onCreate method.
 * Retrieves event details from the intent extras passed from the previous activity (eventName and eventDescription) and sets them to the respective TextViews.
 * Handles button clicks to navigate to other activities (EventDetailsAdapter, AttendeeListActivity) using intents.
 * Upon clicking the backButton, it navigates to the EventDetailsAdapter activity.
 * Upon clicking the generateGuestButton, it navigates to the AttendeeListActivity activity.
 */

public class AdminEventDetails extends AppCompatActivity {

    private TextView eventName;
    private TextView eventDescription;
    private ImageView eventPoster;
    private Button backButton;
    long eventNum;
    CollectionReference QrRef;
    private FirebaseFirestore firestore;
    String deviceID;
    String imageBaseString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_event_details);
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        eventPoster = findViewById(R.id.eventPoster);
        backButton = findViewById(R.id.backButton);
        firestore = FirebaseFirestore.getInstance();
        QrRef = firestore.
                collection("inviteQrCodes");

        Bundle bundle = getIntent().getExtras();
        String eventName1 = bundle.getString("eventName");
        String eventDescription1 = bundle.getString("eventDescription");
        eventNum = bundle.getLong("eventNum");
        eventName.setText(eventName1);
        eventDescription.setText(eventDescription1);
        eventPosterImage(new EventPosterCallback() {
            @Override
            public void onEventPosterCallback(String string) {
                Log.d("getevent", "ABC" + string);
                Bitmap b = StringToBitMap(string);
                eventPoster.setImageBitmap(b);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminEventDetails.this, AdminEventsActivity.class));
            }
        });
    }

    private interface EventPosterCallback {
        void onEventPosterCallback(String string);
    }

    public void eventPosterImage(EventPosterCallback eventPosterCallback) {
        QrRef.whereEqualTo("eventNum", eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                imageBaseString = document.getString("eventImageData");
                                Log.d("getevent", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("getevent", "Error getting documents: ", task.getException());
                        }
                        Log.d("getevent", "ab " + imageBaseString);
                        eventPosterCallback.onEventPosterCallback(imageBaseString);

                    }

                });
    }

    public Bitmap StringToBitMap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);

            InputStream inputStream = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
