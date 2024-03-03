package com.example.thesix;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class OrganizerEditActivity extends AppCompatActivity {
    private Button saveChangeButton;
    private Button shareInviteButton;
    private Button sharePromoButton;
    private Button backButton;
    private EditText eventDescription;
    private ImageView eventPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create_edit_screen);

        saveChangeButton = findViewById(R.id.saveChangesButton);
        shareInviteButton = findViewById(R.id.shareInviteButton);
        sharePromoButton = findViewById(R.id.sharePromoButton);
        backButton = findViewById(R.id.backButton);
        eventDescription = findViewById(R.id.eventDescription);
        eventPoster = findViewById(R.id.eventPoster);
        saveChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sharePromoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        shareInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Code to be added for where back button takes us
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        String eventDes = eventDescription.getText().toString(); //Getting event description from user and storing it as a string

        eventPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
