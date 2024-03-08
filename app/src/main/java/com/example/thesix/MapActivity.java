package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * MapActivity class facilitates the display of a map screen within the application.
 */

public class MapActivity extends AppCompatActivity {

    private Button back2AttendeesButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_screen);

        back2AttendeesButton = findViewById(R.id.map2AttendeesButton);

        /**
         Initializes a UI component, a Button named back2AttendeesButton
         @param :
         @return
         **/
        back2AttendeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapActivity.this, AttendeeListActivity.class));
            }
        });
    }
}
