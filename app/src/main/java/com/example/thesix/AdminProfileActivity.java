package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * AdminProfileActivity class manages all attendee profile's display and navigation within an Android application.
 * Requests necessary permissions and sets up the layout for Profile listings.
 * Lists all profiles in the listview created in the app.
 *
 * Continue JavaDocs
 *
 */
public class AdminProfileActivity extends AppCompatActivity {

    private Button back2AdminButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_screen);

        back2AdminButton = findViewById(R.id.backButton);

        /**
         Initializes a UI component, a Button named back2AdminButton
         @param :
         @return
         **/
        back2AdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminProfileActivity.this, AdminActivity.class));
            }
        });
    }
}
