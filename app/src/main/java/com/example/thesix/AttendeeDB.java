package com.example.thesix;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * AttendeeDB class facilitates interaction with a Firestore database to manage attendee information.
 * Initializes a FirebaseFirestore instance (firestore) in the constructor using FirebaseFirestore.getInstance().
 * Provides a method saveAttendeeInfo to save attendee information to the Firestore database.
 * Within saveAttendeeInfo, it adds the Attendee object to the "AttendeeDB" collection in Firestore.
 * Logs successful addition of a document with its ID using addOnSuccessListener.
 * Logs any failure in adding the document using addOnFailureListener.
 **/

public class AttendeeDB {

    private FirebaseFirestore firestore;

    public AttendeeDB() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void saveAttendeeInfo(Attendee attendee) {
        firestore.collection("AttendeeDB")
                .add(attendee)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));
}
}
