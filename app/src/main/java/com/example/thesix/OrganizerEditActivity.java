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
/**
 * OrganizerEditActivity is an activity class for editing event details and sharing event information.
 * It allows users to edit event descriptions, choose event posters, save changes, and share event invitations or promotions.
 * @Author David Lee, Arjun
 * @See AppCompatActivity
 */

public class OrganizerEditActivity extends AppCompatActivity {
    private Button saveChangeButton;
    private Button shareInviteButton;
    private Button sharePromoButton;
    private Button backButton;
    private EditText eventDescription;
    private ImageView eventPoster;
    private Uri imageuri;

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
    /**
     * Function to select image from the storage.
     */
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode   The integer request code originally supplied to startActivityForResult(),
     *                      allowing you to identify who this result came from.
     * @param resultCode    The integer result code returned by the child activity through its setResult().
     * @param data          An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
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
