package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AttendeeMainActivity extends AppCompatActivity {
    private Button scanButton;
    TextView testing;
    private QrCodeDB firestoreHelper;
    String contents;
    String[] contentsArray;
    String imageData,name,description;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_main_activity);
        scanButton = findViewById(R.id.scanButton);
        testing = findViewById(R.id.textView);
        firestoreHelper = new QrCodeDB();
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://www.youtube.com/watch?v=bWEt-_z7BOY
                IntentIntegrator intentIntegrator = new IntentIntegrator(AttendeeMainActivity.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan a QR Code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });
    }
    //https://www.youtube.com/watch?v=bWEt-_z7BOY
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult != null) {
            contents = intentResult.getContents();
            Log.d("scanner",contents);
            if(contents != null) {
                //String inviteQrString = num+ "device id"+deviceID;

                if (contents.startsWith("promo")) {
                    contents = contents.replace("promo","");
                    testing.setText(contents);
                    contentsArray = contents.split("device id", 2);
                    Log.d("scanner", contentsArray[0] + contentsArray[1]);
                    promoData(new PromoDataCallback() {
                        @Override
                        public void onPromoDataCallback(String imageData, String name, String description) {
                            Log.d("qwert",name+description);
                            Intent i = new Intent(AttendeeMainActivity.this, AttendeePromoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("imageData", imageData);
                            bundle.putString("name", name);
                            bundle.putString("description", description);
                            i.putExtras(bundle);
                            startActivity(i);
                        }
                    });


                }
                else {
                    contentsArray = contents.split("device id", 2);
                    Log.d("scanner", contentsArray[0] + contentsArray[1]);
                    testing.setText(contents);
                }

            }
        }
    }
    private interface PromoDataCallback {
        void onPromoDataCallback(String imageData, String name, String description);
    }

    public void promoData(PromoDataCallback promoDataCallback) {
        firestoreHelper.getDeviceDocRef(contentsArray[1])
                .collection("event")
                .whereEqualTo("eventNum",Long.parseLong(contentsArray[0]))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                imageData = document.getString("eventImageData");
                                name = document.getString("name");
                                description = document.getString("description");
                                Log.d("promo", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("promo", "Error getting documents: ", task.getException());
                        }
                        promoDataCallback.onPromoDataCallback(imageData,name,description);
                    }
                });
    }
}
