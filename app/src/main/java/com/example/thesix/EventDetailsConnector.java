package com.example.thesix;

import android.Manifest;
import android.content.Context;
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
import androidx.core.content.FileProvider;

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
import java.util.Objects;

/**
 * EventDetailsConnector class manages the display of event details and provides options for generating guest lists within an Android application.
 */

public class EventDetailsConnector extends AppCompatActivity {

    private TextView eventName; // TextView to display the event name
    private TextView eventDescription; // TextView to display the event description
    private ImageView eventPoster;
    private Button generateGuestButton;
    private Button backButton;
    private Button shareInviteButton;
    private Button sharePromoButton;
    long eventNum; // Unique identifier for the event
    CollectionReference QrRef;
    private QrCodeDB firestoreHelper;
    String deviceID; // Device ID of the current user
    String OrganizerdeviceID; // Base string for the event image
    String imageBaseString;
    String inviteBase64; // Base64 representation of the event invite
    String promoBase64;


    /** initializes UI components such as TextView (eventName, eventDescription), ImageView (eventPoster), and Button (backButton, generateGuestButton)
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details_connector);
        // getting device ID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Gets the reference to the QR code collection for the current device ID
        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        eventPoster = findViewById(R.id.eventPoster);
        backButton = findViewById(R.id.backButton);
        generateGuestButton = findViewById(R.id.guestListButton);
        shareInviteButton = findViewById(R.id.shareInviteButton);
        sharePromoButton = findViewById(R.id.sharePromoButton);
        firestoreHelper = new QrCodeDB();
        QrRef = firestoreHelper.getOldQrRef(deviceID);

        //getting bundle with info
        Bundle bundle = getIntent().getExtras();
        String eventName1 = bundle.getString("eventName");
        String eventDescription1 = bundle.getString("eventDescription");
        OrganizerdeviceID = bundle.getString("OrganizerdeviceID");
        eventNum = bundle.getLong("eventNum");
        eventName.setText(eventName1);
        eventDescription.setText(eventDescription1);


        eventPosterImage(new EventPosterCallback() {

            /** Sets Image to Bitmap
             * @param string  String of bitmap
             */
            @Override
            public void onEventPosterCallback(String string) {
                Bitmap b=StringToBitMap(string);
                eventPoster.setImageBitmap(b);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /** Naviagtes User Back to EventDetails Adapter
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDetailsConnector.this, EventDetailsAdapter.class));
            }
        });


        generateGuestButton.setOnClickListener(new View.OnClickListener() {
            /** Generating guest Button , and signing them in
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //getting bundle
                Bundle bundle = new Bundle();
                bundle.putLong("eventNum", eventNum);
                bundle.putString("OrganizerdeviceID",OrganizerdeviceID);
                Intent myIntent = new Intent(EventDetailsConnector.this, AttendeeListActivity.class);
                Log.d("hihi","Before ID: "+ eventNum);
                //myIntent.putExtra("eventNum", eventNum);
                //startActivity(myIntent);
                myIntent.putExtras(bundle);
                startActivity(myIntent);

            }
        });

        sharePromoButton.setOnClickListener(new View.OnClickListener() {
            /** Allows user to Share promo code
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                sharePromo(new SharePromoCallback() {
                    /** Callback for firebase
                     * @param string  for string of database
                     */
                    @Override
                    public void onSharePromoCallback(String string) {
                        Bitmap bitmap = Base64Tobitmap(string);
                        shareImage(bitmap);
                    }
                });

            }
        });

        shareInviteButton.setOnClickListener(new View.OnClickListener() {
            /** Allows for User to Share InviteQRCode
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                shareInvite(new ShareInviteCallback() {
                    /** Allows for User to Share InviteQRCode
                     * @param string for onshareInviteCallback
                     */
                    @Override
                    public void onShareInviteCallback(String string) {
                        Bitmap bitmap = Base64Tobitmap(string);
                        shareImage(bitmap);

                    }
                });



            }
        });
    }

    private interface ShareInviteCallback{
        /**Callback Interface to share InviteQRCod
         * @param string for inviteCallback
         */
        void onShareInviteCallback(String string);
    }

    private interface SharePromoCallback{
        /**Callback Interface to share PromoQrcode
         * @param string with promo info
         */
        void onSharePromoCallback(String string);
    }


    private interface EventPosterCallback{
        /** Callback Interface to share EventPosterCallBack
         * @param string of event poster call back
         */
        void onEventPosterCallback(String string);
    }

    /** Getting eventPoster Image
     * @param eventPosterCallback event poster call back for firebase
     */
    public void eventPosterImage(EventPosterCallback eventPosterCallback) {
        QrRef.whereEqualTo("eventNum",eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /**with successful data querying
                     * @param task query for database
                     */
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

    /**  Allows user to Share Invite
     * @param shareInviteCallback  call back for firebase
     */
    public void shareInvite(ShareInviteCallback shareInviteCallback) {
        QrRef.whereEqualTo("eventNum",eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /**with successful data querying
                     * @param task  query for databas
                     */
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
    /**
     Allows user to Share PromoQR
     @param : SharePromoCallback SharePromoCallback
     @return void
     **/
    public void sharePromo(SharePromoCallback sharePromoCallback) {
        firestoreHelper.getOldQrRef2(deviceID).whereEqualTo("eventNum",eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                promoBase64 = document.getString("promoQrImageData");

                                Log.d("getevent", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("getevent", "Error getting documents: ", task.getException());
                        }
                        sharePromoCallback.onSharePromoCallback(promoBase64);

                    }

                });
    }
    /**
     Coverts  string to bitmap
     @param : String base64String
     @return :Bitmap
     **/
    private Bitmap Base64Tobitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private void shareImage(Bitmap bitmap) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); //https://stackoverflow.com/questions/48117511/exposed-beyond-app-through-clipdata-item-geturi
        StrictMode.setVmPolicy(builder.build());
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/png");
        Uri bmpUri;
        String textToShare = "Share Tutorial";
        bmpUri=saveImage(bitmap,getApplicationContext());
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.putExtra(Intent.EXTRA_STREAM,bmpUri);
        share.putExtra(Intent.EXTRA_SUBJECT,"New App");
        share.putExtra(Intent.EXTRA_TEXT,textToShare);
        startActivity(Intent.createChooser(share,"Share Content"));
    }
    private static Uri saveImage(Bitmap image, Context context) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); //https://stackoverflow.com/questions/48117511/exposed-beyond-app-through-clipdata-item-geturi
        StrictMode.setVmPolicy(builder.build());
        File imagesFolder = new File(context.getCacheDir(),"images");
        Uri uri = null;
        try{
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "shared_images.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG,90,stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()),"com.example.thesix"+".provider",file);
        }
        catch(IOException e) {
            Log.d("TAG","Exception"+e.getMessage());
        }
        return uri;
    }
    /**
     Coverts  string to bitmap
     @param : String base64String
     @return :Bitmap
     **/
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
