package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 *
 *
 * Continue JavaDocs
 *
 */
public class AttendeeProfileActivity extends AppCompatActivity {

    private Button back2AttendeeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_profile_screen);


        back2AttendeeButton = findViewById(R.id.backButton);

        /**
         Initializes a UI component, a Button named back2AttendeeButton
         @param :
         @return
         **/
        back2AttendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeProfileActivity.this, AttendeeMainActivity.class));
            }
        });
    }
}
