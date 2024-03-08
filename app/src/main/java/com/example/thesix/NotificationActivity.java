package com.example.thesix;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
/**
 * NotificationActivity is an activity class responsible for displaying a notification screen.
 * It provides a button for navigating back to the AttendeeListActivity.
 * @Author Danielle
 * @See AppCompatActivity
 */
public class NotificationActivity extends AppCompatActivity {
    private Button back2AttendeesButton;
    /**
     * Called when the activity is starting. Initializes UI components and sets up event listeners.
     *
     * @param savedInstanceState    If the activity is being re-initialized after previously being shut down
     *                              then this Bundle contains the data it most recently supplied in
     *                              onSaveInstanceState(Bundle). Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_screen);

        back2AttendeesButton = findViewById(R.id.noti2AttendeesButton);

        back2AttendeesButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(NotificationActivity.this, AttendeeListActivity.class));
        }
    });
}
}
