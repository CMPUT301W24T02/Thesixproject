package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
/**
 * Main functionality of Organizer
 **/
public class OrganizerMainActivity extends AppCompatActivity {

    private Button generateEventButton;
    private Button generateViewEventButton;

    /**
     *Creating eventbutton
     @param :
     @return void
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_main_activity);

        generateEventButton = findViewById(R.id.createEventButton);
        generateViewEventButton = findViewById(R.id.viewEventButton);

        /**
         Taking user to CreateNewQrCodeActivity
         @param :
         @return void
         **/


        generateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(OrganizerMainActivity.this, OrganizerUseNewQRActivity.class));
            }
        });

        /**
         Generating event display
         **/
        generateViewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrganizerMainActivity.this, EventDetailsAdapter.class));
            }
        });




    }

}