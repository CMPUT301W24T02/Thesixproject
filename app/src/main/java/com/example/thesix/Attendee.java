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

    /**
     * Saves attendee information
     * @param : Attendee attendee
     * @return : void
     */

    public Attendee(String name, Long checkin){
        // Temporary Constructor
        this.name = name;
        this.checkin = checkin;
    }

    /**
     * gets profile Name
     * @param :
     * @return : String
     */


    public String getName() {
        return name;
    }

    /**
     * sets profile Name
     * @param :
     * @return : String
     */


    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks profile In
     * @param :
     * @return : Long
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
}
