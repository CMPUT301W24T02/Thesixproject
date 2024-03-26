package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AttendeeProfileUpdate extends AppCompatActivity {
    private EditText nameEditText;
    private EditText contactEditText;
    private EditText homePageEditText;
    private Button submitButton;
    private Button back2AttendeeProfileButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_profile_update);

        // Initialize EditText and Button
        nameEditText = findViewById(R.id.name_editText);
        contactEditText = findViewById(R.id.contact_editText);
        homePageEditText = findViewById(R.id.homePage_editText);
        submitButton = findViewById(R.id.submit_button);
        back2AttendeeProfileButton = findViewById(R.id.backButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to send result back
                Intent resultIntent = new Intent();
                resultIntent.putExtra("name", nameEditText.getText().toString());
                resultIntent.putExtra("contact", contactEditText.getText().toString());
                resultIntent.putExtra("homePage", homePageEditText.getText().toString());

                setResult(RESULT_OK, resultIntent);
                finish(); // Close the activity
            }
        });

        //Sets up the 'back2AttendeeButton' to return to the Attendee main screen when clicked.
        back2AttendeeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the AttendeeMainActivity
                finish(); // Close the activity
            }
        });
    }

}
