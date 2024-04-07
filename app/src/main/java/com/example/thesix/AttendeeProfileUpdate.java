package com.example.thesix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AttendeeProfileUpdate extends AppCompatActivity {
    private EditText nameEditText, contactEditText, homePageEditText;
    private ImageView profileImageView;
    private Button submitButton, backButton, removePictureButton;
    private String temporaryImagePath = null; // Temporary storage for the selected image path

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_profile_update);

        // Initialize views
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

            // Check if any of the EditText fields have more than 25 characters
            if (name.length() > 25 || contact.length() > 25 || homePage.length() > 25) {
                Toast.makeText(AttendeeProfileUpdate.this, "Typed information cannot exceed 25 characters", Toast.LENGTH_SHORT).show();
                return; // Exit the onClick method early
            }

            String imagePath = temporaryImagePath != null ? temporaryImagePath : prefs.getString("profileImagePath", "");

            AttendeeDB attendeeDB = new AttendeeDB();
            String documentId = prefs.getString("documentId", "");

            SharedPreferences.Editor editor = prefs.edit();
            if (temporaryImagePath != null) {
                editor.putString("profileImagePath", temporaryImagePath);
            }
            // Always save profile data
            saveProfileData(prefs);

            if (documentId.isEmpty()) {
                // Logic to save new attendee information
                attendeeDB.saveAttendeeInfo(name, contact, homePage, imagePath, newDocumentId -> {
                    editor.putString("documentId", newDocumentId);
                    // Set flag to indicate that the profile has been updated
                    editor.putBoolean("profileUpdated", true);
                    editor.apply();

                    // Indicate that the update was successful
                    setResult(RESULT_OK);
                    finish();
                });
            } else {
                // Logic to update existing attendee information
                attendeeDB.updateAttendeeInfo(documentId, name, contact, homePage, imagePath);
                // Set flag to indicate that the profile has been updated here as well
                editor.putBoolean("profileUpdated", true);
                editor.apply();

                // Indicate that the update was successful
                setResult(RESULT_OK);
                finish();
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
}
