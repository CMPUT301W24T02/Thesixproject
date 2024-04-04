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

import java.util.HashMap;
import java.util.Map;

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
     @param : String deviceid , EventDetails eventdetails
     **/
    public void saveInviteQRCode(String deviceID, EventDetails eventdetail,Long num) {
        firestore.collection("inviteQrCodes")
                .document(String.valueOf(num))
                .set(eventdetail)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: "))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));
        firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .collection("event")
                .document(String.valueOf(num))
                .set(eventdetail)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: "))
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

    /**
     Retrieving oldQRef from firebase
     @param : String deviceId
     @return Collectionreference
     **/

    public CollectionReference getOldQrRef(String deviceID) {
        CollectionReference qrRef;
        qrRef = firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .collection("event");
        return qrRef;

    }
    /**
     Retrieving oldQRef from firebase
     @param : String deviceId
     @return Collectionreference
     **/
    public CollectionReference getOldQrRef2(String deviceID) {
        CollectionReference qrRef;
        qrRef = firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .collection("event");
        return qrRef;

    }
    /**
     Retrieving DocumentReference from firebase
     @param : String deviceId
     @return Documentreference
     **/
    public DocumentReference getDeviceDocRef(String deviceID) {
        DocumentReference documentReference;
        documentReference = firestore.collection("OrganizerdevicesDB")
                .document(deviceID);
        return documentReference;
    }
    public void saveDeviceIDToToken(String d_id, String token){
        Map<String, Object> device = new HashMap<>();
        device.put("token",token);
        firestore.collection("deviceIdToToken")
                .document(d_id)
                .set(device)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: "))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));

    }
    public CollectionReference getAllEvent() {
        return firestore.collection("inviteQrCodes");
    }






}
