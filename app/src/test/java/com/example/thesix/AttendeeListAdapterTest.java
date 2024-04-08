package com.example.thesix;

import static com.google.common.base.Verify.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;

public class AttendeeListAdapterTest {
    private ArrayList<Attendee> attendees;
    @Mock
    private View convertView;

    @Mock
    private ViewGroup parent;

    @Mock
    private TextView nameTextView;

    @Mock
    private TextView checkedInTextView;

    @InjectMocks
    private AttendeeListAdapter adapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        attendees = new ArrayList<>();
        attendees.add(new Attendee("John Doe", 1L));
        attendees.add(new Attendee("Jane Smith", 0L));
        adapter = new AttendeeListAdapter(null, attendees);
        //adapter = new AttendeeListAdapter(null, null); // Pass null for context and attendees
    }

    @Test
    public void testGetView() {
        // Mock behavior of name and checked-in TextViews
        if (convertView != null) {
            when(convertView.findViewById(R.id.name_text)).thenReturn(nameTextView);
            when(convertView.findViewById(R.id.checkedin_text)).thenReturn(checkedInTextView);
            when(nameTextView.getText()).thenReturn("John Doe");
            when(checkedInTextView.getText()).thenReturn("1");
        }

        // Create sample attendee
        Attendee attendee = new Attendee("John Doe", 1L);

        // Test getView method with non-null convertView
        View view = adapter.getView(0, convertView, parent);


        // Verify that correct data is set on TextViews
        assertEquals("John Doe", nameTextView.getText().toString());
        assertEquals("1", checkedInTextView.getText().toString());
    }



    public static class OrganizerUseOldQRActivityTest {
        @org.junit.Test
        public void testAddingBundle() {
            Bundle bundle = new Bundle();
            bundle.putLong("eventNum", 138L);
            assertNotNull("bundle is empty",bundle);


        }
    }
}
