package com.example.thesix;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;

public class Firebase {

    private FirebaseFirestore firestore;

    public Firebase() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void saveQRCode(MyQRCode qrCode) {
        firestore.collection("qrcodes")
                .add(qrCode)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));
    }
}
