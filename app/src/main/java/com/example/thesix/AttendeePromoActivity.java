package com.example.thesix;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * AttendeePromoActivity displays promotional information about an event to attendees,
 * including the event name, description, and poster image. Attendees can view event details
 * and sign up for the event using this activity.
 */

public class AttendeePromoActivity extends AppCompatActivity {
    private TextView eventName;
    private TextView eventDescription;
    private ImageView eventPoster;
    private Button backButton;
    private Button signUpButton;
    public QrCodeDB firestoreHelper;
    public Long eventNum;
    public String organizerID;
    List<String> signUpIDList;

    /**
     * Initializes the activity, sets up UI components, retrieves event information from the intent,
     * and displays the event details.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_promo_activity);

        eventName = findViewById(R.id.eventName); // TextView for displaying the event name
        eventDescription = findViewById(R.id.eventDescription); // TextView for displaying the event description
        eventPoster = findViewById(R.id.eventPoster); // ImageView for displaying the event poster image
        backButton = findViewById(R.id.backButton); //Button for navigating back to the main activity
        signUpButton = findViewById(R.id.signUpButton);

        // Firestore database helper
        firestoreHelper = new QrCodeDB();

        // Event information
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String description = bundle.getString("description");
        String imageData = bundle.getString("imageData");

        organizerID = bundle.getString("organizerID");
        eventNum = bundle.getLong("eventNum");
        Log.d("showing",name+description);

        eventName.setText(name);
        eventDescription.setText(description);
        Bitmap bitmap = StringToBitMap(imageData);
        eventPoster.setImageBitmap(bitmap);

        //button to take back to the beginning
        backButton.setOnClickListener(new View.OnClickListener() {
            /** take back to attendee main
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeePromoActivity.this, AttendeeMainActivity.class));
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            /** take to signUp button
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                signUp(new SignUpCallback() {
                    /**
                     * Callback method invoked when the sign-up operation is completed.
                     * This method updates the sign-up list with the device ID if the device
                     * is not already signed up for the event.
                     *
                     * @param signUpIDList The list of device IDs that have signed up for the event.
                     */
                    @Override
                    public void onSignUpCallback(List<String> signUpIDList) {
                        // Get the device ID
                        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        if (signUpIDList.contains(deviceID)) {
                            // Check if the device ID is already in the sign-up list
                        }
                        else {
                            signUpIDList.add(deviceID);
                        }

                        // Update the sign-up list in Firestore
                        firestoreHelper.getDeviceDocRef(organizerID).collection("event").document(String.valueOf(eventNum))
                                .update("signUpIDList", signUpIDList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("update", "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("update", "Error updating document", e);
                                    }
                                });
                    }
                });

            }
        });

    }
    private interface SignUpCallback {

        /**
         * Callback method invoked when the sign-up operation is completed.
         * This method receives the list of device IDs that have signed up for the event.
         * @param signUpIDList list of device id
         */
        void onSignUpCallback(List<String> signUpIDList);
    }
    /**
     * Signs up the current device for the event.
     *
     * @param signUpCallback The callback interface for sign-up operations.
     */
    private void signUp(SignUpCallback signUpCallback) {
        // Retrieve the document reference for the event from Firestore
        firestoreHelper.getDeviceDocRef(organizerID).collection("event")
                .document(String.valueOf(eventNum)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Get the list of device IDs that have signed up for the event
                                signUpIDList = (List<String>) document.get("signUpIDList");
                                signUpCallback.onSignUpCallback(signUpIDList);
                                // Invoke the callback method with the sign-up list
                                Log.d("signup", "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d("signup", "No such document");
                            }
                        } else {
                            Log.d("signup", "get failed with ", task.getException());
                        }
                    }
                });
    }
    /**
     * Converts a Base64 encoded string to a Bitmap image.
     *
     * @param image The Base64 encoded string representing the image.
     * @return The Bitmap image decoded from the Base64 string, or null if decoding fails.
     */
    public Bitmap StringToBitMap(String image){
        try{
            // Decode the Base64 string to byte array
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);

            // Convert the byte array to an InputStream
            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            // Return the decoded Bitmap
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            // Print error message if decoding fails
            e.getMessage();
            return null;
        }
    }


}
