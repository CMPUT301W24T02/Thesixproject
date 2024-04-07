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


    /**  Saving the Invite QR code to database
     * @param deviceID device ID of whoever
     * @param eventdetail event detail object
     * @param num number of event
     */
    public void saveInviteQRCode(String deviceID, EventDetails eventdetail,Long num) {
        firestore.collection("inviteQrCodes")
                .document(String.valueOf(num))
                .set(eventdetail)
                // Document REFERENCE ADDEDED
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: "))
                // Document REFERENCE added wrong
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));
        firestore.collection("OrganizerdevicesDB")
                //collecting from organizer ID
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
                    /** DocumentSnapshot successfully updated
                     * @param aVoid successfully get data
                     */
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firebase", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**DocumentSnapshot not successfully updated
                     * @param e error that was caught
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firebase", "Error updating document", e);
                    }
                });

    }


    /** Retrieving oldQRef from firebase
     * @param deviceID  of whoever trying to get old qr
     * @return Collection from firebase
     */
    public CollectionReference getOldQrRef(String deviceID) {
        CollectionReference qrRef;
        qrRef = firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .collection("event");
        return qrRef;

    }
    /** Retrieving oldQRef from firebase
     * @param deviceID  of whoever trying to get old qr
     * @return Collection from firebase
     */

    public CollectionReference getOldQrRef2(String deviceID) {
        CollectionReference qrRef;
        //querying from firestore
        qrRef = firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .collection("event");
        return qrRef;

    }

    /** Retrieving device collection reference from firebase
     * @param deviceID of whoever trying to get device collection reference from firebase
     * @return Collection from firebase
     */
    public CollectionReference getDeviceColRef( String deviceID) {
        return firestore.collection("OrganizerdevicesDB").document(deviceID).collection("event");
    }

    /** Retrieving device document reference from firebase
     * @param deviceID   whoever trying to get device collection reference from firebase
     * @return document from firebase
     */
    public DocumentReference getDeviceDocRef(String deviceID) {
        DocumentReference documentReference;
        documentReference = firestore.collection("OrganizerdevicesDB")
                .document(deviceID);
        return documentReference;
    }

    /** saving device id to token
     * @param d_id string device id
     * @param token token id assosciated to device
     */
    public void saveDeviceIDToToken(String d_id, String token){
        // creating new hashmap
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

    /** Retrieving token collection reference from firebase
     * @return Collection Reference
     */
    public CollectionReference getTokenRef() {
        CollectionReference TokenRef;
        //retrieving from firestore collection
        TokenRef = firestore.collection("deviceIdToToken");
        return TokenRef;
    }
    /** Retrieving all events collection reference from firebase
     * @return Collection Reference of all events
     */
    public CollectionReference getAllEvent() {
        return firestore.collection("inviteQrCodes");
    }


}
