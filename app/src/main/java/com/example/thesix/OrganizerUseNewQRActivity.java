
package com.example.thesix;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.IOException;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 Makaing a newQr code for organizer specific to device id
 **/

public class OrganizerUseNewQRActivity extends AppCompatActivity {
    private EditText descriptionEditText;
    private EditText eventnameEditText;
    private Button createEventButton;
    private Button useOldQRButton;
    private QrCodeDB firestoreHelper;
    private Button backButton;

    private ImageView eventPoster;
    private Uri imageuri;
    private boolean imageChanged = false;

    private Bitmap eventImageBitmap;

    private Long count;
    private String deviceID;

    /** getting deviceId , creating new database
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override



    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_use_new_qr_screen);
        // Set the content view to the organizer's new QR screen layout
        // Initialize UI components
        descriptionEditText = findViewById(R.id.eventDescription);
        eventnameEditText = findViewById(R.id.eventName);
        createEventButton = findViewById(R.id.createEventButton);
        // Initialize the Firestore helper for database operations
        firestoreHelper = new QrCodeDB();
        backButton = findViewById(R.id.backButton);
        eventPoster = findViewById(R.id.eventPoster);
        useOldQRButton = findViewById(R.id.oldQRButton);
        // Retrieve the device ID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        eventPoster.setOnClickListener(new View.OnClickListener() {
            /** Choose picture for event
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                choosePicture(); //Function to select images
            }
        });
        useOldQRButton.setOnClickListener(new View.OnClickListener() {
            /**Use Old Qr code on click listener
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerUseNewQRActivity.this,OrganizerChooseOldQRActivity.class));
            }
        });

        createEventButton.setOnClickListener(new View.OnClickListener() {
            /** Creating event button , with required event details
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //string data
                String even = eventnameEditText.getText().toString();
                String desc = descriptionEditText.getText().toString();
                if (even.matches("")) {
                    Toast.makeText(OrganizerUseNewQRActivity.this, "Please enter event name.", Toast.LENGTH_SHORT).show();
                }
                else if((desc.matches("")))
                {
                    Toast.makeText(OrganizerUseNewQRActivity.this, "Please enter event description.", Toast.LENGTH_SHORT).show();
                }
                else if (imageChanged == false)
                {
                    Toast.makeText(OrganizerUseNewQRActivity.this, "Please select an event poster.", Toast.LENGTH_SHORT).show();
                }
                else {
                    readData(new MyCallback() {
                        /** query for database
                         * @param num on callback for firebase
                         */
                        @Override
                        public void onCallback(long num) {
                            Log.d("callback", String.valueOf(num));
                            try {
                                //creating string data
                                String description = descriptionEditText.getText().toString();
                                String eventName = eventnameEditText.getText().toString();
                                String inviteQrString = num+ "device id"+deviceID;
                                String promoQrString = "promo" + inviteQrString;
                                //new QRCode Writer
                                QRCodeWriter writer = new QRCodeWriter();
                                //encoding data
                                BitMatrix inviteBitMatrix = writer.encode(inviteQrString, BarcodeFormat.QR_CODE, 512, 512);
                                BitMatrix promoBitMatrix = writer.encode(promoQrString, BarcodeFormat.QR_CODE, 512, 512);
                                //creating bitmap
                                Bitmap Invitebitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565);
                                Bitmap Promobitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565);


                                for (int x = 0; x < 512; x++) {
                                    for (int y = 0; y < 512; y++) {
                                        Invitebitmap.setPixel(x, y, inviteBitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                                    }
                                }
                                for (int x = 0; x < 512; x++) {
                                    for (int y = 0; y < 512; y++) {
                                        Promobitmap.setPixel(x, y, promoBitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                                    }
                                }


                                // Convert Bitmap to Base64 String
                                String inviteQrImageBase64 = bitmapToBase64(Invitebitmap);
                                String promoQrImageBase64 = bitmapToBase64(Promobitmap);
                                String eventImageBase64 = BitMapToString(eventImageBitmap);


                                // Save invite QR Code in Firestore
                                List<String> attendeeList= new ArrayList<String>();
                                List<Long> checkIn = new ArrayList<Long>();
                                List<String> attendeeIDList = new ArrayList<>();
                                List<String> notificationList = new ArrayList<>();
                                List<String> signUpIDList = new ArrayList<>();
                                List<Location> location = new ArrayList<>();
                                EventDetails eventdetail = new EventDetails(eventImageBase64, inviteQrImageBase64, promoQrImageBase64, num, description, eventName,checkIn, 0L,attendeeIDList,signUpIDList,notificationList, location);
                                firestoreHelper.saveInviteQRCode(deviceID, eventdetail,num);

                            } catch (WriterException e) {
                                Log.e("MainActivity", "Error generating QR code", e);
                            }

                        }
                    });
                    Toast.makeText(OrganizerUseNewQRActivity.this, "Your Event has been created.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /**Back button to take user back to main acitvity
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerUseNewQRActivity.this, OrganizerMainActivity.class));
            }
        });


    }

    /**
     * interface my callback
     */
    public interface MyCallback {
        /**  Call back interface
         * @param num Callback for firebase
         */
        void onCallback(long num);
    }

    /** Reading Data from QR code
     * @param myCallback Callback for firebase
     */
    public void readData(MyCallback myCallback) {
        //getting device ID
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        firestoreHelper.getDeviceDocRef(deviceID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            //DocumentSnapshot successfully updated
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("count", "DocumentSnapshot data: " + document.getData());
                        count = (Long) document.get("inviteCount");
                        myCallback.onCallback(count);
                    } else {
                        //DocumentSnapshot not successfully updated
                        Log.d("count", "No such document");
                    }
                } else {
                    Log.d("count", "get failed with ", task.getException());
                }

            }
        });

    }


    /**
     * @param bitmap QR code bit map
     * @return String data of QR
     */
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //compressing
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        // storing into an array
        String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return result;

    }

    /** Encoding QRcode Data but PNG data
     * @param bitmap  QR code bit map
     * @return String data of QR
     */
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //compressing
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        // storing into an array
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Function to select image from the storage

    /**
     * Choosing picture from device
     */
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }


    /**Setting all acquired Data to event
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //making request of code
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            //eventPoster.setImageURI(imageuri);
            try {
                eventImageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageuri);
                eventPoster.setImageBitmap(eventImageBitmap);
                // has image been changed
                imageChanged= true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
