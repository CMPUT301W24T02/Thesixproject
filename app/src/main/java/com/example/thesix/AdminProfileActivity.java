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
 * Continue JavaDocs
 *
 */
public class AdminProfileActivity extends AppCompatActivity {

    private Button back2AdminButton;
    private ArrayList<Attendee> profileDataList;
    private FirebaseFirestore firestore;
    private Button backButton;
    private CollectionReference profileRef;
    private AdminProfileListAdapter imagesArrayAdapter;
    private String attendeeName;
    private String contact;
    private String homePage;
    private String profile_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_screen);
        back2AdminButton = findViewById(R.id.backButton);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        profileDataList = new ArrayList<>();
        backButton = findViewById(R.id.backButton);
        firestore = FirebaseFirestore.getInstance();
        profileRef = firestore.collection("AttendeeProfileDB");
        imagesArrayAdapter = new AdminProfileListAdapter(AdminProfileActivity.this, profileDataList);
        ListView imageList = findViewById(R.id.profile_list_view);
        imageList.setAdapter(imagesArrayAdapter);
        Log.d("DD", "OnCreateAdminProfileActivity, set adapter");

        // Call readData method to populate profileDataList
        readData(new MyCallback() {
            @Override
            public void onCallback(List<Attendee> list1) {
                // Update UI with populated data
                imagesArrayAdapter.notifyDataSetChanged();
            }
        });

        // Set long click listener for the ListView
        imageList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Attendee selectedAttendee = profileDataList.get(position);
                Toast.makeText(AdminProfileActivity.this, "Long clicked on- Delete Me " + selectedAttendee.getName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    /**
     * Interface Callback
     * @param :List<String> list1
     * @return :
     */

    public interface MyCallback {
        void onCallback(List<Attendee> list1);
    }
    /**
     * Reads data
     * @param :MyCallback myCallback
     * @return :
     */

    public void readData(MyCallback myCallback) {
        profileRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
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

        /**
         Initializes a UI component, a Button named back2AdminButton
         @param :
         @return
         **/
        back2AdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminProfileActivity.this, AdminActivity.class));
            }
        });




    }
}
