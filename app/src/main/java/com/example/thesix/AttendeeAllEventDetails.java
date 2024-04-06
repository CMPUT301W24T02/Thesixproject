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
 * AttendeeAllEventDetails class manages the display of event details.
 */

public class AttendeeAllEventDetails extends AppCompatActivity {

    private TextView eventName;
    private TextView eventDescription;
    private ImageView eventPoster;
    private Button backButton;
    long eventNum;
    CollectionReference QrRef;
    private FirebaseFirestore firestore;
    String deviceID;
    String imageBaseString;


    /** Creating AttendeeAllEventDetails
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_event_details);
        //getting deviceID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        //finding Id's of buttons
        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        eventPoster = findViewById(R.id.eventPoster);
        backButton = findViewById(R.id.backButton);

        //getting instance of firestore
        firestore = FirebaseFirestore.getInstance();
        QrRef = firestore.
                collection("inviteQrCodes");

        //bundle to send data
        Bundle bundle = getIntent().getExtras();
        String eventName1 = bundle.getString("eventName");
        String eventDescription1 = bundle.getString("eventDescription");
        eventNum = bundle.getLong("eventNum");
        eventName.setText(eventName1);
        eventDescription.setText(eventDescription1);


        eventPosterImage(new EventPosterCallback() {
            /** eventposter calling him out
             * @param string of events
             */
            @Override
            public void onEventPosterCallback(String string) {
                Log.d("getevent", "ABC" + string);
                Bitmap b = StringToBitMap(string);
                eventPoster.setImageBitmap(b);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /** Start activity to AttendeeAllEventsActivity
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeAllEventDetails.this, AttendeeAllEventsActivity.class));
            }
        });
    }

    private interface EventPosterCallback {
        /** On event poster Callback
         * @param string to string
         */
        void onEventPosterCallback(String string);
    }

    /**getting event poster image
     * @param eventPosterCallback with firestore data
     */
    public void eventPosterImage(EventPosterCallback eventPosterCallback) {
        QrRef.whereEqualTo("eventNum", eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    /**On completing firebase data
                     * @param task to query data
                     */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //getting document string
                                imageBaseString = document.getString("eventImageData");
                                Log.d("getevent", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("getevent", "Error getting documents: ", task.getException());
                        }
                        //getting event poster call back
                        Log.d("getevent", "ab " + imageBaseString);
                        eventPosterCallback.onEventPosterCallback(imageBaseString);

                    }

                });
    }


    /** string of bitmap data
     * @param image of string data
     * @return bitmap of image
     */
    public Bitmap StringToBitMap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);

            //getting input stream
            InputStream inputStream = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
