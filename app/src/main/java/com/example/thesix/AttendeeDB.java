package com.example.thesix;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

public class AttendeeDB {
    /*
     Connects to Firestore AttendeeDB and adds an Attendee Object
    */

    private FirebaseFirestore firestore;

    public AttendeeDB() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void saveAttendeeInfo(String deviceID, Attendee attendee) {
        firestore.collection("AttendeeDB")
                .add(attendee)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));

}
}