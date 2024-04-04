package com.example.thesix;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
NotificationActivity class manages notifications within the application.
**/

public class NotificationActivity extends AppCompatActivity {
    private Button back2AttendeesButton;

    private Button sendNotificationButton;
    private EditText message;

    /**
     Initializes UI components, Button (back2AttendeesButton, sendNotificationButton), in the onCreate method.
     @param : Bundle saved instanceData
     @return void
     **/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_screen);

        back2AttendeesButton = findViewById(R.id.noti2AttendeesButton);
        sendNotificationButton = findViewById(R.id.sendNotificationButton);
        message = findViewById(R.id.sendNotification);


        /**
         Sets a click listener for the back2AttendeesButton button to navigate back to the AttendeeListActivity.
         **/

        back2AttendeesButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(NotificationActivity.this, AttendeeListActivity.class));
        }
    });
        /**
         Provides an empty click listener for the sendNotificationButton, which would handle the functionality to send notifications.
         @param :
         @return
         **/
        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notification  = message.getText().toString();



            }
        });
}
//    void sendNotification(String message){
//        FirebaseUtil
//    }
    void callApi(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAArB3cQ50:APA91bG_togOY7bXrsTTB4-odg_57yUVbu3kRJXeRKDOR_yo7D9YJ_u13JxNRDxcpTg_Ryo4Zy7aJUoVKWEOiXUng7z_Hu4YG-388eOWVdAVwICh2JDC78uknlcbbl-HyvGukJ__kINK")
                .build();
        client.newCall(request);


    }
}
