package com.example.thesix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Activity for displaying and managing attendee profile information.
 */

public class AttendeeProfileActivity extends AppCompatActivity {
    private ImageView profilePicture;
    private TextView nameTextView, contactTextView, homePageTextView;
    private Button back2AttendeeButton;
    byte[] byteList;
    Bitmap profileBitmap;
    List<Integer> colorlist;
    int[] finalColorList1;
    int[] finalColorList2;
    private AttendeeDB firestorehelper;
    String name;
    String contact;
    String homepage;
    String bitmapString;

    /** Create Attendee profile activity
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_profile_screen);
        finalColorList1 = new int[16];
        finalColorList2 = new int[16];
        firestorehelper = new AttendeeDB();
        // List of colors for profile picture
        colorlist = Arrays.asList(
                getResources().getColor(R.color.black),
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.magenta),
                getResources().getColor(R.color.cyan),
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.red));
        // Initialize views
        profilePicture = findViewById(R.id.profile_picture);
        nameTextView = findViewById(R.id.name_textView);
        contactTextView = findViewById(R.id.contact_textView);
        homePageTextView = findViewById(R.id.homePage_textView);
        back2AttendeeButton = findViewById(R.id.backButton);

        // Setup button to go back to AttendeeMainActivity
        back2AttendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeProfileActivity.this, AttendeeMainActivity.class));
            }
        });

        // Setup ImageView click to navigate to AttendeeProfileUpdate for editing

        loadProfile(new ProfileCallback() {
            @Override
            public void onProfileCallback(String name, String contact, String homepage, String bitmapString) {
                nameTextView.setText(name);
                contactTextView.setText(contact);
                homePageTextView.setText(homepage);
                Bitmap bitmap = StringToBitMap(bitmapString);
                profilePicture.setImageBitmap(bitmap);
            }
        });
        // Load profile information including image
//        displayAttendeeInfo();
    }

    /**
     * Creates a bitmap with a customized pattern based on the provided string.
     * Each pixel in the bitmap is assigned a color based on the SHA-256 hash of the input string.
     * The color of each pixel is determined by the remainder of the hash value divided by 10.
     * The resulting bitmap has a size of 16x16 pixels.
     *
     * @param string The input string used to generate the bitmap pattern.
     * @return The generated bitmap.
     */
    private Bitmap createBitmap(String string) {
        // Create a new bitmap with dimensions 16x16 and RGB_565 configuration
        Bitmap Invitebitmap = Bitmap.createBitmap(16, 16, Bitmap.Config.RGB_565);
        try {
            // Generate SHA-256 hash of the input string
            byteList = getSHA(string);
        } catch (NoSuchAlgorithmException e) {
            // Throw a runtime exception if SHA-256 algorithm is not available
            throw new RuntimeException(e);
        }
        // Assign colors to the first 16 pixels based on the hash values
        // Set colors for each pixel in the bitmap based on the generated color lists
        for (int i = 0 ;i<16;i++) {
            finalColorList1[i] = colorlist.get(Math.abs(byteList[i]%10));
        }
        for (int i = 16 ;i<32;i++) {
            finalColorList2[i-16] = colorlist.get(Math.abs(byteList[i]%10));
        }

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y <16; y++) {
                if (x%2 == 0) {
                    Invitebitmap.setPixel(x, y, finalColorList1[(x+y)%16]);
                }
                else {
                    Invitebitmap.setPixel(x, y, finalColorList2[(x+y)%16]);
                }
            }
        }
        // Return the generated bitmap
        return Invitebitmap;
    }
    /**
     * Converts a Bitmap image to a Base64-encoded string.
     *
     * @param bitmap The Bitmap image to be converted.
     * @return The Base64-encoded string representation of the Bitmap image.
     */
    public String BitMapToString(Bitmap bitmap) {
        // Create a ByteArrayOutputStream to store the compressed image data
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress the bitmap image to JPEG format with maximum quality (100)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        // Encode the byte array as a Base64 string
        String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return result;

    }
    public interface ProfileCallback {
        void onProfileCallback(String name, String contact, String homepage, String bitmapString);
    }
    public void loadProfile(ProfileCallback profileCallback) {
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        firestorehelper.getAttendeeDocRef().document(deviceID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            //DocumentSnapshot successfully updated
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("count", "DocumentSnapshot data: " + document.getData());

                        name = (String) document.get("name");
                        homepage = (String) document.get("homePage");
                        contact = (String) document.get("contact");
                        bitmapString = (String) document.get("profile_image");
//                        Log.d("pull",name);
//                        Log.d("pull",homepage);
//                        Log.d("pull",contact);
                        profileCallback.onProfileCallback(name,contact,homepage,bitmapString);
                    } else {
                        //DocumentSnapshot not successfully updated
                        Log.d("count", "No such document");
                    }
                } else {
                    Log.d("count", "get failed with ", task.getException());
                }

            }
        });
    }
    /**
     * Calculates the SHA-256 hash of the input string.
     * @param input input The input string for which the hash is to be calculated.
     * @return The SHA-256 hash value as a byte array.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
    //https://www.geeksforgeeks.org/sha-256-hash-in-java/
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Converts a byte array to a hexadecimal string representation.
     * @param hash The byte array to be converted.
     * @return The hexadecimal string representation of the byte array.
     */
    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);
            // Convert the decoded byte array to an input stream
            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            // Decode the input stream into a Bitmap image
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
