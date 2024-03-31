package com.example.thesix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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

public class AttendeeProfileActivity extends AppCompatActivity {
    private ImageView profilePicture;
    private TextView nameTextView, contactTextView, homePageTextView;
    private Button back2AttendeeButton;
    byte[] byteList;
    Bitmap profileBitmap;
    List<Integer> colorlist;
    int[] finalColorList1;
    int[] finalColorList2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_profile_screen);
        finalColorList1 = new int[16];
        finalColorList2 = new int[16];

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
        profileBitmap = createBitmap("testing");
        profilePicture.setImageBitmap(profileBitmap);
        // Setup ImageView click to navigate to AttendeeProfileUpdate for editing
        profilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(AttendeeProfileActivity.this, AttendeeProfileUpdate.class);
            startActivityForResult(intent, 1);
        });

        // Load profile information including image
        displayAttendeeInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure data is refreshed when coming back to this activity
        displayAttendeeInfo();
    }

    private void displayAttendeeInfo() {
        SharedPreferences prefs = getSharedPreferences("AttendeePrefs", MODE_PRIVATE);
        nameTextView.setText(prefs.getString("name", ""));
        contactTextView.setText(prefs.getString("contact", ""));
        homePageTextView.setText(prefs.getString("homePage", ""));

        boolean isProfilePictureRemoved = prefs.getBoolean("isProfilePictureRemoved", false);
        if (isProfilePictureRemoved) {
            // Generate and set the bitmap as the profile picture
            profileBitmap = createBitmap("testing");
            profilePicture.setImageBitmap(profileBitmap);
            // Reset the flag
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isProfilePictureRemoved", false);
            editor.apply();
        } else {
            String imagePath = prefs.getString("profileImagePath", null);
            if (imagePath != null) {
                profilePicture.setImageURI(Uri.fromFile(new File(imagePath)));
            } else {
                // Handle case where there is no imagePath, but also isProfilePictureRemoved is false
                profileBitmap = createBitmap("testing");
                profilePicture.setImageBitmap(profileBitmap);
            }
        }
    }
    private Bitmap createBitmap(String string) {
        Bitmap Invitebitmap = Bitmap.createBitmap(16, 16, Bitmap.Config.RGB_565);
        try {
            byteList = getSHA(string);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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
        return Invitebitmap;
    }
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return result;

    }
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
