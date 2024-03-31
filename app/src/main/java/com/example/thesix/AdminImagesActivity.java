package com.example.thesix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

/**
 * AdminImagesActivity class
 * Requests necessary permissions and sets up the layout for Image listings.
 * Lists all Images in the listview created in the app.
 *
 * Continue JavaDocs
 *
 */
public class AdminImagesActivity extends AppCompatActivity {
    private Button back2AdminButton;
    private ArrayList<String> imageDataList;
    private FirebaseFirestore firestore;
    private Button backButton;
    private CollectionReference eventImagesRef;
    private ArrayAdapter<String> imagesArrayAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_images_screen);
        back2AdminButton = findViewById(R.id.backButton);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        imageDataList = new ArrayList<>();
        backButton = findViewById(R.id.backButton);
        firestore = FirebaseFirestore.getInstance();
        eventImagesRef = firestore.collection("inviteQrCodes");
        imagesArrayAdapter = new ArrayAdapter<String>( AdminImagesActivity.this,
                R.layout.image_list_content, R.id.image_list, imageDataList);
        ListView imageList = findViewById(R.id.images_list_view);
        imageList.setAdapter(imagesArrayAdapter);

        /**
         * Does Read data Callback
         * @param : List<String> list1
         * @return : void
         */
        readData(new MyCallback() {
            @Override
            public void onCallback(List<String> list1) {
                imageDataList = (ArrayList<String>) list1;
                imagesArrayAdapter.notifyDataSetChanged();
            }
        });
    }
    /**
     * Interface Callback
     * @param :List<String> list1
     * @return :
     */

    public interface MyCallback {
        void onCallback(List<String> list1);
    }
    /**
     * Reads data
     * @param :MyCallback myCallback
     * @return :
     */

    public void readData(MyCallback myCallback) {
        eventImagesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String base64String = document.getString("qrImageData");
                    imageDataList.add(base64String);
                }
                myCallback.onCallback(imageDataList);
            }
        });

        /**
         Initializes a UI component, a Button named back2AdminButton
         @param :
         @return
         **/
        back2AdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminImagesActivity.this, AdminActivity.class));
            }
        });
    }
}