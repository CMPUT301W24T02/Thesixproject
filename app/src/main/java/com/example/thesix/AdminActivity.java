package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
/**
 * AdminActivity class allows organizers to view vents, profiles and view images.
 * Clicking viewProfiles is not implemented yet.
 */
public class AdminActivity extends AppCompatActivity {
    private Button viewEvents;
    private Button viewProfiles;
    private Button viewImages;

    /**
     *  * Initializes UI components such as Button (viewEvents,viewProfile, viewImages) in the onCreate method.
     * @param : Bundle Saved Instances
     * @return : void
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_mainactivity);

        viewEvents = findViewById(R.id.viewEvents);
        viewProfiles = findViewById(R.id.viewProfiles);
        viewImages = findViewById(R.id.viewImages);

        /**
         * Clicking viewEvents navigates to AdminEventsActivity to display all events created on app.
         * @param : View v
         * @return : void
         */

        viewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AdminEventsActivity.class));
            }
        });
        /**
         * Clicking viewEvents navigates to diaply all profile created on app.
         * @param : View v
         * @return : void
         */

        viewProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        /**
         * Clicking viewImages navigates to diaply all profile created on app.
         * @param : View v
         * @return : void
         */

        viewImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
