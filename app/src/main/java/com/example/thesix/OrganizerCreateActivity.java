package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * OrganizerCreateActivity class allows organizers to create new events or use existing QR codes.
 * Initializes UI components such as Button (useExistingQrCodeButton, createNewQrCodeButton) and QrCodeDB instance (firestoreHelper) in the onCreate method.
 * Sets click listeners for each button to handle navigation to corresponding activities.
 * Clicking useExistingQrCodeButton navigates to OrganizerUseOldQRActivity to use existing QR codes.
 * Clicking createNewQrCodeButton navigates to OrganizerUseNewQRActivity to create new QR codes.
 * Clicking backButton navigates back to MainActivity.
 *
 */

public class OrganizerCreateActivity extends AppCompatActivity {
    private Button useExistingQrCodeButton;
    private Button createNewQrCodeButton;
    private QrCodeDB firestoreHelper;
    private Button backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_create_screen);
        useExistingQrCodeButton = findViewById(R.id.useExistingQrCodeButton);
        createNewQrCodeButton = findViewById(R.id.createNewQrCodeButton);
        firestoreHelper = new QrCodeDB();
        backButton  =findViewById(R.id.backButton);

        useExistingQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerCreateActivity.this, OrganizerUseOldQRActivity.class));
            }
        });
        createNewQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerCreateActivity.this, OrganizerUseNewQRActivity.class));
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerCreateActivity.this, MainActivity.class));
            }
        });


    }
}
