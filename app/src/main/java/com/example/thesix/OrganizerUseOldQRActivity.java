package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrganizerUseOldQRActivity extends AppCompatActivity {
    ListView qrList;
    ArrayList<String> qrDataList;
    ArrayAdapter arrayAdapter;
    CollectionReference oldQrRef;
    private QrCodeDB firestoreHelper;
    private Button backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_use_old_qr_screen);
        firestoreHelper = new QrCodeDB();
        qrList = findViewById(R.id.qrcode_list);
        qrDataList = new ArrayList<>();
        backButton = findViewById(R.id.backButton);
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        oldQrRef = firestoreHelper.getOldQrRef(deviceID);

//        oldQrRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.e("UseOldQR", error.toString());
//                    return;
//                }
//                qrDataList.clear();
//                arrayAdapter.clear();
//                for (QueryDocumentSnapshot doc:querySnapshots) {
//                    String qrImageBase64 = doc.getId();
//                    qrDataList.add(qrImageBase64);
//                    arrayAdapter.notifyDataSetChanged();
//
//
//                }
//            }
//        });
//        qrList = findViewById(R.id.qrcode_list);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerUseOldQRActivity.this, OrganizerCreateActivity.class));
            }
        });
    }

}
