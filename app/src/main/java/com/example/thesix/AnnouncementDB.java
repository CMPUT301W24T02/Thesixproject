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

public class AnnouncementDB {
    /*this class is get announcement data from firebase
    */
    private FirebaseFirestore db;
    private CollectionReference Announcemntref;
    private ArrayList AnnouncemntDataList;

    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        Announcemntref = db.collection("Announcemnt");
        AnnouncemntDataList = new ArrayList<>();
        Announcemntref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                }    return;

                if(querySnapshots != null) {
                    AnnouncemntDataList.clear();
                    for (QueryDocumentSnapshot doc: querySnapshots) {
                        String Announcementid = doc.getId();
                        String information = doc.getString("information");
                        Log.d("Firestore", String.format("Announcement(%s, %s) fetched", Announcementid, information));
                        AnnouncemntDataList.add(new Announcement[Announcementid,information]);
            }
                    AnnouncemntAdapter.notifyDataSetChanged();
        }
    }
    });
    }
}