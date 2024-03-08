package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
/**
 * MapActivity is an activity class responsible for displaying a map screen.
 * It allows users to view maps and navigate back to the AttendeeListActivity.
 * @Author David Lee
 * @See AppCompatActivity
 */
public class MapActivity extends AppCompatActivity {
    /**
     * Called when the activity is starting. Initializes UI components and sets up event listeners.
     *
     * @param savedInstanceState    If the activity is being re-initialized after previously being shut down
     *                              then this Bundle contains the data it most recently supplied in
     *                              onSaveInstanceState(Bundle). Note: Otherwise, it is null.
     */
    private Button back2AttendeesButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_screen);

        back2AttendeesButton = findViewById(R.id.map2AttendeesButton);

        back2AttendeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapActivity.this, AttendeeListActivity.class));
            }
        });
    }
}
