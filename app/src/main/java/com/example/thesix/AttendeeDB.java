package com.example.thesix;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * AttendeeDB class facilitates interaction with a Firestore database to manage attendee information.
 * Initializes a FirebaseFirestore instance (firestore)
 **/

public class AttendeeDB {

    private FirebaseFirestore firestore;

    public AttendeeDB() {
        firestore = FirebaseFirestore.getInstance();
    }

    /**
     * Saves attendee information to Firestore.
     *
     * @param name The name of the attendee.
     * @param contact The contact number of the attendee.
     * @param homePage The home page of the attendee.
     */
    public void saveAttendeeInfo(String name, String contact, String homePage) {
        Map<String, Object> attendee = new HashMap<>();
        attendee.put("name", name);
        attendee.put("contact_number", contact);
        attendee.put("home_page", homePage);

        firestore.collection("AttendeeProfileDB")
                .add(attendee)
                .addOnSuccessListener(documentReference -> Log.d("AttendeeDB", "DocumentSnapshot written with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("AttendeeDB", "Error adding document", e));
    }

    public void saveUserLocation(String deviceID, Location location) {
        firestore.collection("UserLocations")
                .document(deviceID)
                .collection("coordinates")
                .add(location)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));
    }
}
