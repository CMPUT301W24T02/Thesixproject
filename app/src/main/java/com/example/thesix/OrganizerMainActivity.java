package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Main functionality of Organizer
 **/
public class OrganizerMainActivity extends AppCompatActivity {
     //generated buttons
    private Button generateEventButton;
    private Button generateViewEventButton;
    private CollectionReference collectionReference;
    private CollectionReference attendeeCol;
    private String deviceId;
    private QrCodeDB qrCodeDB;
    private ArrayList<String> eventIds;
    private HashMap<String,ArrayList<String>> signinMap;
    private String name;


    /** Creating Organizer Main Activity Page
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_main_activity);
        qrCodeDB = new QrCodeDB();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            deviceId = bundle.getString("deviceID");
        }
        signinMap = new HashMap<String,ArrayList<String>>();


        generateEventButton = findViewById(R.id.createEventButton);
        generateViewEventButton = findViewById(R.id.viewEventButton);
        collectionReference = qrCodeDB.getDeviceColRef(deviceId);
        attendeeCol = qrCodeDB.getAttendeeDB();
        //this part will  get eventid and initial signin array, will used in late loop to add listener, and array stored in Hash
        //map to compare and check if attendee have joined.
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                eventIds = new ArrayList<String>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String id = document.getId();
                    ArrayList<String> originSignInArray = (ArrayList<String>)document.get("signUpIDList");

                    signinMap.put(id,originSignInArray);
                    eventIds.add(id);
                }
            }
        });
        //This loop will add listener to all event, and check if signin array changed or not, if an attendee has sign in event
        //it will looking for that attendee's name and send notification to organizer
        if (eventIds != null) {
            for (String id : eventIds) {
                collectionReference.document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Eventlistener", "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d("Eventlistener", "Success! ");
                            ArrayList<String> signInArray = (ArrayList<String>) snapshot.get("signUpIDList");
                            if (signInArray != signinMap.get(id)) {
                                String eventName = (String) snapshot.get("name");
                                String newAttendeeId = signInArray.get(signInArray.size() - 1);
                                DocumentReference attendeedoc = attendeeCol.document(newAttendeeId);
                                attendeedoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {

                                                //token document info
                                                Log.d("Eventlistener", "DocumentSnapshot data: " + document.getData());

                                                name = (String) document.get("name");
                                                Log.d("Eventlistener", "name" + name);
                                                signInNotification(name, deviceId, eventName);
                                            } else {
                                                Log.d("Eventlistener", "No such document");
                                            }
                                        } else {
                                            Log.d("Eventlistener", "get failed with ", task.getException());
                                        }
                                    }

                                });


                            } else {
                                Log.d("Eventlistener", "Current data: null");
                            }
                        }

                    }
                });
            }
        }


        /**
         Taking user to CreateNewQrCodeActivity
         @param :
         @return void
         **/


        generateEventButton.setOnClickListener(new View.OnClickListener() {
             /** Taking user to CreateNewQrCodeActivity
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {

                startActivity(new Intent(OrganizerMainActivity.this, OrganizerUseNewQRActivity.class));
            }
        });

        /**
         Generating event display
         **/
        generateViewEventButton.setOnClickListener(new View.OnClickListener() {
            /**         Generating event display
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrganizerMainActivity.this, EventDetailsAdapter.class));
            }
        });




    }
    // this method will use organizer's deviceId and new sign in attendee's name and event name to
    //send notification to organizer's device
    void signInNotification(String name, String Token, String eventName){
        try{
            JSONObject jsonObject = new JSONObject();

            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title",eventName);
            notificationObj.put("body",name+"have joined your event");

            JSONObject dataObj = new JSONObject();
            dataObj.put("event",eventName);

            jsonObject.put("notification",notificationObj);
            jsonObject.put("data",dataObj);
            jsonObject.put("to",Token);

            callApi(jsonObject);

        }catch (Exception e){

        }
    }
    //This used to call api
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
