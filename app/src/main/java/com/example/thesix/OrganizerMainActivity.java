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
        generateEventButton = findViewById(R.id.createEventButton);
        generateViewEventButton = findViewById(R.id.viewEventButton);
        collectionReference = qrCodeDB.getDeviceColRef(deviceId);
        attendeeCol = qrCodeDB.getAttendeeDB();
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

}
