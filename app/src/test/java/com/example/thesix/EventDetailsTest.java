package com.example.thesix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventDetailsTest {

    private EventDetails eventDetails;

    @BeforeEach
    public void setUp() {
        List<String> attendeeList = new ArrayList<>();
        attendeeList.add("Attendee 1");
        attendeeList.add("Attendee 2");

        List<Long> checkInCountList = new ArrayList<>();
        checkInCountList.add(10L);
        checkInCountList.add(20L);
        List<String> attendeeIDList = new ArrayList<>();
        attendeeList.add("Attendee 1");
        attendeeList.add("Attendee 2");
        List<String> sighUpIDList = new ArrayList<>();
        attendeeList.add("Attendee 1");
        attendeeList.add("Attendee 2");


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
                sighUpIDList
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
}