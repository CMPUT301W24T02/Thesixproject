package com.example.thesix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class AdminImagesActivity extends AppCompatActivity {
    private Button back2AdminButton;
    private FirebaseFirestore firestore;
    private CollectionReference eventImagesRef;
    private CollectionReference profileImagesRef;

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

        ArrayList<Bitmap> eventImageDataList = new ArrayList<>();
        ArrayList<Bitmap> profileImageDataList = new ArrayList<>();
        CustomImageAdapter eventImageAdapter = new CustomImageAdapter(AdminImagesActivity.this, eventImageDataList);
        CustomImageAdapter profileImageAdapter = new CustomImageAdapter(AdminImagesActivity.this, profileImageDataList);

        // Set Adapters
        eventImageListView.setAdapter(eventImageAdapter);
        profileImageListView.setAdapter(profileImageAdapter);

        // Firestore Event images
        readEventData(new MyCallback() {
            @Override
            public void onCallback(List<String> list1, ArrayList<Bitmap> imageDataList, CustomImageAdapter imageAdapter) {
                // Convert Base64 strings to Bitmaps and add to eventImageDataList
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
                // Convert Base64 strings to Bitmaps
                for (String base64String : list1) {
                    Bitmap bitmap = decodeBase64(base64String);
                    if (bitmap != null) {
                        imageDataList.add(bitmap);
                    }
                }
                imageAdapter.notifyDataSetChanged();
            }
        }, profileImageDataList, profileImageAdapter);

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
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String base64String = document.getString("eventImageData");
                    base64Strings.add(base64String);
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
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String base64String = document.getString("profile_image");
                    base64Strings.add(base64String);
                }
                myCallback.onCallback(base64Strings, imageDataList, imageAdapter);
            }
        });
    }

    public interface MyCallback {
        void onCallback(List<String> list1, ArrayList<Bitmap> imageDataList, CustomImageAdapter imageAdapter);
    }

    private Bitmap decodeBase64(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
