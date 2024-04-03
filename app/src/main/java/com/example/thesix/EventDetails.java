package com.example.thesix;

import android.location.Location;
import android.net.Uri;

import java.util.List;

/**
 * EventDetails class encapsulates details of an event including QR code image data, event number, description, and name.
 * Accessor (get) and mutator (set) methods are provided for each attribute..
 */


public class EventDetails {

    private String eventImageData;
    private String inviteQrImageData;
    private String promoQrImageData;
    private Long eventNum;
    private String description;
    private String name;
    private List<Long> checkInCountList;
    private List<String> attendeeIDList;
    private Long totalCheckIn;
    private List<String> signUpIDList;
    private List<Location> location;

    /**
     Constructor for event Details
     * @param : String eventImageData, String inviteQrImageData, String promoQrImageData, long eventNum, String description,
     *                         String name, List<String> attendeeList, List<Long> checkIn
     * @return : Uri
     **/
    public EventDetails(String eventImageData, String inviteQrImageData, String promoQrImageData, long eventNum, String eventDescription,
                        String eventName, List<Long> checkIn, Long totalCheckIn, List<String> attendeeIDList, List<String> signUpIDList,List <Location> locations) {
        this.eventImageData = eventImageData;
        this.inviteQrImageData = inviteQrImageData;
        this.promoQrImageData = promoQrImageData;
        this.eventNum = eventNum;
        this.description = eventDescription;
        this.name = eventName;

        this.checkInCountList = checkIn;
        this.totalCheckIn = totalCheckIn;
        this.attendeeIDList = attendeeIDList;
        this.signUpIDList = signUpIDList;
        this.location = location;
    }

    /**
     getEventImageData() retrieves the event poster.
     * @param :
     * @return : String
     **/
    public String getEventImageData() {
        return eventImageData;
    }

    /**
     setEventImageData(String eventImageData) sets event poster.
     * @param :String
     * @return :
     **/
    public void setEventImageData(String eventImageData) {
        this.eventImageData = eventImageData;
    }
    /**

     * @param :
     * @return : String
     **/
    public String getQrImageData() {
        return inviteQrImageData;
    }
    /**
     setInviteQrImageData(String qrImageData) sets the invite QR code image data..
     * @param : String
     * @return : void
     **/

    public void setQrImageData(String qrImageData) {
        this.inviteQrImageData = qrImageData;
    }
    /**
     getEventNum() retrieves the event number
     * @param :
     * @return : Long
     **/

    public Long getEventNum() {
        return eventNum;
    }
    /**
     setEventNum(Long eventNum) sets the event number.
     * @param : Long
     * @return :
     **/

    public void setEventNum(Long eventNum) {
        this.eventNum = eventNum;
    }
    /**
     getDescription() retrieves the event description.
     * @param :
     * @return : String
     **/

    public String getDescription() {
        return description;
    }
    /**
     * setDescription(String description) sets the event description.
     * @param : String
     * @return :
     **/

    public void setDescription(String description) {
        this.description = description;
    }
    /**
     getEventImageData() retrieves the event poster.
     * @param :
     * @return : String
     **/

    public String getName() {
        return name;
    }
    /**
     getEventImageData() retrieves the event poster.
     * @param :
     * @return : String
     **/

    public void setName(String name) {
        this.name = name;
    }
    /**
     * getPromoQrImageData() retrieves the promo QR code image data.
     * @param :
     * @return : String
     **/

    public String getPromoQrImageData() {
        return promoQrImageData;
    }
    /**
     setPromoQrImageData(String qrImageData) sets the promo QR code image data.
     * @param :
     * @return : String
     **/

    public void setPromoQrImageData(String promoQrImageData) {
        this.promoQrImageData = promoQrImageData;
    }
    /**
     getCheckIn() retrieves checkIn number.
     * @param :
     * @return : List<Long>
     **/




    public Long getTotalCheckIn() {
        return totalCheckIn;
    }

    public void setTotalCheckIn(Long totalCheckIn) {
        this.totalCheckIn = totalCheckIn;
    }

    public List<Long> getCheckInCountList() {
        return checkInCountList;
    }

    public void setCheckInCountList(List<Long> checkInCountList) {
        this.checkInCountList = checkInCountList;
    }

    public List<String> getAttendeeIDList() {
        return attendeeIDList;
    }

    public void setAttendeeIDList(List<String> attendeeIDList) {
        this.attendeeIDList = attendeeIDList;
    }

    public List<String> getSignUpIDList() {
        return signUpIDList;
    }

    public void setSignUpIDList(List<String> signUpIDList) {
        this.signUpIDList = signUpIDList;
    }

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }
}