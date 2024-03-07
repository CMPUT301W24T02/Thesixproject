package com.example.thesix;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
/**
NotificationActivity class manages notifications within the application.
        Initializes UI components, Button (back2AttendeesButton, sendNotificationButton), in the onCreate method.
        Sets a click listener for the back2AttendeesButton button to navigate back to the AttendeeListActivity.
        Upon clicking the back2AttendeesButton, it starts an intent to navigate to the AttendeeListActivity.
        Provides an empty click listener for the sendNotificationButton, which would handle the functionality to send notifications.
**/

public class NotificationActivity extends AppCompatActivity {
    private Button back2AttendeesButton;

    private Button sendNotificationButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_screen);

        back2AttendeesButton = findViewById(R.id.noti2AttendeesButton);
        sendNotificationButton = findViewById(R.id.sendNotificationButton);

        back2AttendeesButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(NotificationActivity.this, AttendeeListActivity.class));
        }
    });
        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
}
}
