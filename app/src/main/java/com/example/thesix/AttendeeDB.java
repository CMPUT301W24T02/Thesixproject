package com.example.thesix;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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
     * Saves attendee information
     * @param : Attendee attendee
     * @return : void
     */

    public void saveAttendeeInfo(Attendee attendee) {
        firestore.collection("AttendeeDB")
                .add(attendee)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));
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
