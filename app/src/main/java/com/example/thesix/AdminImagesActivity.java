package com.example.thesix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminImagesActivity class
 * Requests necessary permissions and sets up the layout for Image listings.
 * Lists all Images in the listview created in the app.
 *
 * Continue JavaDocs
 *
 */

public class AdminImagesActivity extends AppCompatActivity {
    //required Button
    private Button back2AdminButton;
    //creating firestore instance
    private FirebaseFirestore firestore;
    //creating collectionReferences
    private CollectionReference eventImagesRef;
    private CollectionReference profileImagesRef;
    //creating required Array lists
    private ArrayList<Long> eventNumList;
    private ArrayList<String> deviceIdList;
    private String adminBase64Image;

    /** Creating Activity
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_images_screen);

        //back2admin button finding
        back2AdminButton = findViewById(R.id.backButton);

        //creating listviews
        ListView eventImageListView = findViewById(R.id.images_event_view);
        ListView profileImageListView = findViewById(R.id.images_profile_view);
        //asking for permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        //getting firestore instance
        firestore = FirebaseFirestore.getInstance();
        eventImagesRef = firestore.collection("inviteQrCodes");
        profileImagesRef = firestore.collection("AttendeeProfileDB");
        adminBase64Image = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAIAAACQkWg2AAAAAXNSR0IArs4c6QAAAANzQklUBQYFMwuNgAAAAGFJREFUKJGVzrERADEIA8Frys163Ot9IPgYHBAYzSJEBaGH4EUQkXdEknpH8v8v3lFQxHoXW9rZld/ZrmyBvjC0G5rZNVZ2czM7XRZ2ujC3u8vYTpaNHWJspwsbu+CpnS4fMs2CcPktswUAAAAASUVORK5CYII=";

        // For Event
        ArrayList<Bitmap> eventImageDataList = new ArrayList<>();
        eventNumList = new ArrayList<>();
        CustomImageAdapter eventImageAdapter = new CustomImageAdapter(AdminImagesActivity.this, eventImageDataList);

        // For Profile
        ArrayList<Bitmap> profileImageDataList = new ArrayList<>();
        deviceIdList = new ArrayList<>();
        CustomImageAdapter profileImageAdapter = new CustomImageAdapter(AdminImagesActivity.this, profileImageDataList);

        // Set Adapters
        eventImageListView.setAdapter(eventImageAdapter);
        profileImageListView.setAdapter(profileImageAdapter);

        // Firestore Event images
        readEventData(new MyCallback() {
            /** Reading Event data
             * @param list1 list for callback
             * @param imageDataList image data-list with bitmap info
             * @param imageAdapter image data-list adapter
             */
            @Override
            public void onCallback(List<String> list1, ArrayList<Bitmap> imageDataList, CustomImageAdapter imageAdapter) {
                for (String base64String : list1) {
                    Bitmap bitmap = decodeBase64(base64String);
                    if (bitmap != null) {
                        imageDataList.add(bitmap);
                    }
                }
                //notify change
                imageAdapter.notifyDataSetChanged();
            }
        }, eventImageDataList, eventImageAdapter);

        // Firestore Profile images
        readProfileData(new MyCallback() {
            /** Reading Profile data
             * @param list1 to callback from firestore
             * @param imageDataList with required data-list
             * @param imageAdapter custom image adapter
             */
            @Override
            public void onCallback(List<String> list1, ArrayList<Bitmap> imageDataList, CustomImageAdapter imageAdapter) {
                for (String base64String : list1) {
                    Bitmap bitmap = decodeBase64(base64String);
                    if (bitmap != null) {
                        imageDataList.add(bitmap);
                    }
                }
                imageAdapter.notifyDataSetChanged();
            }
        }, profileImageDataList, profileImageAdapter);

        eventImageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /** On clicking item in adapter
             * @param parent   The AbsListView where the click happened
             * @param view     The view within the AbsListView that was clicked
             * @param position The position of the view in the list
             * @param id       The row id of the item that was clicked
             * @return boolean whether clicked
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Replace Here

                Bitmap profilePosterBitmap = decodeBase64(adminBase64Image);
                eventImageDataList.set(position, profilePosterBitmap);
                eventImageAdapter.notifyDataSetChanged();
                //getting position
                long eventNum = eventNumList.get(position);
                eventFirestoreUpdate(eventNum, profilePosterBitmap);
                return true;
            }
        });

        profileImageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /** Profile Image View
             * @param parent   The AbsListView where the click happened
             * @param view     The view within the AbsListView that was clicked
             * @param position The position of the view in the list
             * @param id       The row id of the item that was clicked
             * @return boolean with status
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Replace Here

                Bitmap eventPosterBitmap = decodeBase64(adminBase64Image);
                profileImageDataList.set(position, eventPosterBitmap);
                profileImageAdapter.notifyDataSetChanged();

                //getting deviceId with position
                String deviceId = deviceIdList.get(position);
                profileFirestoreUpdate(deviceId, eventPosterBitmap);

                return true;
            }
        });

        back2AdminButton.setOnClickListener(new View.OnClickListener() {
            /** starting Activity for admin
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminImagesActivity.this, AdminActivity.class));
            }
        });
    }

    /** Reading Event Data
     * @param myCallback with callabck information
     * @param imageDataList with required data-list
     * @param imageAdapter image data-list adapter
     */
    // Firestore Read- Event Images
    public void readEventData(MyCallback myCallback, ArrayList<Bitmap> imageDataList, CustomImageAdapter imageAdapter) {
        eventImagesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            /** on success of getting query
             * @param queryDocumentSnapshots with successful query
             */
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> base64Strings = new ArrayList<>();
                //clearing event number
                eventNumList.clear();
                //looping through queried data
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String base64String = document.getString("eventImageData");
                    Long eventNum = document.getLong("eventNum");
                    base64Strings.add(base64String);
                    eventNumList.add(eventNum);
                }
                //callback on completion
                myCallback.onCallback(base64Strings, imageDataList, imageAdapter);
            }
        });
    }

    /** Reading profile data
     * @param myCallback calling back completed data
     * @param imageDataList creating event image data list
     * @param imageAdapter creating image data adapter
     */
    // Firestore Read- Profile Images
    public void readProfileData(MyCallback myCallback, ArrayList<Bitmap> imageDataList, CustomImageAdapter imageAdapter) {
        profileImagesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            /** On successly accessing firestore
             * @param queryDocumentSnapshots with document snapshots
             */
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> base64Strings = new ArrayList<>();
                //clearing device id
                deviceIdList.clear();

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String base64String = document.getString("profile_image");
                    //adding to firebase
                    deviceIdList.add(document.getId());
                    base64Strings.add(base64String);
                }
                //on callback
                myCallback.onCallback(base64Strings, imageDataList, imageAdapter);
            }
        });
    }

    public interface MyCallback {
        /** On event callback
         * @param list1 with string data
         * @param imageDataList data list of images
         * @param imageAdapter data list adapter of images
         */
        void onCallback(List<String> list1, ArrayList<Bitmap> imageDataList, CustomImageAdapter imageAdapter);
    }

    /** bitmap to string
     * @param base64String of image
     * @return Bitmap of image
     */
    // CHatGPt: Prompt, bitmap to string, string to bitmap
    private Bitmap decodeBase64(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    /**string to bitmap
     * @param bitmap of image
     * @return String
     */
    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //compressing images
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        String base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64String;
    }

    /**Updating eventFirestoreUpdate
     * @param eventNum with eventNum
     * @param eventPosterBitmap with bitmap of event poster
     */
    private void eventFirestoreUpdate(long eventNum, Bitmap eventPosterBitmap) {
        eventImagesRef.whereEqualTo("eventNum", eventNum)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        documentSnapshot.getReference().update("eventImageData", adminBase64Image);
                    }
                    Toast.makeText(this, "Image Deleted, Default Set", Toast.LENGTH_LONG).show();
                });
    }

  /** Updating profileFirestoreUpdate
     * @param docId deviceId
     * @param eventPosterBitmap with bitmap of event poster
     */
    private void profileFirestoreUpdate(String docId, Bitmap eventPosterBitmap) {
        // Change this to Admin pic
        profileImagesRef.document(docId)
                .update("profile_image", adminBase64Image);
        Toast.makeText(this, "Image Deleted, Default Set", Toast.LENGTH_LONG).show();
    }
}
