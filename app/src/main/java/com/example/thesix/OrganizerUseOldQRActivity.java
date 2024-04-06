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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrganizerUseOldQRActivity extends AppCompatActivity {
    private EditText descriptionEditText;
    private EditText eventnameEditText;
    private Button createEventButton;
    private ArrayList<Bitmap> imageDataList;
    private QrCodeDB firestoreHelper;
    private Button backButton;

    private ImageView eventPoster;
    private Uri imageuri;
    private boolean imageChanged = false;

    private Bitmap eventImageBitmap;
    String promoQrImageData;
    String inviteQrImageData;

    private Long count;
    private String deviceID;
    Long eventNum;
    private Location location;

    @Override

    /**
     getting deviceId , creating new database
     @param :Bundle savedInstanceState
     @return Documentreference
     **/

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_use_old_qr_screen);
        descriptionEditText = findViewById(R.id.eventDescription);
        eventnameEditText = findViewById(R.id.eventName);
        createEventButton = findViewById(R.id.createEventButton);
        firestoreHelper = new QrCodeDB();
        backButton = findViewById(R.id.backButton);
        eventPoster = findViewById(R.id.eventPoster);
        Bundle bundle = getIntent().getExtras();
        eventNum = bundle.getLong("eventNum");
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        /**
         Choose picture for event
         **/
        eventPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture(); //Function to select images
            }
        });

        /**
         Creating event button , with required event details
         **/
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionEditText.getText().toString();
                String eventName = eventnameEditText.getText().toString();
                String even = eventnameEditText.getText().toString();
                String desc = descriptionEditText.getText().toString();
                if (even.matches("")) {
                    Toast.makeText(OrganizerUseOldQRActivity.this, "Please enter event name.", Toast.LENGTH_SHORT).show();
                }
                else if((desc.matches("")))
                {
                    Toast.makeText(OrganizerUseOldQRActivity.this, "Please enter event description.", Toast.LENGTH_SHORT).show();
                }
                else if (imageChanged == false)
                {
                    Toast.makeText(OrganizerUseOldQRActivity.this, "Please select an event poster.", Toast.LENGTH_SHORT).show();
                }
                else {
                    readData(new MyCallback() {
                        @Override
                        public void onCallback(long num , String inviteImageData, String promoImageData) {

                            Log.d("callback", String.valueOf(num));
                            List<Long> checkIn = new ArrayList<Long>();
                            List<String> attendeeIDList = new ArrayList<>();
                            List<String> signUpIDList = new ArrayList<>();
                            List<Location> locations = new ArrayList<>();
                            String eventImageBase64 = BitMapToString(eventImageBitmap);
                            EventDetails eventdetail = new EventDetails(eventImageBase64, inviteQrImageData, promoQrImageData, num, description, eventName,checkIn, 0L,attendeeIDList,signUpIDList, locations);
                            firestoreHelper.saveInviteQRCode(deviceID, eventdetail,num);

                        }
                    });
                    Toast.makeText(OrganizerUseOldQRActivity.this, "Your Event has been created.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /**
         Back button to take user back to main acitvity
         **/

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerUseOldQRActivity.this, OrganizerMainActivity.class));
            }
        });


    }

    /**
     Call back interface
     @param : Long num
     **/
    public interface MyCallback {
        void onCallback(long num, String inviteQrImageData, String promoQrImageData);
    }
    /**
     Reading Data from QR code
     @param : MycCallBcak callBack
     **/
    public void readData(MyCallback myCallback) {
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        firestoreHelper.getDeviceDocRef(deviceID).collection("event").document(String.valueOf(eventNum)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("count", "DocumentSnapshot data: " + document.getData());

                        promoQrImageData = (String) document.get("promoQrImageData");
                        inviteQrImageData = (String) document.get("inviteQrImageData");
                        myCallback.onCallback(eventNum,inviteQrImageData,promoQrImageData);
                    } else {
                        Log.d("count", "No such document");
                    }
                } else {
                    Log.d("count", "get failed with ", task.getException());
                }

            }
        });

    }
    /**
     Decoding QrCode to make it useable for other functions
     @param : BitMap bitmap
     @return String
     **/

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return result;

    }




    // Function to select image from the storage
    /**
     Choosing picture from device
     @param : void
     @return void
     **/
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    /**
     Setting all acquired Data to event
     @param : int requestcode , int resultcode , intentData
     @return void
     **/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            //eventPoster.setImageURI(imageuri);
            try {
                eventImageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageuri);
                eventPoster.setImageBitmap(eventImageBitmap);
                imageChanged= true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

