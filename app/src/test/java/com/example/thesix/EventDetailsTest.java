package com.example.thesix;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
public class EventDetailsTest {
    @Test
    void testEventDetailsConstructor() {
        String eventImageData = "eventImageData";
        String inviteQrImageData = "inviteQrImageData";
        String promoQrImageData = "promoQrImageData";
        Long eventNum = (long)123456;
        String description = "Sample Event Description";
        String name = "Sample Event Name";
        List<String> attendeeList = new ArrayList<>();
        List<Long> checkIn = new ArrayList<>();

        EventDetails eventDetails = new EventDetails(eventImageData, inviteQrImageData, promoQrImageData,
                eventNum, description, name, attendeeList, checkIn);

        assertEquals(eventImageData, eventDetails.getEventImageData());
        assertEquals(inviteQrImageData, eventDetails.getQrImageData());
        assertEquals(promoQrImageData, eventDetails.getPromoQrImageData());
        assertEquals(eventNum, eventDetails.getEventNum());
        assertEquals(description, eventDetails.getDescription());
        assertEquals(name, eventDetails.getName());
        assertEquals(attendeeList, eventDetails.getAttendeeList());
        assertEquals(checkIn, eventDetails.getCheckIn());
    }

    @Test
    void testSettersAndGetters() {
        EventDetails eventDetails = new EventDetails("", "", "", 0L, "", "", new ArrayList<>(), new ArrayList<>());

        String newEventImageData = "newEventImageData";
        String newInviteQrImageData = "newInviteQrImageData";
        String newPromoQrImageData = "newPromoQrImageData";
        Long newEventNum = (long)654321;
        String newDescription = "New Sample Event Description";
        String newName = "New Sample Event Name";
        List<String> newAttendeeList = new ArrayList<>();
        List<Long> newCheckIn = new ArrayList<>();

        eventDetails.setEventImageData(newEventImageData);
        eventDetails.setQrImageData(newInviteQrImageData);
        eventDetails.setPromoQrImageData(newPromoQrImageData);
        eventDetails.setEventNum(newEventNum);
        eventDetails.setDescription(newDescription);
        eventDetails.setName(newName);
        eventDetails.setAttendeeList(newAttendeeList);
        eventDetails.setCheckIn(newCheckIn);

        assertEquals(newEventImageData, eventDetails.getEventImageData());
        assertEquals(newInviteQrImageData, eventDetails.getQrImageData());
        assertEquals(newPromoQrImageData, eventDetails.getPromoQrImageData());
        assertEquals(newEventNum, eventDetails.getEventNum());
        assertEquals(newDescription, eventDetails.getDescription());
        assertEquals(newName, eventDetails.getName());
        assertEquals(newAttendeeList, eventDetails.getAttendeeList());
        assertEquals(newCheckIn, eventDetails.getCheckIn());
    }
}

