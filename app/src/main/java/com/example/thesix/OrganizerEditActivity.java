package com.example.thesix;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
/** Organizer can edit a created activity
 * methods : Oncreate,Onclick,ChoosePicture,OnActivityResult
 **/

public class OrganizerEditActivity extends AppCompatActivity {

    private Button saveChangeButton;
    private Button shareInviteButton;
    private Button sharePromoButton;
    private Button backButton;
    private EditText eventDescription;
    private ImageView eventPoster;
    private Uri imageuri;

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
                choosePicture(); //Function to select images
            }
        });


    }
    // Function to select image from the storage
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageuri=data.getData();
            eventPoster.setImageURI(imageuri);
        }
    }
}
