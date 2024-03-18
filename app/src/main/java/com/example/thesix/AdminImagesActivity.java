package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * AdminImagesActivity class
 * Requests necessary permissions and sets up the layout for Image listings.
 * Lists all Images in the listview created in the app.
 *
 * Continue JavaDocs
 *
 */
public class AdminImagesActivity extends AppCompatActivity {
    private Button back2AdminButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_images_screen);

        back2AdminButton = findViewById(R.id.backButton);

        /**
         Initializes a UI component, a Button named back2AdminButton
         @param :
         @return
         **/
        back2AdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminImagesActivity.this, AdminActivity.class));
            }
        });
    }
}