package com.example.thesix;

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

public class AttendeeListDB {
    /*this class is get announcement data from firebase
     */
    private FirebaseFirestore db;
    private CollectionReference Orangizerref;
    private ArrayList AttendeeDataList;

    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        AttendeeListref = db.collection("Announcemnt");
        AttendeeDataList = new ArrayList<>();
        AttendeeListref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                }
                return;

                if (querySnapshots != null) {
                    AttendeeDataList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String Attendeeid = doc.getId();
                        String Attendeename = doc.getString("Attendeename");
                        Log.d("Firestore", String.format("City(%s, %s) fetched", Attendeeid, Attendeename));
                        AttendeeDataList.add(new Attendee[Attendeeid, Attendeename]);
                    }
                    AttendeeListAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
