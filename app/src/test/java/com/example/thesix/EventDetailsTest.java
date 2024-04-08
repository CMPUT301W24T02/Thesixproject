package com.example.thesix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import android.location.Location;
import android.util.Log;

public class EventDetailsTest {

    private EventDetails eventDetails;

    @BeforeEach
    public void setUp() {


        List<Long> checkInCountList = new ArrayList<>();
        checkInCountList.add(10L);
        checkInCountList.add(20L);
        List<String> attendeeIDList = new ArrayList<>();
        attendeeIDList.add("27150c669e8b1dc4");
        attendeeIDList.add("2f7daf8e12a8cb75");
        List<String> sighUpIDList = new ArrayList<>();
        List<String> notificationList = new ArrayList<>();
        sighUpIDList.add("2f7daf8e12a8cb75");
        notificationList.add("27150c669e8b1dc4");
        List<Location> locationList = new ArrayList<>();
        Location location1 = null;

        Location location2 = null;

        locationList.add(location1);
        locationList.add(location2);



        eventDetails = new EventDetails(
                "eventImageData",
                "inviteQrImageData",
                "promoQrImageData",
                123L,
                "description",
                "name",
                checkInCountList,
                30L,
                attendeeIDList,
                sighUpIDList,
                notificationList,
                locationList
        );
    }

    @Test
    public void testGetEventImageData() {

        assertEquals("eventImageData", eventDetails.getEventImageData());
    }

    @Test
    public void testSetEventImageData() {
        eventDetails.setEventImageData("newEventImageData");
        assertEquals("newEventImageData", eventDetails.getEventImageData());
    }

    @Test
    public void testGetQrImageData() {
        assertEquals("inviteQrImageData", eventDetails.getQrImageData());
    }

    @Test
    public void testSetQrImageData() {
        eventDetails.setQrImageData("newQrImageData");
        assertEquals("newQrImageData", eventDetails.getQrImageData());
    }

    @Test
    public void testGetEventNum() {
        assertEquals(123L, (long)eventDetails.getEventNum());
    }

    @Test
    public void testSetEventNum() {
        eventDetails.setEventNum(456L);
        assertEquals(456L, (long)eventDetails.getEventNum());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", eventDetails.getDescription());
    }

    @Test
    public void testSetDescription() {
        eventDetails.setDescription("newDescription");
        assertEquals("newDescription", eventDetails.getDescription());
    }

    @Test
    public void testGetName() {
        assertEquals("name", eventDetails.getName());
    }

    @Test
    public void testSetName() {
        eventDetails.setName("newName");
        assertEquals("newName", eventDetails.getName());
    }

    @Test
    public void testGetPromoQrImageData() {
        assertEquals("promoQrImageData", eventDetails.getPromoQrImageData());
    }

    @Test
    public void testSetPromoQrImageData() {
        eventDetails.setPromoQrImageData("newPromoQrImageData");
        assertEquals("newPromoQrImageData", eventDetails.getPromoQrImageData());
    }



    @Test
    public void testGetTotalCheckIn() {
        assertEquals(30L, (long)eventDetails.getTotalCheckIn());
    }

    @Test
    public void testSetTotalCheckIn() {
        eventDetails.setTotalCheckIn(50L);
        assertEquals(50L, (long)eventDetails.getTotalCheckIn());
    }


    @Test
    public void testGetCheckInCountList() {
        List<Long> expectedCheckInCountList = new ArrayList<>();
        expectedCheckInCountList.add(10L);
        expectedCheckInCountList.add(20L);
        assertEquals(expectedCheckInCountList, eventDetails.getCheckInCountList());
    }

    @Test
    public void testSetCheckInCountList() {
        List<Long> newCheckInCountList = new ArrayList<>();
        newCheckInCountList.add(30L);
        newCheckInCountList.add(40L);
        eventDetails.setCheckInCountList(newCheckInCountList);
        assertEquals(newCheckInCountList, eventDetails.getCheckInCountList());
    }

    @Test
    public void testGetAttendeeIDList() {
        List<String> attendeeIDList = new ArrayList<>();
        attendeeIDList.add("27150c669e8b1dc4");
        attendeeIDList.add("2f7daf8e12a8cb75");
        assertEquals(attendeeIDList,eventDetails.getAttendeeIDList());
    }
    @Test
    public void testSetAttendeeIDList() {
        List<String> attendeeIDList = new ArrayList<>();
        attendeeIDList.add("2f7daf8e12a8cb75");
        attendeeIDList.add("27150c669e8b1dc4");
        attendeeIDList.add("2f7daf8e12a8cb75");
        eventDetails.setAttendeeIDList(attendeeIDList);
        assertEquals(attendeeIDList,eventDetails.getAttendeeIDList());
    }

    @Test
    public void testGetSignUpIDList() {
        List<String> sighUpIDList = new ArrayList<>();
        sighUpIDList.add("2f7daf8e12a8cb75");
        assertEquals(sighUpIDList,eventDetails.getSignUpIDList());
    }
    @Test
    public void testSetSignUpIDList() {
        List<String> sighUpIDList = new ArrayList<>();
        sighUpIDList.add("2f7daf8e12a8cb75");
        sighUpIDList.add("27150c669e8b1dc4");
        eventDetails.setSignUpIDList(sighUpIDList);
        assertEquals(sighUpIDList,eventDetails.getSignUpIDList());
    }

}