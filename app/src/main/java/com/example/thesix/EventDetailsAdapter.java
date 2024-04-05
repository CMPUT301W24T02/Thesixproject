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

    /**
     Initializes UI components like lists, adapters, and buttons
     **/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        eventnameDataList = new ArrayList<>();
        eventdescriptionDataList = new ArrayList<>();
        eventNumList = new ArrayList<>();
        eventImageDataList = new ArrayList<>();
        backButton = findViewById(R.id.backButton);
        firestoreHelper = new QrCodeDB();


        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID); //get device ID
        String Organizerdeviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceID ="27150c669e8b1dc4";
        eventsRef = firestoreHelper.getOldQrRef(deviceID);
        eventnameArrayAdapter = new ArrayAdapter<String>(
                EventDetailsAdapter.this,
                R.layout.event_list_textview, R.id.itemTextView, eventnameDataList);
        ListView eventdescriptionList = findViewById(R.id.event_list);
        eventdescriptionList.setAdapter(eventnameArrayAdapter);

        /**
         Callback Interface to share Event details
         @param : String
         @return
         **/
        readData(new MyCallback() {
            @Override
            public void onCallback(List<String> list1, List<Long> list2, List<String> list3) {
                Log.d("callback", "3");
                eventnameDataList = (ArrayList<String>) list1;
                eventNumList = (ArrayList<Long>) list2;
                //Log.d("callback", "1" + eventnameDataList.get(0));
                //Log.d("callback", "2");
                eventnameArrayAdapter.notifyDataSetChanged();
            }
        });
        /**
         Setting Event Details
         @param : AdapterView parent, View view, int position, long id
         @return :void
         **/
        eventdescriptionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(new Intent(EventDetailsAdapter.this, EventDetailsConnector.class));

                Intent i = new Intent(EventDetailsAdapter.this, EventDetailsConnector.class);
                String eventName = (String) (eventdescriptionList.getItemAtPosition(position));
                String eventDescription = "Singh";
                long eventNum = eventNumList.get(position);
                for (int j = 0; j < eventnameDataList.size(); j++) {
                    String eventName1 = (String) (eventnameDataList.get(j));
                     if(eventName.equalsIgnoreCase(eventName1))
                     {
                         eventDescription = (String) (eventdescriptionDataList.get(j));
                     }
                }
                String Organizerdeviceid="27150c669e8b1dc4";
                Bundle bundle = new Bundle();
                bundle.putString("eventName", eventName);
                bundle.putString("eventDescription", eventDescription);
                bundle.putLong("eventNum", eventNum);
                bundle.putString("OrganizerdeviceID",Organizerdeviceid);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        /**
         Clicking backButton navigates back to MainActivity.
         @param: View v
         @return void
         **/
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDetailsAdapter.this, OrganizerMainActivity.class));
            }
        });

    }
    /**
     Callback Interface to share Event details
     @param : String
     @return
     **/

    public interface MyCallback {
        void onCallback(List<String> list1, List<Long> list2, List<String> list3);
    }
    /**
     Reading Data from QR code
     @param : MycCallBcak callBack
     **/

    public void readData(MyCallback myCallback) {
        eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String description = document.getString("description");
                    String eventname = document.getString("name");
                    Long eventNum = document.getLong("eventNum");
                    String base64String = document.getString("qrImageData");
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
    /**
     Coverts  string to bitmap
     @param : String base64String
     @return :Bitmap
     **/
    private Bitmap Base64Tobitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

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

}