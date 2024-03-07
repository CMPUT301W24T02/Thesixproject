package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
/**
 MainActivity class serves as the entry point of the application and provides options for users to navigate to different functionalities.
 Initializes UI components such as Button (generateEventButton, generateViewEventButton, generateShareQRCode) in the onCreate method.
 Sets click listeners for each button to handle navigation to corresponding activities.
 Clicking generateEventButton navigates to the OrganizerCreateActivity, allowing users to create new events.
 Clicking generateViewEventButton navigates to the EventDetailsAdapter activity, enabling users to view existing events.
 Clicking generateShareQRCode navigates to the ShareQRCodeActivity, where users can share QR codes associated with events.
**/
public class MainActivity extends AppCompatActivity {

    private Button generateEventButton;
    private Button generateViewEventButton;
    private Button generateShareQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateEventButton = findViewById(R.id.createEventButton);
        generateViewEventButton = findViewById(R.id.viewEventButton);
        generateShareQRCode = findViewById(R.id.shareQRButton);

        generateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, OrganizerCreateActivity.class));
            }
        });

        generateViewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EventDetailsAdapter.class));
            }
        });


        generateShareQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, ShareQRCodeActivity.class));
            }
        });


    }

}