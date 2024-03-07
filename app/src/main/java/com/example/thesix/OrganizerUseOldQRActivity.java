package com.example.thesix;
import static android.app.PendingIntent.getActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class OrganizerUseOldQRActivity extends AppCompatActivity {
    ArrayList<Long> eventNumList;

    ArrayList<String> desciptionDataList;
    private Button backButton;
    private QrCodeDB firestoreHelper;
    String deviceID;
    CollectionReference QrRef;
    private ArrayAdapter<String> descriptionArrayAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_use_old_qr_screen);
        desciptionDataList = new ArrayList<>();
        eventNumList = new ArrayList<>();
        backButton = findViewById(R.id.backButton);
        firestoreHelper = new QrCodeDB();
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        QrRef = firestoreHelper.getOldQrRef(deviceID);
        descriptionArrayAdapter = new ArrayAdapter<String>(
                OrganizerUseOldQRActivity.this,
                R.layout.share_qr_view,R.id.itemTextView,desciptionDataList);
        ListView descriptionList = findViewById(R.id.qrcode_list);
        descriptionList.setAdapter(descriptionArrayAdapter);
        readData(new OldQrCallback() {
            @Override
            public void onOldQrCallback(List<String> list1, List<Long> list2) {
                desciptionDataList = (ArrayList<String>) list1;
                Log.d("callback","1"+desciptionDataList.get(0));
                Log.d("callback","2");
                eventNumList = (ArrayList<Long>) list2;
                descriptionArrayAdapter.notifyDataSetChanged();
            }
        });
        descriptionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("hihi","1: "+ String.valueOf(eventNumList.get(position)));
                Intent myIntent = new Intent(OrganizerUseOldQRActivity.this, OrganizerEditActivity.class);
                myIntent.putExtra("eventNum", eventNumList.get(position));
                startActivity(myIntent);

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerUseOldQRActivity.this, OrganizerCreateActivity.class));
            }
        });


    }
    public interface OldQrCallback {
        void onOldQrCallback(List<String> list1,List<Long> list2);
    }
    public void readData(OldQrCallback oldQrCallback) {
        QrRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                    String description = document.getString("description");
                    Long eventNum = document.getLong("eventNum");
                    String base64String = document.getString("qrImageData");

                    eventNumList.add(eventNum);
                    desciptionDataList.add(description);
                    Log.d("list",document.getId()+ "=>"+document.getData());
                    Log.d("list",desciptionDataList.get(0));
                }
                oldQrCallback.onOldQrCallback(desciptionDataList,eventNumList);

            }
        });
    }

}


