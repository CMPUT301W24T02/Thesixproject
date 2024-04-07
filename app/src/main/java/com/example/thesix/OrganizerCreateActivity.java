package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * OrganizerCreateActivity class allows organizers to create new events or use existing QR codes.
 */

public class OrganizerCreateActivity extends AppCompatActivity {
    private Button createNewQrCodeButton;
    private QrCodeDB firestoreHelper;
    private Button backButton;

    /**
     Initializes UI components such as Button ( createNewQrCodeButton) and QrCodeDB instance (firestoreHelper)
     @param savedInstanceState instance state
     **/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_create_screen);
        //initialzing UI
        createNewQrCodeButton = findViewById(R.id.createNewQrCodeButton);
        firestoreHelper = new QrCodeDB();
        backButton  =findViewById(R.id.backButton);

        createNewQrCodeButton.setOnClickListener(new View.OnClickListener() {
            /**Clicking createNewQrCodeButton navigates to OrganizerUseNewQRActivity to create new QR codes
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerCreateActivity.this, OrganizerUseNewQRActivity.class));
            }
        });
        //setting back button
        backButton.setOnClickListener(new View.OnClickListener() {
            /**Clicking backButton navigates back to MainActivity.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerCreateActivity.this, OrganizerMainActivity.class));
            }
        });


    }
}
