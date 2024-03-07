package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button generateEventButton;
    private Button generateViewEventButton;
    private Button generateShareQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateEventButton = findViewById(R.id.createEventButton);
        generateViewEventButton = findViewById(R.id.viewEventButton);
        generateShareQRCode = findViewById(R.id.shareQRButton);

        generateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, OrganizerCreateActivity.class));
            }
        });

        generateViewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EventDetailsAdapter.class));
            }
        });


        generateShareQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, ShareQRCodeActivity.class));
            }
        });


    }

}