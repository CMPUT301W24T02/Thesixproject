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

    /**getting deviceId , creating new database
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override



    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Call the superclass onCreate method to perform any necessary setup
        super.onCreate(savedInstanceState);
        // Set the content view to the organizer's old QR screen layout
        setContentView(R.layout.organizer_use_old_qr_screen);

        // Initialize UI components
        descriptionEditText = findViewById(R.id.eventDescription);
        eventnameEditText = findViewById(R.id.eventName);
        createEventButton = findViewById(R.id.createEventButton);

        // Initialize the Firestore helper for database operations
        firestoreHelper = new QrCodeDB();
        backButton = findViewById(R.id.backButton);
        eventPoster = findViewById(R.id.eventPoster);

        // Retrieve the event number from the intent's extras
        Bundle bundle = getIntent().getExtras();
        eventNum = bundle.getLong("eventNum");

        // Retrieve the device ID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        eventPoster.setOnClickListener(new View.OnClickListener() {
            /**  Choose picture for event
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                choosePicture(); //Function to select images
            }
        });

        createEventButton.setOnClickListener(new View.OnClickListener() {
            /** Creating event button , with required event details
             * @param v The view that was clicked.
             */
            // Creating event button with required event details
            @Override
            public void onClick(View v) {
                // Retrieve event details from EditText fields
                String description = descriptionEditText.getText().toString();
                String eventName = eventnameEditText.getText().toString();
                String even = eventnameEditText.getText().toString();
                String desc = descriptionEditText.getText().toString();
                // Check if event name is empty
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
                    // Proceed with creating the event
                    readData(new MyCallback() {
                        /** Read data
                         * @param num of event number
                         * @param inviteImageData inviting qr code of event
                         * @param promoImageData promo image data  of event
                         */
                        @Override
                        public void onCallback(long num , String inviteImageData, String promoImageData) {
                            // Log event number
                            Log.d("callback", String.valueOf(num));

                            // Initialize lists
                            List<Long> checkIn = new ArrayList<Long>();
                            List<String> attendeeIDList = new ArrayList<>();
                            List<String> signUpIDList = new ArrayList<>();
                            List<Location> locations = new ArrayList<>();

                            List<String> notificationList = new ArrayList<>();
                            // Convert event poster to Base64 string
                            String eventImageBase64 = BitMapToString(eventImageBitmap);
                            // Create event details object
                            EventDetails eventdetail = new EventDetails(eventImageBase64, inviteQrImageData, promoQrImageData, num, description, eventName,checkIn, 0L,attendeeIDList,signUpIDList,notificationList, locations);
                            // Save event details to Firestore
                            firestoreHelper.saveInviteQRCode(deviceID, eventdetail,num);

                        }
                    });
                    // Show event creation success message
                    Toast.makeText(OrganizerUseOldQRActivity.this, "Your Event has been created.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /**Back button to take user back to main acitvity
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerUseOldQRActivity.this, OrganizerChooseOldQRActivity.class));
            }
        });


    }

    public interface MyCallback {
        /**
         * @param num of event number
         * @param inviteQrImageData inviting qr code of event
         * @param promoQrImageData  promo qr code of event
         */
        void onCallback(long num, String inviteQrImageData, String promoQrImageData);
    }

    /** Reading Data from QR code
     * @param myCallback Callback for firebase
     */
    public void readData(MyCallback myCallback) {
        //getting device ID
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //getting document reference
        firestoreHelper.getDeviceDocRef(deviceID).collection("event").document(String.valueOf(eventNum)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("count", "DocumentSnapshot data: " + document.getData());
                        //getting firebase data
                        promoQrImageData = (String) document.get("promoQrImageData");
                        inviteQrImageData = (String) document.get("qrImageData");
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


    /** Decoding QrCode to make it useable for other functions
     * @param bitmap of QRCode
     * @return String decoded of QRCode
     */
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //compress
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return result;

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
        //getting result okay
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

