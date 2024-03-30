package com.example.thesix;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;


public class AttendeePromoActivity extends AppCompatActivity {
    private TextView eventName;
    private TextView eventDescription;
    private ImageView eventPoster;
    private Button backButton;
    private Button signUpButton;
    public QrCodeDB firestoreHelper;
    public Long eventNum;
    public String organizerID;
    List<String> signUpIDList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_promo_activity);
        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        eventPoster = findViewById(R.id.eventPoster);
        backButton = findViewById(R.id.backButton);
        signUpButton = findViewById(R.id.signUpButton);
        firestoreHelper = new QrCodeDB();
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String description = bundle.getString("description");
        String imageData = bundle.getString("imageData");
        organizerID = bundle.getString("organizerID");
        eventNum = bundle.getLong("eventNum");
        Log.d("showing",name+description);
        eventName.setText(name);
        eventDescription.setText(description);
        Bitmap bitmap = StringToBitMap(imageData);
        eventPoster.setImageBitmap(bitmap);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeePromoActivity.this, AttendeeMainActivity.class));
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(new SignUpCallback() {
                    @Override
                    public void onSignUpCallback(List<String> signUpIDList) {
                        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        if (signUpIDList.contains(deviceID)) {

                        }
                        else {
                            signUpIDList.add(deviceID);
                        }
                        firestoreHelper.getDeviceDocRef(organizerID).collection("event").document(String.valueOf(eventNum))
                                .update("signUpIDList", signUpIDList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("update", "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("update", "Error updating document", e);
                                    }
                                });
                    }
                });

            }
        });

    }
    private interface SignUpCallback {

        void onSignUpCallback(List<String> signUpIDList);
    }
    private void signUp(SignUpCallback signUpCallback) {
        firestoreHelper.getDeviceDocRef(organizerID).collection("event")
                .document(String.valueOf(eventNum)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                signUpIDList = (List<String>) document.get("signUpIDList");
                                signUpCallback.onSignUpCallback(signUpIDList);
                                Log.d("signup", "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d("signup", "No such document");
                            }
                        } else {
                            Log.d("signup", "get failed with ", task.getException());
                        }
                    }
                });
    }
    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }


}
