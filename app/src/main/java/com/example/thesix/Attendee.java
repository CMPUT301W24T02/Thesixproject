package com.example.thesix;

/**
 *  Keeps attendee information
 *  Generates user profile picture
 * methods : AttendeeConstructor
 **/

public class Attendee {
    /*
    Keeps attendee information
    Keeps profile picture Data(if uploaded)
    Generates user profile picture
    */
    private String name;
    private Long checkin;
    private String contact;
    private String homePage;
    private String profile_image;

    public Attendee() {
        // Default constructor required for calls to DataSnapshot.getValue(Attendee.class)
    }

    /**
     * Constructor for Attendee with contact information.
     * This constructor is used when you have the attendee's name,
     * contact number, and home page details available. It initializes
     * the object with these three pieces of information.
     *
     * @param name      the name of the attendee
     * @param contact   the contact number of the attendee
     * @param homePage  the home page URL or handle of the attendee
     */
    public Attendee(String name,String contact, String homePage, String profileImage){

        this.name = name;
        this.contact = contact;
        this.homePage = homePage;
        this.profile_image = profileImage;
    }


    /** Saves attendee information
     * @param name  of attendee
     * @param checkin long number
     */
    public Attendee(String name, Long checkin) {

        this.name = name;
        this.checkin = checkin;
    }


    /** getting Name
     * @return String with name
     */
    public String getName() {
        return name;
    }


    /** sets profile Name
     * @param name of person setter
     */
    public void setName(String name) {
        this.name = name;
    }


    /** Checks profile In
     * @return Long checkin
     */
    public Long getCheckin() {
        return checkin;
    }

    /**
     * Set check in
     * @param : Long checkin
     * @return : void
     */
    public void setCheckin(Long checkin) {
        this.checkin = checkin;
    }

    /**
     * Gets the contact number of the attendee.
     * This method retrieves the contact number that was previously set for this attendee.
     *
     * @return A string representing the contact number of the attendee.
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the contact number of the attendee.
     * This method allows updating the contact number for this attendee.
     *
     * @param contact A string representing the new contact number to be set for the attendee.
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Gets the home page of the attendee.
     * This method retrieves the home page URL or handle that was previously set for this attendee.
     *
     * @return A string representing the home page of the attendee.
     */
    public String getHomePage() {
        return homePage;
    }

    /**
     * Sets the home page of the attendee.
     * This method allows updating the home page information for this attendee.
     *
     * @param homePage A string representing the new home page URL or handle to be set for the attendee.
     */
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }


    /** Getting Profile Image String
     * @return String of profile image
     */
    public String getProfile_image() {
        return profile_image;
    }

    /** getting Profile Image
     * @param profile_image with string of profile image
     */
    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
