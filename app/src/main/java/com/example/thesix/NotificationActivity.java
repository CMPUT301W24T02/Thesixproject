package com.example.thesix;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
NotificationActivity class manages notifications within the application.
**/

public class NotificationActivity extends AppCompatActivity {
    private Button back2AttendeesButton;

    private Button sendNotificationButton;
    private EditText message;
    private QrCodeDB firestoreHelper;
    CollectionReference QrRef;
    CollectionReference tokenRef;
    String deviceID;
    private String eventName;
    long eventNum;
    private String[] deviceIdList;
    private String token;


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
        Intent mIntent = getIntent();
        eventNum = mIntent.getLongExtra("eventNum", 0);
        firestoreHelper = new QrCodeDB();
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        QrRef = firestoreHelper.getOldQrRef(deviceID);
        tokenRef = firestoreHelper.getTokenRef();



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
                QrRef.whereEqualTo("eventNum", eventNum).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        eventName = (String) document.get("name");
                                        deviceIdList = (String[]) document.get("attendeeIDList");
                                        if (deviceIdList != null) {
                                            for (String device : deviceIdList) {
                                                DocumentReference docRef = tokenRef.document(device);
                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                Log.d("TokenGetting", "DocumentSnapshot data: " + document.getData());
                                                                token = (String)document.get("token");
                                                                sendNotification(notification,token,eventName);
                                                            } else {
                                                                Log.d("TokenGetting", "No such document");
                                                            }
                                                        } else {
                                                            Log.d("TokenGetting", "get failed with ", task.getException());
                                                        }
                                                    }
                                                });


                                                }
                                            }
                                        }
                                    }
                                }

                        }
                        );
            }
        }
        );
}
    void sendNotification(String message, String Token, String eventName){
        try{
            JSONObject jsonObject = new JSONObject();

            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title",eventName);
            notificationObj.put("body",message);

            JSONObject dataObj = new JSONObject();
            dataObj.put("event",eventName);

            jsonObject.put("notification",notificationObj);
            jsonObject.put("data",dataObj);
            jsonObject.put("token",token);

            callApi(jsonObject);

        }catch (Exception e){

        }
    }
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
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });


    }
}
