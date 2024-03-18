package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

/**
 * AdminImagesActivity class
 * Requests necessary permissions and sets up the layout for Image listings.
 * Lists all Images in the listview created in the app.
 *
 * Continue JavaDocs
 *
 */
public class AdminImagesActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_images_screen);
    }
}