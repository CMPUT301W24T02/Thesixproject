package com.example.thesix;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
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
 * AdminEventsActivity class manages all event names display and navigation within an Android application.
 * Requests necessary permissions and sets up the layout for event listing.
 * Lists all events in the listview created in the app.
 * Retrieves event details asynchronously from a Firebase Firestore database using the readData method.
 * Handles item clicks to navigate to another activity (AdminEventDetails) with selected event details.
 */

public class AdminEventsActivity extends AppCompatActivity {
    //initializing lists
    private ArrayList<Long> eventNumList;

    private ArrayList<String> eventnameDataList;
    private ArrayList<String> eventdescriptionDataList;
    private ArrayList<String> eventImageDataList;
    private ArrayAdapter<String> eventnameArrayAdapter;

    //initializing firestore
    private FirebaseFirestore firestore;
    private Button backButton;
    private CollectionReference eventsRef;

    /**
     * Initializes UI components like lists, adapters, and buttons
     * @param :  Bundle savedInstanceState
     * @return : void
     */

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
        firestore = FirebaseFirestore.getInstance();
        eventsRef = firestore.collection("inviteQrCodes");
        eventnameArrayAdapter = new ArrayAdapter<String>(
                AdminEventsActivity.this,
                R.layout.event_list_textview, R.id.itemTextView, eventnameDataList);
        ListView eventdescriptionList = findViewById(R.id.event_list);
        eventdescriptionList.setAdapter(eventnameArrayAdapter);

        /**
         * Does Read data Callback
         * @param : List<String> list1, List<Long> list2, List<String> list3
         * @return : void
         */
        readData(new MyCallback() {
            @Override
            public void onCallback(List<String> list1, List<Long> list2, List<String> list3) {
                eventnameDataList = (ArrayList<String>) list1;
                eventNumList = (ArrayList<Long>) list2;
                eventnameArrayAdapter.notifyDataSetChanged();
            }
        });
        /**
         * Sets Event Description
         * @param : AdapterView<?> parent, View view, int position, long id
         * @return : void
         */
        eventdescriptionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(AdminEventsActivity.this, AdminEventDetails.class);
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

                Bundle bundle = new Bundle();
                bundle.putString("eventName", eventName);
                bundle.putString("eventDescription", eventDescription);
                bundle.putLong("eventNum", eventNum);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        /**
         * Sets back button to navigate to Admin Activity
         * @param : View v
         * @return :
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminEventsActivity.this, AdminActivity.class));
            }
        });

        eventdescriptionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the event name, description, and number at the clicked position
                String eventName = eventnameDataList.get(position);
                String eventDescription = eventdescriptionDataList.get(position);
                Long eventNum = eventNumList.get(position);
                String base64ImageData = eventImageDataList.get(position);

                eventnameDataList.remove(position);
                eventdescriptionDataList.remove(position);
                eventNumList.remove(position);
                eventImageDataList.remove(position);
                eventnameArrayAdapter.notifyDataSetChanged();

                // Firestore deletion
                eventsRef.whereEqualTo("eventNum", eventNum).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    document.getReference().delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(AdminEventsActivity.this, "Event Deleted!", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            }
                        });
                return true;
            }
        });

    }
    /**
     * Interface Callback
     * @param :List<String> list1, List<Long> list2, List<String> list3
     * @return :
     */

    public interface MyCallback {
        void onCallback(List<String> list1, List<Long> list2, List<String> list3);
    }
    /**
     * Reads data
     * @param :MyCallback myCallback
     * @return :
     */

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
                }
                myCallback.onCallback(eventnameDataList, eventNumList, eventImageDataList);

            }
        });
    }
}