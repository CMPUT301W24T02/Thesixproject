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
import java.util.Set;

/**
 * This class represents an activity in the Android application responsible for updating attendee profiles.
 * It allows users to input their name, contact information, homepage URL, and select a profile picture from the gallery.
 * Attendee profile data is persisted using SharedPreferences, while profile pictures are stored internally.
 * SHA-256 hashing is utilized for generating unique identifiers based on attendee information and device ID.
 *
 *
 */
public class AttendeeProfileUpdate extends AppCompatActivity {
    // Attributes
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


    /** Initializes the activity and its views.
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_profile_update);
        //colour lists
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

        //initizaling buttons
        nameEditText = findViewById(R.id.name_editText);
        contactEditText = findViewById(R.id.contact_editText);
        homePageEditText = findViewById(R.id.homePage_editText);
        profileImageView = findViewById(R.id.profile_picture);
        submitButton = findViewById(R.id.submit_button);
        backButton = findViewById(R.id.backButton);
        removePictureButton = findViewById(R.id.remove_picture_button);

        SharedPreferences prefs = getSharedPreferences("AttendeePrefs", MODE_PRIVATE);
        loadExistingData(prefs);

        // Set a click listener for the profileImageView, which allows users to select a photo from the gallery
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

    /** On activity Result
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //get data uri selected images
            Uri selectedImage = data.getData();
            String imagePath = saveImageToInternalStorage(selectedImage);
            temporaryImagePath = imagePath; // Update the temporaryImagePath
            profileImageView.setImageURI(Uri.fromFile(new File(imagePath))); // Set the ImageView to the new image
        }
    }

    /**
     * Saves the attendee's profile data (name, contact, homepage) to the SharedPreferences.
     *
     * @param prefs The SharedPreferences instance to store the data.
     */
    private void saveProfileData(SharedPreferences prefs) {
        //// Get an editor to modify the SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        // Save the profile data to SharedPreferences
        editor.putString("name", nameEditText.getText().toString());
        editor.putString("contact", contactEditText.getText().toString());
        editor.putString("homePage", homePageEditText.getText().toString());
        // Apply the changes
        editor.apply();
    }
    /**
     * Loads existing profile data from SharedPreferences and displays it in the corresponding EditText fields and ImageView.
     *
     * @param prefs The SharedPreferences instance from which to load the data.
     */
    private void loadExistingData(SharedPreferences prefs) {
        // Set the text in the EditText fields to the corresponding values stored in SharedPreferences
        nameEditText.setText(prefs.getString("name", ""));
        contactEditText.setText(prefs.getString("contact", ""));
        homePageEditText.setText(prefs.getString("homePage", ""));

        // Get the profile image path from SharedPreferences
        String imagePath = prefs.getString("profileImagePath", null);
        // If a profile image path exists, set the ImageView to display the image
        if (imagePath != null) {
            profileImageView.setImageURI(Uri.fromFile(new File(imagePath)));
        }
    }

    /**
     * Removes the profile picture by deleting its reference from SharedPreferences and resetting the ImageView to display a default image.
     * If the profile picture is associated with a document ID, it also removes the profile image from the database.
     *
     * @param prefs The SharedPreferences instance from which to remove the profile picture reference.
     */
    private void removeProfilePicture(SharedPreferences prefs) {
        // Retrieve the document ID associated with the profile picture from SharedPreferences
        String documentId = prefs.getString("documentId", "");

        // Check if a document ID exists
        if (!documentId.isEmpty()) {
            // Initialize AttendeeDB instance to access database operations
            AttendeeDB attendeeDB = new AttendeeDB();
            // Remove the profile image from the database using the document ID
            attendeeDB.removeProfileImage(documentId);
            // Get SharedPreferences editor to remove the profile picture reference
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("profileImagePath");
            editor.apply();
            // Reset the ImageView to display a default image
            profileImageView.setImageResource(android.R.drawable.ic_menu_gallery); // Reset to a default image
            Toast.makeText(this, "Profile picture removed", Toast.LENGTH_SHORT).show();
        } else {
            // If no document ID is found, display a toast message indicating that no profile picture is present
            Toast.makeText(this, "No profile picture to remove", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Saves the selected image to internal storage and returns the absolute path of the saved image file.
     *
     * @param uri The URI of the selected image.
     * @return The absolute path of the saved image file, or null if saving fails.
     */
    private String saveImageToInternalStorage(Uri uri) {
        try {
            // Open an InputStream to read the selected image
            InputStream inputStream = getContentResolver().openInputStream(uri);
            // Decode the input stream into a Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile("profile_", ".jpg", storageDir);
            FileOutputStream out = new FileOutputStream(imageFile);
            // Compress and write the bitmap data to the FileOutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            // Return the absolute path of the saved image file
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            // If an IOException occurs, display a toast message indicating that image saving failed
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    /**
     * Converts a Bitmap image to a Base64-encoded string.
     *
     * @param bitmap The Bitmap image to be converted.
     * @return The Base64-encoded string representation of the Bitmap image.
     */
    public String BitMapToString(Bitmap bitmap) {
        // Create a ByteArrayOutputStream to hold the compressed bitmap data
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress the bitmap image to JPEG format with maximum quality (100), and write the compressed data to the ByteArrayOutputStream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return result;

    }
    //https://www.geeksforgeeks.org/sha-256-hash-in-java/
    /**
     * Calculates the SHA-256 hash of the input string.
     *
     * @param input The input string to be hashed.
     * @return The SHA-256 hash of the input string as a byte array.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
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
     *
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
    /**
     * Creates a bitmap image based on the provided string using SHA-256 hashing algorithm.
     *
     * @param string The input string to be hashed and used to generate the bitmap.
     * @return The generated bitmap image.
     */
    private Bitmap createBitmap(String string) {
        // Create a new bitmap with dimensions 16x16 pixels and RGB_565 configuration
        Bitmap Invitebitmap = Bitmap.createBitmap(16, 16, Bitmap.Config.RGB_565);
        try {
            // Get the SHA-256 hash of the input string
            byteList = getSHA(string);
        } catch (NoSuchAlgorithmException e) {
            // Throw a runtime exception if NoSuchAlgorithmException occurs
            throw new RuntimeException(e);
        }
        // Generate color lists based on the hash values
        for (int i = 0 ;i<16;i++) {
            finalColorList1[i] = colorlist.get(Math.abs(byteList[i]%10));
        }
        for (int i = 16 ;i<32;i++) {
            finalColorList2[i-16] = colorlist.get(Math.abs(byteList[i]%10));
        }
        // Populate the bitmap with colors based on the generated color lists
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
     * Converts a Bitmap image to a Base64 encoded string.
     *
     * @param bitmap The Bitmap image to be converted.
     * @return The Base64 encoded string representing the Bitmap image.
     */
    private String bitmapToBase64(Bitmap bitmap) {
        // Create a ByteArrayOutputStream to write the compressed bitmap data
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // Compress the bitmap image to PNG format with 100% quality and write to the ByteArrayOutputStream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        // Encode the byte array to Base64 string using default Base64 encoding
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
