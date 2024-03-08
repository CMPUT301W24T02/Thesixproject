package com.example.thesix;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
/**
 * MainActivity is the main entry point of the application.
 * It provides buttons for generating events, managing guest lists, and sharing QR codes.
 * This activity allows users to navigate to different functionalities of the application.
 * @Author: Jingru, David Lee, Danielle,Melrita
 * @See AppCompatActivity
 */
public class MainActivity extends AppCompatActivity {

    private Button generateEventButton;
    private Button generateGuestButton;
    private Button generateShareQRCode;
    /**
     * Called when the activity is starting. Initializes UI components and sets up event listeners.
     *
     * @param savedInstanceState    If the activity is being re-initialized after previously being shut down
     *                              then this Bundle contains the data it most recently supplied in
     *                              onSaveInstanceState(Bundle). Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateEventButton = findViewById(R.id.createEventButton);
        generateGuestButton = findViewById(R.id.guestListButton);
        generateShareQRCode = findViewById(R.id.shareQRButton);

        generateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, OrganizerCreateActivity.class));
            }
        });

        generateGuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, AttendeeListActivity.class));
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
