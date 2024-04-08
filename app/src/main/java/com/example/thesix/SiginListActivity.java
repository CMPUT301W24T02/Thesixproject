package com.example.thesix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import org.checkerframework.common.reflection.qual.NewInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SiginListActivity extends AppCompatActivity{

    ArrayList<String> nameList;
    ArrayList<String> deviceIDList;
    private ArrayAdapter<String> signUpArrayAdapter;
    private QrCodeDB firestoreHelper;
    private Button backButton;
    CollectionReference eventsRef;
    CollectionReference attendeeCol;
    private String Organizerdeviceid;
    private String deviceID;
    private Long eventNum;
    private String name;
    private int count;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_list_screen);
        firestoreHelper = new QrCodeDB();
        Bundle bundle = getIntent().getExtras();
        eventNum = bundle.getLong("eventNum");
        count = 1;

        //getting string device id
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //deviceID ="2f7daf8e12a8cb75";
        nameList = new ArrayList<>();
        deviceIDList = new ArrayList<>();
        backButton = findViewById(R.id.backButton);
        firestoreHelper = new QrCodeDB();
        eventsRef = firestoreHelper.getOldQrRef(deviceID);
        attendeeCol = firestoreHelper.getAttendeeDB();
        signUpArrayAdapter = new ArrayAdapter<String>(
                SiginListActivity.this,
                R.layout.signedup_content, R.id.signed_content, nameList);
        ListView signUpList = findViewById(R.id.signedup_list);
        signUpList.setAdapter(signUpArrayAdapter);
        eventsRef.whereEqualTo("eventNum", eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("signlist","1st");
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                deviceIDList = (ArrayList<String>) document.get("signUpIDList");
                            }
                            Log.d("signlist","first"+deviceIDList.get(0));
                        }
                        for(int j = 0; j<deviceIDList.size();j++){
                            Log.d("signlist","loop start");
                            String ID = (String) (deviceIDList.get(j));
                            Log.d("signlist",ID);
                            DocumentReference docRef = attendeeCol.document(ID);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("signlist","successful");
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            name = (String) document.get("name");
                                            Log.d("signlist",name);
                                            nameList.add(name);
                                            signUpArrayAdapter.notifyDataSetChanged();
                                        } else {
                                            name = "guest" + count;
                                            Log.d("signlist",name);
                                            nameList.add(name);
                                            count += 1;
                                            signUpArrayAdapter.notifyDataSetChanged();
                                        }
                                    }else {
                                        Log.d("signlist","not successful");
                                    }
                                }
                            });
                        }

                    }
                });


        backButton.setOnClickListener(new View.OnClickListener() {
            /** Generating guest Button , and signing them in
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //getting bundle
                Bundle bundle = new Bundle();
                bundle.putLong("eventNum", eventNum);
                bundle.putString("OrganizerdeviceID",deviceID);
                Intent myIntent = new Intent(SiginListActivity.this, AttendeeListActivity.class);
//                Log.d("hihi","Before ID: "+ eventNum);
                //myIntent.putExtra("eventNum", eventNum);
                //startActivity(myIntent);
                myIntent.putExtras(bundle);
                startActivity(myIntent);

            }
        });

    }
}
