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

    /**
     * Initializes UI components such as TextView (eventName, eventDescription), ImageView (eventPoster), and Button (backButton)
     * @param : Bundle Saved Instances
     * @return : void
     */

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

        /**
         * Does Event Poster data Callback
         * @param : String String
         * @return : void
         */
        eventPosterImage(new EventPosterCallback() {
            @Override
            public void onEventPosterCallback(String string) {
                Log.d("getevent", "ABC" + string);
                Bitmap b = StringToBitMap(string);
                eventPoster.setImageBitmap(b);
            }
        });

        /**
         * sets back button
         * @param : View v
         * @return : void
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeAllEventDetails.this, AttendeeAllEventsActivity.class));
            }
        });
    }
    /**
     * Does EventPoster Callback
     * @param : String string
     * @return : void
     */
    private interface EventPosterCallback {
        void onEventPosterCallback(String string);
    }
    /**
     * Setting Event Poster Image
     * @param : EventPosterCallback eventPosterCallback
     * @return : void
     */

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

    /**
     * Changing Strig Data to Bitmap
     * @param : String image
     * @return : void
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
