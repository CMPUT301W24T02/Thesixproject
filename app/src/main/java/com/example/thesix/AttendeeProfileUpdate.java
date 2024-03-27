package com.example.thesix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AttendeeProfileUpdate extends AppCompatActivity {
    private EditText nameEditText, contactEditText, homePageEditText;
    private Button submitButton, back2AttendeeProfileButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_profile_update);

        nameEditText = findViewById(R.id.name_editText);
        contactEditText = findViewById(R.id.contact_editText);
        homePageEditText = findViewById(R.id.homePage_editText);
        submitButton = findViewById(R.id.submit_button);
        back2AttendeeProfileButton = findViewById(R.id.backButton);

        submitButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String contact = contactEditText.getText().toString();
            String homePage = homePageEditText.getText().toString();

            // Save to Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> attendee = new HashMap<>();
            attendee.put("name", name);
            attendee.put("contact_number", contact);
            attendee.put("home_page", homePage);

            db.collection("attendee").add(attendee)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(AttendeeProfileUpdate.this, "Information Saved to Firestore", Toast.LENGTH_SHORT).show();

                        // Also save to SharedPreferences for local access
                        SharedPreferences prefs = getSharedPreferences("AttendeePrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("name", name);
                        editor.putString("contact", contact);
                        editor.putString("homePage", homePage);
                        editor.apply();

                        // Indicate success
                        setResult(RESULT_OK);

                        // Finish activity and return to the profile
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(AttendeeProfileUpdate.this, "Error saving to Firestore", Toast.LENGTH_SHORT).show());
        });

        back2AttendeeProfileButton.setOnClickListener(v -> {
            // Simply finish this activity without saving or passing back any data
            finish();
        });
    }
}
