package com.example.thesix;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.*;

import android.location.Location;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.utils.Asserts;

import java.util.ArrayList;
import java.util.List;

public class QRCodeDBTest {
    FirebaseFirestore firestoreMock;
    List<Long> test1=new ArrayList<>();
    List<String> test2=new ArrayList<>();
    List<String> test3=new ArrayList<>();
    List<String> test4=new ArrayList<>();
    List<Location> test5 =new ArrayList<>();

    EventDetails eventDetails = new EventDetails("a","a","a", 1L,"a","a",test1,32L,test2,test3,test4,test5);

    @InjectMocks
    QrCodeDB qrCodeDB;
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize mocks

        when(firestoreMock.collection(anyString())).thenReturn(mock(CollectionReference.class));
    }

    @Test
    public void testSaveInviteQRCode() {
        // Mock the behavior of FirebaseFirestore
        //when(firestoreMock.collection(anyString())).thenReturn(mock(CollectionReference.class));
        // Your test code here
        // Call the method you want to test on qrCodeDB
        //qrCodeDB.saveInviteQRCode("deviceID", eventDetails, 123L);

        assertNotNull(eventDetails);
        // Add more verification as needed
    }






}
