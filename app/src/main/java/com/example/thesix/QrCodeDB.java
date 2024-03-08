package com.example.thesix;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Contains all functions and methods required to interact with QRCode Firebase
 *
 */

public class QrCodeDB {

    private FirebaseFirestore firestore;

    /**
    Creating instance of firebase
     **/
    public QrCodeDB() {
        firestore = FirebaseFirestore.getInstance();
    }

    /**
    Saving the Invite QR code to database
     **/
    public void saveInviteQRCode(String deviceID, EventDetails eventdetail) {
        firestore.collection("inviteQrCodes")
                .add(eventdetail)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));
        firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .collection("event")
                .add(eventdetail)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));

        firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .update("inviteCount", FieldValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firebase", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firebase", "Error updating document", e);
                    }
                });

    }

    public CollectionReference getOldQrRef(String deviceID) {
        CollectionReference qrRef;
        qrRef = firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .collection("event");
        return qrRef;

    }
    public CollectionReference getOldQrRef2(String deviceID) {
        CollectionReference qrRef;
        qrRef = firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .collection("event");
        return qrRef;

    }
    public DocumentReference getDocRef(String deviceID) {
        DocumentReference documentReference;
        documentReference = firestore.collection("OrganizerdevicesDB")
                .document(deviceID);
        return documentReference;
    }


}
