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
    private Button back2AdminButton;
    private FirebaseFirestore firestore;
    private CollectionReference eventImagesRef;
    private CollectionReference profileImagesRef;
    private ArrayList<Long> eventNumList;
    private ArrayList<String> deviceIdList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_images_screen);

        back2AdminButton = findViewById(R.id.backButton);
        ListView eventImageListView = findViewById(R.id.images_event_view);
        ListView profileImageListView = findViewById(R.id.images_profile_view);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        firestore = FirebaseFirestore.getInstance();
        eventImagesRef = firestore.collection("inviteQrCodes");
        profileImagesRef = firestore.collection("AttendeeProfileDB");

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
        }, eventImageDataList, eventImageAdapter);

        // Firestore Profile images
        readProfileData(new MyCallback() {
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
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Replace Here
                String adminBase64Image = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAIAAACQkWg2AAAAAXNSR0IArs4c6QAAAANzQklUBQYFMwuNgAAAAGFJREFUKJGVzrERADEIA8Frys163Ot9IPgYHBAYzSJEBaGH4EUQkXdEknpH8v8v3lFQxHoXW9rZld/ZrmyBvjC0G5rZNVZ2czM7XRZ2ujC3u8vYTpaNHWJspwsbu+CpnS4fMs2CcPktswUAAAAASUVORK5CYII=";

                Bitmap profilePosterBitmap = decodeBase64(adminBase64Image);
                eventImageDataList.set(position, profilePosterBitmap);
                eventImageAdapter.notifyDataSetChanged();

                long eventNum = eventNumList.get(position);
                eventFirestoreUpdate(eventNum, profilePosterBitmap);
                return true;
            }
        });

        profileImageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Replace Here
                String adminBase64Image = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAIAAACQkWg2AAAAAXNSR0IArs4c6QAAAANzQklUBQYFMwuNgAAAAGFJREFUKJGVzrERADEIA8Frys163Ot9IPgYHBAYzSJEBaGH4EUQkXdEknpH8v8v3lFQxHoXW9rZld/ZrmyBvjC0G5rZNVZ2czM7XRZ2ujC3u8vYTpaNHWJspwsbu+CpnS4fMs2CcPktswUAAAAASUVORK5CYII=";

                Bitmap eventPosterBitmap = decodeBase64(adminBase64Image);
                profileImageDataList.set(position, eventPosterBitmap);
                profileImageAdapter.notifyDataSetChanged();

                String deviceId = deviceIdList.get(position);
                profileFirestoreUpdate(deviceId, eventPosterBitmap);

                return true;
            }
        });

        back2AdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminImagesActivity.this, AdminActivity.class));
            }
        });
    }

    // Firestore Read- Event Images
    public void readEventData(MyCallback myCallback, ArrayList<Bitmap> imageDataList, CustomImageAdapter imageAdapter) {
        eventImagesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> base64Strings = new ArrayList<>();
                eventNumList.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String base64String = document.getString("eventImageData");
                    Long eventNum = document.getLong("eventNum");
                    base64Strings.add(base64String);
                    eventNumList.add(eventNum);
                }
                myCallback.onCallback(base64Strings, imageDataList, imageAdapter);
            }
        });
    }

    // Firestore Read- Profile Images
    public void readProfileData(MyCallback myCallback, ArrayList<Bitmap> imageDataList, CustomImageAdapter imageAdapter) {
        profileImagesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> base64Strings = new ArrayList<>();
                deviceIdList.clear();

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String base64String = document.getString("profile_image");
                    deviceIdList.add(document.getId());
                    base64Strings.add(base64String);
                }
                myCallback.onCallback(base64Strings, imageDataList, imageAdapter);
            }
        });
    }

    public interface MyCallback {
        void onCallback(List<String> list1, ArrayList<Bitmap> imageDataList, CustomImageAdapter imageAdapter);
    }

    // CHatGPt: Prompt, bitmap to string, string to bitmap
    private Bitmap decodeBase64(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        String base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64String;
    }

    private void eventFirestoreUpdate(long eventNum, Bitmap eventPosterBitmap) {
        // Change this to Admin pic
        String adminBase64Image = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAIAAACQkWg2AAAAAXNSR0IArs4c6QAAAANzQklUBQYFMwuNgAAAAGFJREFUKJGVzrERADEIA8Frys163Ot9IPgYHBAYzSJEBaGH4EUQkXdEknpH8v8v3lFQxHoXW9rZld/ZrmyBvjC0G5rZNVZ2czM7XRZ2ujC3u8vYTpaNHWJspwsbu+CpnS4fMs2CcPktswUAAAAASUVORK5CYII=";
        String TAG = "button";

        eventImagesRef.whereEqualTo("eventNum", eventNum)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        documentSnapshot.getReference().update("eventImageData", adminBase64Image);
                    }
                });
    }

    private void profileFirestoreUpdate(String docId, Bitmap eventPosterBitmap) {
        // Change this to Admin pic
        String adminBase64Image = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAIAAACQkWg2AAAAAXNSR0IArs4c6QAAAANzQklUBQYFMwuNgAAAAGFJREFUKJGVzrERADEIA8Frys163Ot9IPgYHBAYzSJEBaGH4EUQkXdEknpH8v8v3lFQxHoXW9rZld/ZrmyBvjC0G5rZNVZ2czM7XRZ2ujC3u8vYTpaNHWJspwsbu+CpnS4fMs2CcPktswUAAAAASUVORK5CYII=";
        String TAG = "button";
        profileImagesRef.document(docId)
                .update("profile_image", adminBase64Image);
    }
}
