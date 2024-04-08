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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * organizer chooses old QR activity
 */
public class OrganizerChooseOldQRActivity  extends AppCompatActivity {
    ArrayList<Long> eventNumList;

    ArrayList<String> eventnameDataList; //ArrayList to store the list of event names.


    ArrayList<Bitmap> qrDataList;
    String deviceID;
    private QrCodeDB firestoreHelper;
    private Button backButton; //Button to navigate back.
    CollectionReference eventsRef; //Reference to the collection of events.

    private CustomImageAdapter imagesArrayAdapter;

    /**Initializes UI components like lists, adapters, and buttons
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        //request permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        eventnameDataList = new ArrayList<>();
        eventNumList = new ArrayList<>();
        qrDataList = new ArrayList<>();

        //get device id
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        //initialize back button
        backButton = findViewById(R.id.backButton);
        firestoreHelper = new QrCodeDB();
        eventsRef = firestoreHelper.getDeviceColRef(deviceID);
        imagesArrayAdapter = new CustomImageAdapter(OrganizerChooseOldQRActivity.this, qrDataList);

        //listview
        ListView eventdescriptionList = findViewById(R.id.event_list);
        eventdescriptionList.setAdapter(imagesArrayAdapter);

        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID); //get device ID

//        eventsRef = firestoreHelper.getOldQrRef(deviceID);
//        eventnameArrayAdapter = new ArrayAdapter<String>(
//                OrganizerChooseOldQRActivity.this,
//                R.layout.event_list_textview, R.id.itemTextView, eventnameDataList);
//        ListView eventdescriptionList = findViewById(R.id.event_list);
//        eventdescriptionList.setAdapter(eventnameArrayAdapter);


        /**
         Callback Interface to share Event details
         @param : String
         @return
         **/
        readData(new MyCallback() {
            /** Callback Interface to share Event details
             * @param list1 event name Data List
             * @param list2 event Num data List
             * @param list3 qr Data List
             */
            @Override
            public void onCallback(List<String> list1, List<Long> list2, List<Bitmap> list3) {
                Log.d("callback", "3");
                eventnameDataList = (ArrayList<String>) list1;
                eventNumList = (ArrayList<Long>) list2;
                qrDataList = (ArrayList<Bitmap>) list3;
                //Log.d("callback", "1" + eventnameDataList.get(0));
                //Log.d("callback", "2");
                imagesArrayAdapter.notifyDataSetChanged();
            }
        });

        eventdescriptionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**Setting Event Details
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(new Intent(EventDetailsAdapter.this, EventDetailsConnector.class));
                //creating new intens
                Intent i = new Intent(OrganizerChooseOldQRActivity.this, OrganizerUseOldQRActivity.class);

                long eventNum = eventNumList.get(position);


                Bundle bundle = new Bundle();

                bundle.putLong("eventNum", eventNum);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /** clicking backButton navigates back to MainActivity.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerChooseOldQRActivity.this, OrganizerUseNewQRActivity.class));
            }
        });

    }
    public interface MyCallback {
        /**
         * @param list1 event name Data List
         * @param list2 event Num data List
         * @param list3 qr Data List
         *
         */
        void onCallback(List<String> list1, List<Long> list2, List<Bitmap> list3);
    }

    /** Reading Data from QR code
     * @param myCallback callback for firebase
     */
    public void readData(MyCallback myCallback) {
        eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    // Retrieve event details from the document
                    String eventname = document.getString("name");
                    Long eventNum = document.getLong("eventNum");
                    String base64String = document.getString("qrImageData");
                    // Convert the base64 string to a Bitmap
                    Bitmap bitmap = decodeBase64(base64String);
                    // Add the event data to the respective lists
                    qrDataList.add(bitmap);
                    eventNumList.add(eventNum);
                    eventnameDataList.add(eventname);

                    Log.d("list", document.getId() + "=>" + document.getData());
                    Log.d("list", eventnameDataList.get(0));
                }
                myCallback.onCallback(eventnameDataList, eventNumList, qrDataList);

            }
        });
    }
    /**
     * Converts a base64 string to a Bitmap.
     *
     * @param base64String The base64-encoded string representing the image.
     * @return The decoded Bitmap.
     */
    private Bitmap Base64Tobitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    /**
     * @param view view of Bitmap
     */
    public void ShareBitmap(View view) {

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
    /**
     * Decodes a base64 string into a Bitmap.
     *
     * @param base64String The base64-encoded string representing the image.
     * @return The decoded Bitmap.
     */
    private Bitmap decodeBase64(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
