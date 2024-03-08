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
 * QrCodeDB is a class responsible for managing QR codes in Firestore database.
 * @author David Lee
 */
public class QrCodeDB {


    private FirebaseFirestore firestore;

    public QrCodeDB() {
        firestore = FirebaseFirestore.getInstance();
    }
    /**
     * Saves an invite QR code to Firestore database along with updating invite count for the device.
     *
     * @param deviceID      The ID of the device associated with the QR code.
     * @param inviteQrCode  The invite QR code object to be saved.
     */
    public void saveInviteQRCode(String deviceID, MyQRCode inviteQrCode) {
        firestore.collection("inviteQrCodes")
                .add(inviteQrCode)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));
        firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .collection("inviteQrCodes")
                .add(inviteQrCode)
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
    /**
     * Saves a promo QR code to Firestore database.
     *
     * @param deviceID      The ID of the device associated with the QR code.
     * @param promoQrCode   The promo QR code object to be saved.
     */
    public void savePromoQRCode(String deviceID, MyQRCode promoQrCode) {
        firestore.collection("promoQrCodes")
                .add(promoQrCode)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));
        firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .collection("promoQrCodes")
                .add(promoQrCode)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));
    }
    /**
     * Retrieves the reference to the collection of old QR codes associated with a device.
     *
     * @param deviceID      The ID of the device.
     * @return              The reference to the collection of old QR codes.
     */
    public CollectionReference getOldQrRef(String deviceID) {
        CollectionReference qrRef;
        qrRef = firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .collection("inviteQrCodes");
        return qrRef;

    }
    /**
     * Retrieves the reference to the document associated with a device.
     *
     * @param deviceID      The ID of the device.
     * @return              The reference to the document associated with the device.
     */
    public DocumentReference getDocRef(String deviceID) {
        DocumentReference documentReference;
        documentReference = firestore.collection("OrganizerdevicesDB")
                .document(deviceID);
        return documentReference;
    }


}
