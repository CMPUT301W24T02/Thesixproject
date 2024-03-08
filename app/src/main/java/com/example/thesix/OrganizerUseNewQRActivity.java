
package com.example.thesix;

import android.content.Intent;
import android.graphics.Bitmap;
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
import java.util.List;

public class OrganizerUseNewQRActivity extends AppCompatActivity {
    private EditText descriptionEditText;
    private EditText eventnameEditText;
    private Button createEventButton;
    private QrCodeDB firestoreHelper;
    private Button backButton;

    private ImageView eventPoster;
    private Uri imageuri;
    private boolean imageChanged = false;

    private Bitmap eventImageBitmap;

    public Long count;

    @Override


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_use_new_qr_screen);
        descriptionEditText = findViewById(R.id.eventDescription);
        eventnameEditText = findViewById(R.id.eventName);
        createEventButton = findViewById(R.id.createEventButton);
        firestoreHelper = new QrCodeDB();
        backButton = findViewById(R.id.backButton);
        eventPoster = findViewById(R.id.eventPoster);
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        eventPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture(); //Function to select images
            }
        });

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        @Override
                        public void onCallback(long num) {
                            Log.d("callback", String.valueOf(num));
                            try {
                                String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID); //get device ID
                                String description = descriptionEditText.getText().toString();
                                String eventname = eventnameEditText.getText().toString();
                                String inviteQrString = deviceID + description + num;
                                String promoQrString = "promo" + inviteQrString;
                                QRCodeWriter writer = new QRCodeWriter();
                                BitMatrix inviteBitMatrix = writer.encode(inviteQrString, BarcodeFormat.QR_CODE, 512, 512);
                                BitMatrix promoBitMatrix = writer.encode(promoQrString, BarcodeFormat.QR_CODE, 512, 512);
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
                                EventDetails eventdetail = new EventDetails(eventImageBase64, inviteQrImageBase64, promoQrImageBase64, num, description, eventname);
                                firestoreHelper.saveInviteQRCode(deviceID, eventdetail);
                                firestoreHelper.savePromoQRCode(deviceID, eventdetail);
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
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerUseNewQRActivity.this, OrganizerMainActivity.class));
            }
        });


    }

    public interface MyCallback {
        void onCallback(long num);
    }

    public void readData(MyCallback myCallback) {
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        firestoreHelper.getDocRef(deviceID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("count", "DocumentSnapshot data: " + document.getData());
                        count = (Long) document.get("inviteCount");
                        myCallback.onCallback(count);
                    } else {
                        Log.d("count", "No such document");
                    }
                } else {
                    Log.d("count", "get failed with ", task.getException());
                }

            }
        });

    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return result;

    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Function to select image from the storage
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

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
