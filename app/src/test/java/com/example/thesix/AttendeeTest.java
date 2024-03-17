package com.example.thesix;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class AttendeeTest {
    @Test
    void testAttendeeConstructor() {
        String name = "John Doe";
        Long checkin = System.currentTimeMillis();
        Attendee attendee = new Attendee(name, checkin);

        assertEquals(name, attendee.getName());
        assertEquals(checkin, attendee.getCheckin());
    }

    @Test
    void testGetName() {
        String name = "Alice";
        Attendee attendee = new Attendee(name, System.currentTimeMillis());

        assertEquals(name, attendee.getName());
    }

    @Test
    void testSetName() {
        String initialName = "Bob";
        String newName = "Charlie";
        Attendee attendee = new Attendee(initialName, System.currentTimeMillis());

        attendee.setName(newName);

        assertEquals(newName, attendee.getName());
    }

    @Test
    void testGetCheckin() {
        Long checkin = System.currentTimeMillis();
        Attendee attendee = new Attendee("Alice", checkin);

        assertEquals(checkin, attendee.getCheckin());
    }

    @Test
    void testSetCheckin() {
        Long initialCheckin = System.currentTimeMillis() - 1000;
        Long newCheckin = System.currentTimeMillis();
        Attendee attendee = new Attendee("Alice", initialCheckin);

        attendee.setCheckin(newCheckin);

        assertEquals(newCheckin, attendee.getCheckin());
    }
}
