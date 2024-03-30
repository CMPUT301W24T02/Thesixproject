package com.example.thesix;

import android.location.Location;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * AttendeeDB class facilitates interaction with a Firestore database to manage attendee information.
 * Initializes a FirebaseFirestore instance (firestore)
 **/

public class AttendeeDB {

    private FirebaseFirestore firestore;

    public AttendeeDB() {
        firestore = FirebaseFirestore.getInstance();
    }

    /**
     * Saves attendee information to Firestore.
     *
     * @param name The name of the attendee.
     * @param contact The contact number of the attendee.
     * @param homePage The home page of the attendee.
     * @param imagePath The profile image of the attendee.
     */
    public void saveAttendeeInfo(String name, String contact, String homePage, String imagePath) {
        Map<String, Object> attendee = new HashMap<>();
        attendee.put("name", name);
        attendee.put("contact_number", contact);
        attendee.put("home_page", homePage);
        String imageBase64 = encodeImageToBase64(imagePath);
        if (imageBase64 != null) {
            attendee.put("profile_image", imageBase64);
        }

        firestore.collection("AttendeeProfileDB")
                .add(attendee)
                .addOnSuccessListener(documentReference -> Log.d("AttendeeDB", "DocumentSnapshot written with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("AttendeeDB", "Error adding document", e));
    }

    private String encodeImageToBase64(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            FileInputStream fis = new FileInputStream(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = baos.toByteArray();
            fis.close();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            Log.e("AttendeeDB", "File not found: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("AttendeeDB", "Error encoding image to Base64: " + e.getMessage());
            return null;
        }
    }

    public void saveUserLocation(String deviceID, Location location) {
        firestore.collection("UserLocations")
                .document(deviceID)
                .collection("coordinates")
                .add(location)
                .addOnSuccessListener(documentReference ->
                        Log.d("FirestoreHelper", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("FirestoreHelper", "Error adding document", e));
    }
}
