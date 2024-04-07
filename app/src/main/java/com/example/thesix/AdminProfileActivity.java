package com.example.thesix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * AdminProfileActivity class manages all attendee profile's display and navigation within an Android application.
 * Requests necessary permissions and sets up the layout for Profile listings.
 * Lists all profiles in the listview created in the app.
 *
 */
public class AdminProfileActivity extends AppCompatActivity {
    //creating buttons
    private Button back2AdminButton;
    private ArrayList<Attendee> profileDataList;
    private FirebaseFirestore firestore;
    private Button backButton;
    //colection reference data
    private CollectionReference profileRef;
    private AdminProfileListAdapter imagesArrayAdapter;
    //string data
    private String attendeeName;
    private String contact;
    private String homePage;
    private String profile_image;

    /** Creating data for admin Profile
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_screen);
        //finding button
        back2AdminButton = findViewById(R.id.backButton);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        // new profile data list
        profileDataList = new ArrayList<>();
        backButton = findViewById(R.id.backButton);
        firestore = FirebaseFirestore.getInstance();
        //getting profile reference
        profileRef = firestore.collection("AttendeeProfileDB");
        imagesArrayAdapter = new AdminProfileListAdapter(AdminProfileActivity.this, profileDataList);
        // image list of Listview
        ListView imageList = findViewById(R.id.profile_list_view);
        imageList.setAdapter(imagesArrayAdapter);

        // Call readData method to populate profileDataList
        readData(new MyCallback() {
            /** on callback from firestore
             * @param list1 for imageArray Adapter
             */
            @Override
            public void onCallback(List<Attendee> list1) {
                // Update UI with populated data
                imagesArrayAdapter.notifyDataSetChanged();
            }
        });

        imageList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /** On Item Long click
             * @param parent   The AbsListView where the click happened
             * @param view     The view within the AbsListView that was clicked
             * @param position The position of the view in the list
             * @param id       The row id of the item that was clicked
             * @return with boolean
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //accessing collection
                firestore.collection("AttendeeProfileDB")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            /**on successfully getting data
                             * @param queryDocumentSnapshots with acquired data
                             */
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    String documentId = queryDocumentSnapshots.getDocuments().get(position).getId();

                                    // Update doc in Firestore
                                    firestore.collection("AttendeeProfileDB").document(documentId)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                /** On successfully removing data
                                                 * @param aVoid with success
                                                 */
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    profileDataList.remove(position);
                                                    imagesArrayAdapter.notifyDataSetChanged();
                                                }
                                            });
                                }
                            }
                        });
                //notifying user
                Toast.makeText(AdminProfileActivity.this, "Profile Deleted", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    /**
     * Interface for callback
     */

    public interface MyCallback {
        /** interface for callback
         * @param list1 of attendees
         */
        void onCallback(List<Attendee> list1);
    }

    /** Reading data
     * @param myCallback with callback firestore data
     */
    public void readData(MyCallback myCallback) {
        profileRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            /**on successfully querying from firestore
             * @param queryDocumentSnapshots with successfully queries
             */
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    //getting document strings
                    attendeeName = document.getString("name");
                    contact = document.getString("contact_number");
                    homePage = document.getString("home_page");
                    profile_image = document.getString("profile_image");
                    Attendee attendee = new Attendee(attendeeName, contact, homePage, profile_image);
                    profileDataList.add(attendee);
                }
                myCallback.onCallback(profileDataList);
            }
        });


        back2AdminButton.setOnClickListener(new View.OnClickListener() {
            /**Getting to AdminActivity
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminProfileActivity.this, AdminActivity.class));
            }
        });
    }
}
