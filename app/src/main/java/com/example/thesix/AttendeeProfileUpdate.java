package com.example.thesix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class AttendeeProfileUpdate extends AppCompatActivity {
    private EditText nameEditText, contactEditText, homePageEditText;
    private ImageView profileImageView;
    private Button submitButton, backButton, removePictureButton;
    private String temporaryImagePath = null; // Temporary storage for the selected image path
    byte[] byteList;
    String deviceID;
    Bitmap profileBitmap;
    List<Integer> colorlist;
    int[] finalColorList1;
    int[] finalColorList2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_profile_update);
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
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        nameEditText = findViewById(R.id.name_editText);
        contactEditText = findViewById(R.id.contact_editText);
        homePageEditText = findViewById(R.id.homePage_editText);
        profileImageView = findViewById(R.id.profile_picture);
        submitButton = findViewById(R.id.submit_button);
        backButton = findViewById(R.id.backButton);
        removePictureButton = findViewById(R.id.remove_picture_button);

        SharedPreferences prefs = getSharedPreferences("AttendeePrefs", MODE_PRIVATE);
        loadExistingData(prefs);


        profileImageView.setOnClickListener(v -> {
            // Intent to pick a photo from the gallery
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 2); // requestCode 2 for image selection
        });

        submitButton.setOnClickListener(v -> {
            // Gather the information entered by the user
            String name = nameEditText.getText().toString();
            String contact = contactEditText.getText().toString();
            String homePage = homePageEditText.getText().toString();

            // Check if any of the EditText fields are empty
            if(name.isEmpty() || contact.isEmpty() || homePage.isEmpty()) {
                // Show a toast message if any field is empty
                Toast.makeText(AttendeeProfileUpdate.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                return; // Exit the onClick method early
            }

            String imagePath = temporaryImagePath != null ? temporaryImagePath : prefs.getString("profileImagePath", "");

            AttendeeDB attendeeDB = new AttendeeDB();
//            String documentId = prefs.getString("documentId", "");
            String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            SharedPreferences.Editor editor = prefs.edit();
            if (temporaryImagePath != null) {
                editor.putString("profileImagePath", temporaryImagePath);
            }
            // Always save profile data
            saveProfileData(prefs);
            Log.d("emt","hihi: " +imagePath);
            if (deviceID.isEmpty()) {
                // Logic to save new attendee information
                if(imagePath == null || imagePath.trim().isEmpty()) {
                    Log.d("qwert","1");
                    profileBitmap = createBitmap(name+deviceID);
                    String base64 = bitmapToBase64(profileBitmap);
                    attendeeDB.saveAttendeeInfoNoPhoto(name,contact,homePage,base64,deviceID);
                     }
                else {
                    Log.d("qwert","2");
                    attendeeDB.saveAttendeeInfo(name, contact, homePage, imagePath, newDocumentId -> {
                        editor.putString("documentId", newDocumentId);
                        // Set flag to indicate that the profile has been updated
                        editor.putBoolean("profileUpdated", true);
                        editor.apply();

                        // Indicate that the update was successful
                        setResult(RESULT_OK);
                        finish();
                    });
                }
            } else {
                if(imagePath == null || !imagePath.trim().isEmpty()||imagePath=="") {
                    Log.d("qwert","3");
                    profileBitmap = createBitmap(name+deviceID);
                    String base64 = bitmapToBase64(profileBitmap);
                    attendeeDB.saveAttendeeInfoNoPhoto(name,contact,homePage,base64,deviceID);
                    }
                else {
                    Log.d("qwert","4");
                    // Logic to update existing attendee information
                    attendeeDB.updateAttendeeInfo(deviceID, name, contact, homePage, imagePath);
                    // Set flag to indicate that the profile has been updated here as well
                    editor.putBoolean("profileUpdated", true);
                    editor.apply();

                    // Indicate that the update was successful
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        backButton.setOnClickListener(v -> finish());

        removePictureButton.setOnClickListener(v -> removeProfilePicture(prefs));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String imagePath = saveImageToInternalStorage(selectedImage);
            temporaryImagePath = imagePath; // Update the temporaryImagePath
            profileImageView.setImageURI(Uri.fromFile(new File(imagePath))); // Set the ImageView to the new image
        }
    }

    private void saveProfileData(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name", nameEditText.getText().toString());
        editor.putString("contact", contactEditText.getText().toString());
        editor.putString("homePage", homePageEditText.getText().toString());
        editor.apply();
    }

    private void loadExistingData(SharedPreferences prefs) {
        nameEditText.setText(prefs.getString("name", ""));
        contactEditText.setText(prefs.getString("contact", ""));
        homePageEditText.setText(prefs.getString("homePage", ""));
        String imagePath = prefs.getString("profileImagePath", null);
        if (imagePath != null) {
            profileImageView.setImageURI(Uri.fromFile(new File(imagePath)));
        }
    }

    private void removeProfilePicture(SharedPreferences prefs) {
        String documentId = prefs.getString("documentId", "");
        if (!documentId.isEmpty()) {
            AttendeeDB attendeeDB = new AttendeeDB();
            attendeeDB.removeProfileImage(documentId);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("profileImagePath");
            editor.apply();
            profileImageView.setImageResource(android.R.drawable.ic_menu_gallery); // Reset to a default image
            Toast.makeText(this, "Profile picture removed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No profile picture to remove", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile("profile_", ".jpg", storageDir);
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            return null;
        }
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
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
