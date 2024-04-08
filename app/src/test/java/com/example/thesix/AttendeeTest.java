package com.example.thesix;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AttendeeTest {

    @Test
    void testAttendeeConstructorWithNameAndContact() {
        // Arrange
        String name = "John Doe";
        String contact = "1234567890";
        String homePage = "http://example.com";
        String profileImage = "profile.jpg";

        // Act
        Attendee attendee = new Attendee(name, contact, homePage, profileImage);

        // Assert
        assertEquals(name, attendee.getName());
        assertEquals(contact, attendee.getContact());
        assertEquals(homePage, attendee.getHomePage());
        assertEquals(profileImage, attendee.getProfile_image());
        assertNull(attendee.getCheckin()); // Check-in should be null for this constructor
    }

    @Test
    void testAttendeeConstructorWithNameAndCheckin() {
        // Arrange
        String name = "John Doe";
        Long checkin = System.currentTimeMillis(); // Assuming current time as check-in time

        // Act
        Attendee attendee = new Attendee(name, checkin);

        // Assert
        assertEquals(name, attendee.getName());
        assertEquals(checkin, attendee.getCheckin());
        assertNull(attendee.getContact()); // Contact should be null for this constructor
        assertNull(attendee.getHomePage()); // Home page should be null for this constructor
        assertNull(attendee.getProfile_image()); // Profile image should be null for this constructor
    }

    @Test
    void testGetName() {
        // Arrange
        String name = "Jane Doe";
        Attendee attendee = new Attendee();
        attendee.setName(name);

        // Act
        String retrievedName = attendee.getName();

        // Assert
        assertEquals(name, retrievedName);
    }

    @Test
    void testSetName() {
        // Arrange
        String name = "Jane Doe";
        Attendee attendee = new Attendee();

        // Act
        attendee.setName(name);

        // Assert
        assertEquals(name, attendee.getName());
    }

    @Test
    void testGetCheckin() {
        // Arrange
        Long checkin = System.currentTimeMillis();
        Attendee attendee = new Attendee();
        attendee.setCheckin(checkin);

        // Act
        Long retrievedCheckin = attendee.getCheckin();

        // Assert
        assertEquals(checkin, retrievedCheckin);
    }

    @Test
    void testSetCheckin() {
        // Arrange
        Long checkin = System.currentTimeMillis();
        Attendee attendee = new Attendee();

        // Act
        attendee.setCheckin(checkin);

        // Assert
        assertEquals(checkin, attendee.getCheckin());
    }

    // Similarly, you can write tests for getContact, setContact, getHomePage, setHomePage,
    // getProfile_image, and setProfile_image methods.
    @Test
    void testGetNameWithRandomString() {
        // Arrange
        String randomName = getRandomString();
        Attendee attendee = new Attendee();
        attendee.setName(randomName);

        // Act
        String retrievedName = attendee.getName();

        // Assert
        assertEquals(randomName, retrievedName);
    }

    @Test
    void testSetNameWithRandomString() {
        // Arrange
        String randomName = getRandomString();
        Attendee attendee = new Attendee();

        // Act
        attendee.setName(randomName);

        // Assert
        assertEquals(randomName, attendee.getName());
    }

    @Test
    void testGetContactWithRandomString() {
        // Arrange
        String randomContact = getRandomString();
        Attendee attendee = new Attendee();
        attendee.setContact(randomContact);

        // Act
        String retrievedContact = attendee.getContact();

        // Assert
        assertEquals(randomContact, retrievedContact);
    }

    @Test
    void testSetContactWithRandomString() {
        // Arrange
        String randomContact = getRandomString();
        Attendee attendee = new Attendee();

        // Act
        attendee.setContact(randomContact);

        // Assert
        assertEquals(randomContact, attendee.getContact());
    }

    // Similarly, you can add tests for getHomePage, setHomePage,
    // getProfile_image, setProfile_image methods.

    // Utility method to generate a random string for testing
    private String getRandomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (Math.random() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}