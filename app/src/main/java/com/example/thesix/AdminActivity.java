package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
/**
 * AdminActivity class allows organizers to view vents, profiles and view images.
 * Initializes UI components such as Button (viewEvents,viewProfile, viewImages) in the onCreate method.
 * Sets click listeners for each button to handle navigation to corresponding activities.
 * Clicking viewEvents navigates to AdminEventsActivity to display all events created on app.
 * Clicking viewImages is not implemented yet.
 * Clicking viewProfiles is not implemented yet.
 */
public class AdminActivity extends AppCompatActivity {
    private Button viewEvents;
    private Button viewProfiles;
    private Button viewImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_mainactivity);

        viewEvents = findViewById(R.id.viewEvents);
        viewProfiles = findViewById(R.id.viewProfiles);
        viewImages = findViewById(R.id.viewImages);

        viewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AdminEventsActivity.class));
            }
        });

        viewProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        viewImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
