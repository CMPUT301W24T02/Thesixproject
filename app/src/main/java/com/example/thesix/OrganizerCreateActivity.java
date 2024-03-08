package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
/**
 * OrganizerCreateActivity is an activity class for creating new events and managing QR codes.
 * It provides options to use existing QR codes or create new ones for event invitations or promotions.
 * Users can navigate back to the MainActivity or choose to use existing or create new QR codes.
 * @Auther David Lee
 * @See  AppCompatActivity
 */
public class OrganizerCreateActivity extends AppCompatActivity {
    private Button useExistingQrCodeButton;
    private Button createNewQrCodeButton;
    private QrCodeDB firestoreHelper;
    private Button backButton;
    /**
     * Called when the activity is starting. Initializes UI components and sets up event listeners.
     *
     * @param savedInstanceState    If the activity is being re-initialized after previously being shut down
     *                              then this Bundle contains the data it most recently supplied in
     *                              onSaveInstanceState(Bundle). Note: Otherwise, it is null.
     */
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
