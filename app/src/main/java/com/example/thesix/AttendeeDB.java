package com.example.thesix;

import android.location.Location;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * AttendeeDB class facilitates interaction with a Firestore database to manage attendee information.
 * Initializes a FirebaseFirestore instance (firestore)
 **/

public class AttendeeDB {

    private FirebaseFirestore firestore;

    private List<GeoPoint> geoPoints=new ArrayList<>();;
    List<GeoPoint> locationList = new ArrayList<>();

    // Callback interface for returning document ID after a successful Firestore operation.
    public interface FirestoreCallback {
        void onCallback(String documentId);
    }

    public AttendeeDB() {
        firestore = FirebaseFirestore.getInstance();
    }

    /**
     * Saves attendee information to Firestore and uses a callback to return the document ID.
     *
     * @param name      The name of the attendee.
     * @param contact   The contact number of the attendee.
     * @param homePage  The home page of the attendee.
     * @param imagePath The profile image of the attendee.
     * @param callback  The callback interface for returning the document ID.
     */
    public void saveAttendeeInfo(String name, String contact, String homePage, String imagePath, FirestoreCallback callback) {
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
                .addOnSuccessListener(documentReference -> {
                    Log.d("AttendeeDB", "DocumentSnapshot written with ID: " + documentReference.getId());
                    callback.onCallback(documentReference.getId()); // Invoke callback with document ID
                })
                .addOnFailureListener(e -> Log.w("AttendeeDB", "Error adding document", e));
    }

    public void saveAttendeeInfoNoPhoto(String name, String contact, String homePage, String imageData, String deviceID) {
        Attendee attendee = new Attendee(name, contact, homePage, imageData);
        firestore.collection("AttendeeProfileDB").document(deviceID).set(attendee);
    }


    public void updateAttendeeInfo(String documentId, String name, String contact, String homePage, String imagePath) {
        Map<String, Object> attendee = new HashMap<>();
        attendee.put("name", name);
        attendee.put("contact_number", contact);
        attendee.put("home_page", homePage);
        String imageBase64 = encodeImageToBase64(imagePath);
        if (imageBase64 != null) {
            attendee.put("profile_image", imageBase64);
        }

        firestore.collection("AttendeeProfileDB").document(documentId)
                .set(attendee)
                .addOnSuccessListener(aVoid -> Log.d("AttendeeDB", "DocumentSnapshot successfully updated"))
                .addOnFailureListener(e -> Log.w("AttendeeDB", "Error updating document", e));
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

    // Added method to remove the profile image from Firestore
    public void removeProfileImage(String documentId) {
        firestore.collection("AttendeeProfileDB").document(documentId)
                .update("profile_image", FieldValue.delete()) // This will remove the field
                .addOnSuccessListener(aVoid -> Log.d("AttendeeDB", "Profile image successfully removed"))
                .addOnFailureListener(e -> Log.e("AttendeeDB", "Error removing profile image", e));
    }

    public CollectionReference saveUserLocation(String deviceID) {
        return firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .collection("event");
    }
    public interface LocationCallback {
        void onLocationReceived(List<GeoPoint> locationList);
        void onLocationError(String errorMessage);
    }


    public void getLocationDocRef(String organizerID, Long eventNum, LocationCallback callback) {
        DocumentReference documentReference = firestore.collection("OrganizerdevicesDB")
                .document(organizerID)
                .collection("event")
                .document(String.valueOf(eventNum));

        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<GeoPoint> locationList = new ArrayList<>();
                List<GeoPoint> geoPoints = (List<GeoPoint>) documentSnapshot.get("location");
                if (geoPoints != null) {
                    locationList.addAll(geoPoints);
                    Log.i("locationList1", locationList.toString());
                    callback.onLocationReceived(locationList);
                }
            } else {
                // Document does not exist
                // Handle this case accordingly
                Log.d("locationDocdontexist", "getLocationDocRef: ");
                callback.onLocationError("Document does not exist");
            }
        }).addOnFailureListener(e -> {
            // Error fetching document
            // Handle this case accordingly
            Log.e("locationDoc", "getLocationDocRef: ", e);
            callback.onLocationError(e.getMessage());
        });
    }
}
