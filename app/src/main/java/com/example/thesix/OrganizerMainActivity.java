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

    //generated buttons
    private Button generateEventButton;
    private Button generateViewEventButton;


    /** Creating Organizer Main Activity Page
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_main_activity);

        generateEventButton = findViewById(R.id.createEventButton);
        generateViewEventButton = findViewById(R.id.viewEventButton);


        generateEventButton.setOnClickListener(new View.OnClickListener() {
            /** Taking user to CreateNewQrCodeActivity
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {

                startActivity(new Intent(OrganizerMainActivity.this, OrganizerUseNewQRActivity.class));
            }
        });

        generateViewEventButton.setOnClickListener(new View.OnClickListener() {
            /**         Generating event display
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrganizerMainActivity.this, EventDetailsAdapter.class));
            }
        });




    }

}