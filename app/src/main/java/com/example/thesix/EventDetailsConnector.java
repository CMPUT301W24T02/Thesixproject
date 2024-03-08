package com.example.thesix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
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
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * EventDetailsConnector class manages the display of event details and provides options for generating guest lists within an Android application.
 * Initializes UI components such as TextView (eventName, eventDescription), ImageView (eventPoster), and Button (backButton, generateGuestButton) in the onCreate method.
 * Retrieves event details from the intent extras passed from the previous activity (eventName and eventDescription) and sets them to the respective TextViews.
 * Handles button clicks to navigate to other activities (EventDetailsAdapter, AttendeeListActivity) using intents.
 * Upon clicking the backButton, it navigates to the EventDetailsAdapter activity.
 * Upon clicking the generateGuestButton, it navigates to the AttendeeListActivity activity.
 */

public class EventDetailsConnector extends AppCompatActivity {

    private TextView eventName;
    private TextView eventDescription;
    private ImageView eventPoster;
    private Button generateGuestButton;
    private Button backButton;
    private Button shareInviteButton;
    private Button sharePromoButton;
    long eventNum;
    CollectionReference QrRef;
    private QrCodeDB firestoreHelper;
    String deviceID;
    String imageBaseString;
    String inviteBase64;
    String promoBase64;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details_connector);
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        eventPoster = findViewById(R.id.eventPoster);
        backButton = findViewById(R.id.backButton);
        generateGuestButton = findViewById(R.id.guestListButton);
        shareInviteButton = findViewById(R.id.shareInviteButton);
        sharePromoButton = findViewById(R.id.sharePromoButton);
        firestoreHelper = new QrCodeDB();
        QrRef = firestoreHelper.getOldQrRef(deviceID);

        Bundle bundle = getIntent().getExtras();
        String eventName1 = bundle.getString("eventName");
        String eventDescription1 = bundle.getString("eventDescription");
        eventNum = bundle.getLong("eventNum");
        eventName.setText(eventName1);
        eventDescription.setText(eventDescription1);
        eventPosterImage(new EventPosterCallback() {
            @Override
            public void onEventPosterCallback(String string) {
                Bitmap b=StringToBitMap(string);
                eventPoster.setImageBitmap(b);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDetailsConnector.this, EventDetailsAdapter.class));
            }
        });

        generateGuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(EventDetailsConnector.this, AttendeeListActivity.class);
                myIntent.putExtra("intVariableName", eventNum);
                startActivity(myIntent);
            }
        });

        sharePromoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePromo(new SharePromoCallback() {
                    @Override
                    public void onSharePromoCallback(String string) {
                        Bitmap bitmap = Base64Tobitmap(string);
                        Uri uri = saveImageExternal(bitmap);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.valueOf(uri)));
                        startActivity(Intent.createChooser(intent, "Share the Image ... "));
                    }
                });

            }
        });
        shareInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareInvite(new ShareInviteCallback() {
                    @Override
                    public void onShareInviteCallback(String string) {
                        Bitmap bitmap = Base64Tobitmap(string);
                        Uri uri = saveImageExternal(bitmap);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.valueOf(uri)));
                        startActivity(Intent.createChooser(intent, "Share the Image ... "));

                    }
                });



            }
        });
    }
    private interface ShareInviteCallback{
        void onShareInviteCallback(String string);
    }
    private interface SharePromoCallback{
        void onSharePromoCallback(String string);
    }

    private interface EventPosterCallback{
        void onEventPosterCallback(String string);
    }

    public void eventPosterImage(EventPosterCallback eventPosterCallback) {
        QrRef.whereEqualTo("eventNum",eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                imageBaseString = document.getString("eventImageData");

                                Log.d("getevent", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("getevent", "Error getting documents: ", task.getException());
                        }
                        eventPosterCallback.onEventPosterCallback(imageBaseString);

                    }
                });
    }

    public void shareInvite(ShareInviteCallback shareInviteCallback) {
        QrRef.whereEqualTo("eventNum",eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                inviteBase64 = document.getString("qrImageData");

                                Log.d("getevent", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("getevent", "Error getting documents: ", task.getException());
                        }
                        shareInviteCallback.onShareInviteCallback(inviteBase64);

                    }

                });
    }
    public void sharePromo(SharePromoCallback sharePromoCallback) {
        firestoreHelper.getOldQrRef2(deviceID).whereEqualTo("eventNum",eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                promoBase64 = document.getString("qrImageData");

                                Log.d("getevent", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("getevent", "Error getting documents: ", task.getException());
                        }
                        sharePromoCallback.onSharePromoCallback(promoBase64);

                    }

                });
    }
    private Bitmap Base64Tobitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    private Uri saveImageExternal(Bitmap image) {
        //TODO - Should be processed in another thread
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); //https://stackoverflow.com/questions/48117511/exposed-beyond-app-through-clipdata-item-geturi
        StrictMode.setVmPolicy(builder.build());
        Uri uri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            Log.d("save_image", "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
