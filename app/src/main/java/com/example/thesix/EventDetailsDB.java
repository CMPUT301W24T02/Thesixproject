package com.example.thesix;

import android.graphics.Picture;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collection;

public class EventDetailsDB {
    /*this class is get announcement data from firebase
     */
    private FirebaseFirestore db;
    private CollectionReference EventDetailsDBref;
    private ArrayList EventDataList;

    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        EventDetailsDBref = db.collection("Announcemnt");
        EventDataList = new ArrayList<>();
        EventDetailsDBref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                }
                return;

                if (querySnapshots != null) {
                    EventDataList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String Eventid = doc.getId();
                        String Eventdetail  = doc.getString("Eventdetail");
                        Picture EventPicture;
                        Log.d("Firestore", String.format("City(%s, %s) fetched", Eventid, Eventdetail));
                        EventDataList.add(new EventDetails[Eventid, Eventdetail, EventPicture]);
                    }
                    EventListAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}