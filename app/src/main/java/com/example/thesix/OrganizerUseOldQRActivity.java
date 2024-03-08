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

import java.util.ArrayList;
import java.util.List;
/**
 * OrganizerUseOldQRActivity is an activity class for displaying and using old QR codes saved in Firestore database.
 * It extends AppCompatActivity and provides functionalities for retrieving and displaying old QR codes.
 * @Author Melrita, David Lee
 * @See AppCompatActivity
 */

public class OrganizerUseOldQRActivity extends AppCompatActivity {

        ArrayList<String> desciptionDataList;
        private ArrayAdapter<String> descriptionArrayAdapter;
        private QrCodeDB firestoreHelper;
        private Button backButton;
        CollectionReference citiesRef;
    /**
     * Called when the activity is starting. Initializes UI components and retrieves old QR codes from Firestore.
     *
     * @param savedInstanceState    If the activity is being re-initialized after previously being shut down
     *                              then this Bundle contains the data it most recently supplied in
     *                              onSaveInstanceState(Bundle). Note: Otherwise, it is null.
     */
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            desciptionDataList = new ArrayList<>();
            firestoreHelper = new QrCodeDB();
            String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID); //get device ID
            citiesRef = firestoreHelper.getOldQrRef(deviceID);

            descriptionArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, desciptionDataList);
            ListView descriptionList = new ListView(this);
            descriptionList.setAdapter(descriptionArrayAdapter);
            setContentView(descriptionList);

            readData(new MyCallback() {
                @Override
                public void onCallback(List<String> list) {
                    desciptionDataList.clear();
                    desciptionDataList.addAll(list);
                    descriptionArrayAdapter.notifyDataSetChanged();
                }
            });

            descriptionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    firestoreHelper.getOldQrRef(deviceID);
                }
            });

            backButton = new Button(this);
            backButton.setText("Back");
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OrganizerUseOldQRActivity.this,MainActivity.class));
                }
            });
            setContentView(backButton);
        }
    /**
     * Interface for a callback to be invoked when data is read from Firestore.
     */
        public interface MyCallback {
            void onCallback(List<String> list);
        }
    /**
     * Reads old QR code data from Firestore database and invokes the callback with the retrieved data.
     *
     * @param myCallback    The callback to be invoked with the retrieved data.
     */
        public void readData(MyCallback myCallback) {
            citiesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<String> dataList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String bit = document.getString("qrImageData");
                        dataList.add(bit);
                    }
                    myCallback.onCallback(dataList);
                }
            });
        }
    /**
     * Converts a Base64 encoded string to a Bitmap.
     *
     * @param base64String  The Base64 encoded string representing the image.
     * @return              The decoded Bitmap.
     */
        private Bitmap Base64Tobitmap(String base64String) {
            String base64Image = base64String.split(",")[1];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
    }


