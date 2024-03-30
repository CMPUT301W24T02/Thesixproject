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
    private Button submitButton, backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_profile_update);

        nameEditText = findViewById(R.id.name_editText);
        contactEditText = findViewById(R.id.contact_editText);
        homePageEditText = findViewById(R.id.homePage_editText);
        profileImageView = findViewById(R.id.profile_picture);
        submitButton = findViewById(R.id.submit_button);
        backButton = findViewById(R.id.backButton);

        SharedPreferences prefs = getSharedPreferences("AttendeePrefs", MODE_PRIVATE);
        loadExistingData(prefs);

        profileImageView.setOnClickListener(v -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 2); // requestCode 2 for image selection
        });

        submitButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String contact = contactEditText.getText().toString();
            String homePage = homePageEditText.getText().toString();
            String imagePath = prefs.getString("profileImagePath", "");

            // Check if the name is entered
            if(name.isEmpty()) {
                // Show a Toast message and return early
                Toast.makeText(AttendeeProfileUpdate.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return; // Stop executing more code, giving the user a chance to enter the name
            }

            AttendeeDB attendeeDB = new AttendeeDB();
            attendeeDB.saveAttendeeInfo(name, contact, homePage, imagePath);

            // Save profile data and show confirmation
            saveProfileData(prefs);
            Toast.makeText(AttendeeProfileUpdate.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });

        backButton.setOnClickListener(v -> finish());
    }

    private void saveProfileData(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name", nameEditText.getText().toString());
        editor.putString("contact", contactEditText.getText().toString());
        editor.putString("homePage", homePageEditText.getText().toString());
        // Do not save the image path here, as it's already saved in onActivityResult
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String imagePath = saveImageToInternalStorage(selectedImage);

            SharedPreferences prefs = getSharedPreferences("AttendeePrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("profileImagePath", imagePath);
            editor.apply();

            profileImageView.setImageURI(Uri.fromFile(new File(imagePath)));
        }
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile("profile_", ".jpg", storageDir);
            try (FileOutputStream out = new FileOutputStream(imageFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
