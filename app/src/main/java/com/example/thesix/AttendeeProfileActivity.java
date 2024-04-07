package com.example.thesix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
        back2AttendeeButton.setOnClickListener(v -> finish());

        // Setup ImageView click to navigate to AttendeeProfileUpdate for editing


        // Load profile information including image
        displayAttendeeInfo();
    }

    /**
     * Called when the activity is resumed after being paused.
     * Checks if the attendee profile was updated while the activity was paused.
     * If the profile was updated, it refreshes the displayed attendee information.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Check if the profile was updated
        SharedPreferences prefs = getSharedPreferences("AttendeePrefs", MODE_PRIVATE);
        boolean profileUpdated = prefs.getBoolean("profileUpdated", false);
        if (profileUpdated) {
            displayAttendeeInfo(); // Refresh the profile information
            // Reset the flag
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("profileUpdated", false);
            editor.apply();
        }
    }

    /** When activity result
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Refresh data when returning from AttendeeProfileUpdate and updates have been made
            displayAttendeeInfo();
        }
    }

    /**
     * Displays attendee information retrieved from SharedPreferences.
     * Sets the name, contact, and home page text views with the corresponding data.
     * If the profile picture was removed, it generates a default profile picture.
     */
    private void displayAttendeeInfo() {
        // Access SharedPreferences
        SharedPreferences prefs = getSharedPreferences("AttendeePrefs", MODE_PRIVATE);
        // Set name text view with attendee's name
        nameTextView.setText(prefs.getString("name", ""));
        contactTextView.setText(prefs.getString("contact", ""));
        // Set home page text view with attendee's home page URL
        homePageTextView.setText(prefs.getString("homePage", ""));

        // Check if the profile picture was removed
        boolean isProfilePictureRemoved = prefs.getBoolean("isProfilePictureRemoved", false);
        if (isProfilePictureRemoved) {
            Log.d("qwer","2");
            // Create default profile picture bitmap
            profileBitmap = createBitmap("testing");
            // Set the image view with the default profile picture
            profilePicture.setImageBitmap(profileBitmap);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isProfilePictureRemoved", false);
            editor.apply();
        } else {
            // If the profile picture was not removed, check if a custom profile picture exists
            String imagePath = prefs.getString("profileImagePath", null);
            if (imagePath != null && new File(imagePath).exists()) {
                Log.d("qwer","1");
                //Set the image view with the custom profile picture
                profilePicture.setImageURI(Uri.fromFile(new File(imagePath)));
            } else {
                Log.d("qwer","3");
                profileBitmap = createBitmap("testing");
                // Set the image view with the default profile picture
                profilePicture.setImageBitmap(profileBitmap);
            }
        }
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

}
