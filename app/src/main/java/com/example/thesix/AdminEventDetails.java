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
 * AdminEventDetails class manages the display of event details.
 */

public class AdminEventDetails extends AppCompatActivity {

    //Initialize textview for event name , description and Poster
    private TextView eventName;
    private TextView eventDescription;
    private ImageView eventPoster;

    // Initialize Buttons for Backbutton ,
    private Button backButton;
    long eventNum;

    //initializing collection reference Qref
    CollectionReference QrRef;

    //initialize firestore
    private FirebaseFirestore firestore;

    //initialize String ID
    String deviceID;
    String imageBaseString;


    /***
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_event_details);

        //getting user's device id
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //initializing TextView and Buttons
        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        eventPoster = findViewById(R.id.eventPoster);
        backButton = findViewById(R.id.backButton);

        //getting firestore instances
        firestore = FirebaseFirestore.getInstance();
        QrRef = firestore.
                collection("inviteQrCodes");

        //getting a bundle with required information
        Bundle bundle = getIntent().getExtras();
        String eventName1 = bundle.getString("eventName");
        String eventDescription1 = bundle.getString("eventDescription");

        //setting with required information
        eventNum = bundle.getLong("eventNum");
        eventName.setText(eventName1);
        eventDescription.setText(eventDescription1);

        eventPosterImage(new EventPosterCallback() {
            /** Implementation of eventPosterCallback
             * @param string to get QRCodeImageData
             */
            @Override
            public void onEventPosterCallback(String string) {
                //setting event Poster image to Bitmap
                Log.d("getevent", "ABC" + string);
                Bitmap b = StringToBitMap(string);
                eventPoster.setImageBitmap(b);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /** Setting back button onclickListener
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Takes to AdminEventsActivity
                startActivity(new Intent(AdminEventDetails.this, AdminEventsActivity.class));
            }
        });
    }

    /**
     * Interface EventPosterCallback
     */
    private interface EventPosterCallback {
        void onEventPosterCallback(String string);
    }

    /** Sets event poster
     * @param eventPosterCallback to check when retrieval is complete
     */
    public void eventPosterImage(EventPosterCallback eventPosterCallback) {
        //retrieving QRref
        QrRef.whereEqualTo("eventNum", eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    /** Add On Success to firebase
                     * @param task to collect firebase data
                     */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                imageBaseString = document.getString("eventImageData");
                            }
                        }
                        eventPosterCallback.onEventPosterCallback(imageBaseString);

                    }

                });
    }


    /** Converting String of Image to BitMap
     * @param image string of Image
     * @return bitmap version of image
     */
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
