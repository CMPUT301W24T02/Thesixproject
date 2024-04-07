package com.example.thesix;

import static android.app.PendingIntent.getActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *EventDetailsAdapter class manages event details display and navigation within an Android application.
 */

public class EventDetailsAdapter extends AppCompatActivity {
    ArrayList<Long> eventNumList;

    ArrayList<String> eventnameDataList;
    ArrayList<String> eventdescriptionDataList;
    ArrayList<String> eventImageDataList;
    private ArrayAdapter<String> eventnameArrayAdapter;
    private QrCodeDB firestoreHelper;
    private Button backButton;
    CollectionReference eventsRef;
    private String Organizerdeviceid;


    /** Initializes UI components like lists, adapters, and buttons
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        //asking for permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        //creating new arraylists
        eventnameDataList = new ArrayList<>();
        eventdescriptionDataList = new ArrayList<>();
        eventNumList = new ArrayList<>();
        eventImageDataList = new ArrayList<>();
        backButton = findViewById(R.id.backButton);

        //new database instance
        firestoreHelper = new QrCodeDB();


        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID); //get device ID
        String Organizerdeviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        //deviceID ="27150c669e8b1dc4";
        eventsRef = firestoreHelper.getOldQrRef(deviceID);
        eventnameArrayAdapter = new ArrayAdapter<String>(
                EventDetailsAdapter.this,
                R.layout.event_list_textview, R.id.itemTextView, eventnameDataList);
        ListView eventdescriptionList = findViewById(R.id.event_list);
        eventdescriptionList.setAdapter(eventnameArrayAdapter);


        readData(new MyCallback() {
            /** Callback Interface to share Event details
             * @param list1 event name DataList info
             * @param list2 eventNumList info
             * @param list3 event name ArrayAdapter info
             */
            @Override
            public void onCallback(List<String> list1, List<Long> list2, List<String> list3) {
                Log.d("callback", "3");
                //setting array lists
                eventnameDataList = (ArrayList<String>) list1;
                eventNumList = (ArrayList<Long>) list2;
                //Log.d("callback", "1" + eventnameDataList.get(0));
                //Log.d("callback", "2");
                eventnameArrayAdapter.notifyDataSetChanged();
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

                //creating intents
                Intent i = new Intent(EventDetailsAdapter.this, EventDetailsConnector.class);
                String eventName = (String) (eventdescriptionList.getItemAtPosition(position));
                String eventDescription = "Singh";
                long eventNum = eventNumList.get(position);

                //parsing through event data lists
                for (int j = 0; j < eventnameDataList.size(); j++) {
                    String eventName1 = (String) (eventnameDataList.get(j));
                     if(eventName.equalsIgnoreCase(eventName1))
                     {
                         eventDescription = (String) (eventdescriptionDataList.get(j));
                     }
                }

                //String Organizerdeviceid="27150c669e8b1dc4";
                //test case example
                Bundle bundle = new Bundle();
                bundle.putString("eventName", eventName);
                bundle.putString("eventDescription", eventDescription);
                bundle.putLong("eventNum", eventNum);
                bundle.putString("OrganizerdeviceID",Organizerdeviceid);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            /** Clicking backButton navigates back to MainActivity.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDetailsAdapter.this, OrganizerMainActivity.class));
            }
        });

    }

    public interface MyCallback {
        /** Callback Interface to share Event details
         * @param list1 event num info
         * @param list2 event data info
         * @param list3 array adapter info
         */
        void onCallback(List<String> list1, List<Long> list2, List<String> list3);
    }


    /** Reading Data from QR code
     * @param myCallback callback for firebase
     */
    public void readData(MyCallback myCallback) {
        eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    //string  description for documents
                    String description = document.getString("description");
                    String eventname = document.getString("name");
                    Long eventNum = document.getLong("eventNum");
                    String base64String = document.getString("qrImageData");

                    //event info adding to firebase
                    eventImageDataList.add(base64String);
                    eventNumList.add(eventNum);
                    eventnameDataList.add(eventname);
                    eventdescriptionDataList.add(description);
                    Log.d("list", document.getId() + "=>" + document.getData());
                    Log.d("list", eventnameDataList.get(0));
                }
                myCallback.onCallback(eventnameDataList, eventNumList, eventImageDataList);

            }
        });
    }

    /** Coverts  string to bitmap
     * @param base64String of string to 64
     * @return Bitmap of converted string
     */
    private Bitmap Base64Tobitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    /** Sharing Bitmap
     * @param view of Sharing Bitmap
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
            //getting file file
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png");
            FileOutputStream stream = new FileOutputStream(file);
            //compressing image
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            Log.d("save_image", "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }

}