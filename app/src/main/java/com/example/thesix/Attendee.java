package com.example.thesix;

/**
 *  Keeps attendee information
 *  Keeps profile picture Data(if uploaded)
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

    public Attendee(String name, Long checkin){
        // Temporary Constructor
        this.name = name;
        this.checkin = checkin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCheckin() {
        return checkin;
    }

    public void setCheckin(Long checkin) {
        this.checkin = checkin;
    }
}
