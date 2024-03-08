package com.example.thesix;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
/**
 * AttendeeDB is a class responsible for managing attendee information in the Firestore database.
 * It provides methods for saving attendee information to the database.
 * @Author Danielle
 */
public class AttendeeDB {


    private FirebaseFirestore firestore;
    /**
     * Constructs a new AttendeeDB object and initializes the instance of FirebaseFirestore.
     */
    public AttendeeDB() {
        firestore = FirebaseFirestore.getInstance();
    }
    /**
     * Saves the attendee information to the Firestore database.
     *
     * @param attendee The Attendee object containing the information to be saved.
     */
    public void saveAttendeeInfo(Attendee attendee) {
        firestore.collection("AttendeeDB")
                .add(attendee)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));
}
}
