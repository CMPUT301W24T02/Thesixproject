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

    private List<GeoPoint> geoPoints = new ArrayList<>();;
    List<GeoPoint> locationList = new ArrayList<>();

    /**
     * Callback for firebase returning document ID after a successful Firestore operation.
     */
    // Callback interface for returning document ID after a successful Firestore operation.
    public interface FirestoreCallback {
        /**Callback for firebase
         * @param documentId returning document ID
         */
        void onCallback(String documentId);
    }

    /**
     * getting instance of firestore
     */
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

        //saving attendee info
        attendee.put("name", name);
        attendee.put("contact_number", contact);
        attendee.put("home_page", homePage);
        String imageBase64 = encodeImageToBase64(imagePath);
        //if image base is null
        if (imageBase64 != null) {
            attendee.put("profile_image", imageBase64);
        }

        firestore.collection("AttendeeProfileDB")
                .add(attendee)
                .addOnSuccessListener(documentReference -> {
                    //log i
                    Log.d("AttendeeDB", "DocumentSnapshot written with ID: " + documentReference.getId());
                    callback.onCallback(documentReference.getId()); // Invoke callback with document ID
                })
                //adding on failure listener
                .addOnFailureListener(e -> Log.w("AttendeeDB", "Error adding document", e));
    }

    /** Saves attendee information to Firestore without profile picture
     * @param name name of attendee
     * @param contact contact of attendee
     * @param homePage homepage of attendee
     * @param imageData imageData of attendee
     * @param deviceID device id of attendee
     */
    public void saveAttendeeInfoNoPhoto(String name, String contact, String homePage, String imageData, String deviceID) {
        Attendee attendee = new Attendee(name, contact, homePage, imageData);
        firestore.collection("AttendeeProfileDB").document(deviceID).set(attendee);
    }

    /**
     *  Get attendee reference
     */
    public CollectionReference getAttendeeDocRef() {
        return firestore.collection("AttendeeProfileDB");
    }
    /** Updates attendee information to Firestore
     * @param documentId document id .to string()
     * @param name name of attendee
     * @param contact contact of attendee
     * @param homePage homepage of attendee
     * @param imagePath  imageData of attendee
     */
    public void updateAttendeeInfo(String documentId, String name, String contact, String homePage, String imagePath) {
        Map<String, Object> attendee = new HashMap<>();
        //updating
        attendee.put("name", name);
        attendee.put("contact", contact);
        attendee.put("homePage", homePage);
        String imageBase64 = encodeImageToBase64(imagePath);
        if (imageBase64 != null) {
            attendee.put("profile_image", imageBase64);
        }
        //updating firestore
        firestore.collection("AttendeeProfileDB").document(documentId)
                .set(attendee)
                .addOnSuccessListener(aVoid -> Log.d("AttendeeDB", "DocumentSnapshot successfully updated"))
                .addOnFailureListener(e -> Log.w("AttendeeDB", "Error updating document", e));
    }
    public CollectionReference getAttendeeCollection() {
        return firestore.collection("AttendeeProfileDB");
    }
    /** encode Image To Base64
     * @param imagePath of image to encode
     * @return base string of image
     */
    private String encodeImageToBase64(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            FileInputStream fis = new FileInputStream(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // creating buffer
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = baos.toByteArray();
            fis.close();
            //return encoding
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //catching errors
        } catch (FileNotFoundException e) {
            Log.e("AttendeeDB", "File not found: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("AttendeeDB", "Error encoding image to Base64: " + e.getMessage());
            return null;
        }
    }

    /** Added method to remove the profile image from Firestore
     * @param documentId path in which to delete
     */
    // Added method to remove the profile image from Firestore
    public void removeProfileImage(String documentId) {
        firestore.collection("AttendeeProfileDB").document(documentId)
                .update("profile_image", FieldValue.delete()) // This will remove the field
                .addOnSuccessListener(aVoid -> Log.d("AttendeeDB", "Profile image successfully removed"))
                .addOnFailureListener(e -> Log.e("AttendeeDB", "Error removing profile image", e));
    }

    /** saving suer location to firebase
     * @param deviceID deviceId of user
     * @return Collection reference
     */
    public CollectionReference saveUserLocation(String deviceID) {
        return firestore.collection("OrganizerdevicesDB")
                .document(deviceID)
                .collection("event");
    }

    /**
     * Callback for firebase location
     */
    public interface LocationCallback {
        /**on receiving locations
         * @param locationList list of geopoints
         */
        void onLocationReceived(List<GeoPoint> locationList);

        /** on receiving locations error
         * @param errorMessage to adding locaton error
         */
        void onLocationError(String errorMessage);
    }


    /** getting list of geopoints
     * @param organizerID device id of organizer
     * @param eventNum event number to pull
     * @param callback on receiving locations
     */
    public void getLocationDocRef(String organizerID, Long eventNum, LocationCallback callback) {
        DocumentReference documentReference = firestore.collection("OrganizerdevicesDB")
                .document(organizerID)
                .collection("event")
                .document(String.valueOf(eventNum));

        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                //making new arraylist
                List<GeoPoint> locationList = new ArrayList<>();
                List<GeoPoint> geoPoints = (List<GeoPoint>) documentSnapshot.get("location");
                //if geopoints are no empty add them
                if (geoPoints != null) {
                    locationList.addAll(geoPoints);
                    Log.i("locationList1", locationList.toString());
                    callback.onLocationReceived(locationList);
                }
            } else {
                // Document does not exist
                // Handling this case accordingly
                Log.d("locationDocdontexist", "getLocationDocRef: ");
                callback.onLocationError("Document does not exist");
            }
        }).addOnFailureListener(e -> {
            // Error fetching document
            // Handling this case accordingly
            Log.e("locationDoc", "getLocationDocRef: ", e);
            callback.onLocationError(e.getMessage());
        });
    }
}

