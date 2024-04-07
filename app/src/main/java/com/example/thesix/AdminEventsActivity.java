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


    /** Creating AdminEventsActivity page
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        //requesting for storage access
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        //creating new Arraylists
        eventnameDataList = new ArrayList<>();
        eventdescriptionDataList = new ArrayList<>();
        eventNumList = new ArrayList<>();
        eventImageDataList = new ArrayList<>();

        //finding required button ids
        backButton = findViewById(R.id.backButton);
        firestore = FirebaseFirestore.getInstance();
        eventsRef = firestore.collection("inviteQrCodes");

        //building Array Adapter
        eventnameArrayAdapter = new ArrayAdapter<String>(
                AdminEventsActivity.this,
                R.layout.event_list_textview, R.id.itemTextView, eventnameDataList);
        //creating list view
        ListView eventdescriptionList = findViewById(R.id.event_list);
        eventdescriptionList.setAdapter(eventnameArrayAdapter);

        readData(new MyCallback() {
            /** Reading Data from firebase Callback
             * @param list1 which is event data list
             * @param list2 event number list
             * @param list3 notifying array adapter
             */
            @Override
            public void onCallback(List<String> list1, List<Long> list2, List<String> list3) {
                eventnameDataList = (ArrayList<String>) list1;
                eventNumList = (ArrayList<Long>) list2;
                //notifying adapter change
                eventnameArrayAdapter.notifyDataSetChanged();
            }
        });

        eventdescriptionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /** Creating event description on click listener
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(AdminEventsActivity.this, AdminEventDetails.class); //starting new intent to AdminEventsDetails
                String eventName = (String) (eventdescriptionList.getItemAtPosition(position));

                String eventDescription = "Singh"; //example case

                //filling adapter with data
                long eventNum = eventNumList.get(position);
                for (int j = 0; j < eventnameDataList.size(); j++) {
                    String eventName1 = (String) (eventnameDataList.get(j));
                    if(eventName.equalsIgnoreCase(eventName1))
                    {
                        eventDescription = (String) (eventdescriptionDataList.get(j));
                    }
                }

                //creating a new bundle with required data
                Bundle bundle = new Bundle();
                bundle.putString("eventName", eventName);
                bundle.putString("eventDescription", eventDescription);
                bundle.putLong("eventNum", eventNum);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            /** Starting Admin Activity when clicked
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminEventsActivity.this, AdminActivity.class));
            }
        });

        eventdescriptionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /**
             * @param parent   The AbsListView where the click happened
             * @param view     The view within the AbsListView that was clicked
             * @param position The position of the view in the list
             * @param id       The row id of the item that was clicked
             * @return boolean with certain condition
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the event name, description, and number at the clicked position
                String eventName = eventnameDataList.get(position);
                String eventDescription = eventdescriptionDataList.get(position);
                Long eventNum = eventNumList.get(position);
                String base64ImageData = eventImageDataList.get(position);

                //removing when clicked
                eventnameDataList.remove(position);
                eventdescriptionDataList.remove(position);
                eventNumList.remove(position);
                eventImageDataList.remove(position);
                eventnameArrayAdapter.notifyDataSetChanged();

                // Firestore deletion
                eventsRef.whereEqualTo("eventNum", eventNum).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            /** Deleting from firestore when user indicates deletion
                             * @param queryDocumentSnapshots with required firestore data
                             */
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    document.getReference().delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {

                                                /** Success when deleting event
                                                 * @param aVoid checking whether event is deleted
                                                 */
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //toast with event deletion
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
     * Public Interface
     */
    public interface MyCallback {
        /** Callback method to check whether firestore is updated
         * @param list1 on callback parameter
         * @param list2 on callback parameter
         * @param list3 on callback parameter
         */
        void onCallback(List<String> list1, List<Long> list2, List<String> list3);
    }

    /** Reading data
     * @param myCallback with completed call from firestore
     */
    public void readData(MyCallback myCallback) {
        eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            /** On successfully querying from firestore
             * @param queryDocumentSnapshots with required document info
             */
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    //getting required document info
                    String description = document.getString("description");
                    String eventname = document.getString("name");
                    Long eventNum = document.getLong("eventNum");
                    String base64String = document.getString("qrImageData");
                    //adding to firebase
                    eventImageDataList.add(base64String);
                    eventNumList.add(eventNum);
                    eventnameDataList.add(eventname);
                    eventdescriptionDataList.add(description);
                }
                //callback with completed data
                myCallback.onCallback(eventnameDataList, eventNumList, eventImageDataList);

            }
        });
    }
}