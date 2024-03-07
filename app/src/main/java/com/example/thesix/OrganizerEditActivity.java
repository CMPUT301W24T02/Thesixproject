package com.example.thesix;

public class OrganizerEditActivity {
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class OrganizerEditActivity extends AppCompatActivity {
    /*
        This activity allow organizer to edit the poster and description of an event.
    */
    private Button saveChangeButton;
    private Button shareInviteButton;
    private Button sharePromoButton;
    private Button backButton;
    private EditText eventDescription;
    private ImageView eventPoster;
    private Uri imageuri;
    private Long eventNum;
    CollectionReference QrRef;
    private QrCodeDB firestoreHelper;
    String deviceID;
    String description;
    String inviteBase64;
    String promoBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create_edit_screen);
        Intent mIntent = getIntent();
        eventNum = mIntent.getLongExtra("eventNum", 0);
        Log.d("hihi","2"+ String.valueOf(eventNum));
        saveChangeButton = findViewById(R.id.saveChangesButton);
        shareInviteButton = findViewById(R.id.shareInviteButton);
        sharePromoButton = findViewById(R.id.sharePromoButton);
        backButton = findViewById(R.id.backButton);
        eventDescription = findViewById(R.id.eventDescription);
        firestoreHelper = new QrCodeDB();
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        QrRef = firestoreHelper.getOldQrRef(deviceID);
        eventPoster = findViewById(R.id.eventPoster);
        showDescription(new EditCallback() {
            @Override
            public void onEditCallback(String description) {
                Log.d("ttt","b: "+description);
                eventDescription.setText(description);
            }
        });
        saveChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        //Code to be added for where back button takes us
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        String eventDes = eventDescription.getText().toString(); //Getting event description from user and storing it as a string

        eventPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture(); //Function to select images
            }
        });


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
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageuri=data.getData();
            eventPoster.setImageURI(imageuri);
        }
    }
    private interface EditCallback {
        void onEditCallback(String description);
    }
    private interface ShareInviteCallback{
        void onShareInviteCallback(String string);
    }
    private interface SharePromoCallback{
        void onSharePromoCallback(String string);
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
    public void showDescription(EditCallback editCallback) {

        QrRef.whereEqualTo("eventNum",eventNum).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                description = document.getString("description");
                                Log.d("ttt","c: "+ description);
                                Log.d("getevent", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("getevent", "Error getting documents: ", task.getException());
                        }
                        Log.d("ttt","a: "+ description);
                        editCallback.onEditCallback(description);
                    }

                });

    }
    private Bitmap Base64Tobitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    /**
     * Saves the image as PNG to the app's private external storage folder.
     * @param image Bitmap to save.
     * @return Uri of the saved file or null
     */
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
}
