package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
/**
 * AdminActivity class allows organizers to view events, profiles and view images.
 * Clicking viewProfiles is not implemented yet.
 */
public class AdminActivity extends AppCompatActivity {
    //Initializing Buttons
    private Button viewEvents;
    private Button viewProfiles;
    private Button viewImages;

    /***
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_mainactivity);
        //find id's of respective Buttons
        viewEvents = findViewById(R.id.viewEvents);
        viewProfiles = findViewById(R.id.viewProfiles);
        viewImages = findViewById(R.id.viewImages);

        /***
         * Allows us to View Events when clicking on button
         */
        viewEvents.setOnClickListener(new View.OnClickListener() {
            /***
             *
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //Takes Admin to view list of all event activities
                startActivity(new Intent(AdminActivity.this, AdminEventsActivity.class));
            }
        });

        /**
         * Clicking viewProfiles navigates to display all profile created on app.
         */
        viewProfiles.setOnClickListener(new View.OnClickListener() {
            /***
             *
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //Takes Admin to view list of all profile activities
                startActivity(new Intent(AdminActivity.this, AdminProfileActivity.class));
            }
        });

        /**
         * Clicking viewImages navigates to display all profile created on app.
         */
        viewImages.setOnClickListener(new View.OnClickListener() {
            /***
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //Takes Admin to view list of all event images
                startActivity(new Intent(AdminActivity.this, AdminImagesActivity.class));
            }
        });
    }
}
