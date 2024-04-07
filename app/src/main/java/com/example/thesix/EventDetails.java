package com.example.thesix;

import android.location.Location;
import android.net.Uri;

import org.checkerframework.common.aliasing.qual.Unique;

import java.util.List;

/**
 * EventDetails class encapsulates details of an event including QR code image data, event number, description, and name.
 * Accessor (get) and mutator (set) methods are provided for each attribute..
 * Holds data related to an event.
 */


public class EventDetails {

    private String eventImageData; // Image data for the event
    private String inviteQrImageData; // Image data for the invite QR code
    private String promoQrImageData;
    private Long eventNum; //Unique identifier for the event
    private String description; // Description of the event
    private String name;
    private List<Long> checkInCountList;
    private List<String> attendeeIDList;
    private Long totalCheckIn;
    private List<String> signUpIDList;
    private List<Location> location;


    /**Constructor for event Details
     * @param eventImageData  event image data
     * @param inviteQrImageData  event invite qr image data
     * @param promoQrImageData event promo qr image data
     * @param eventNum event number qr image data
     * @param eventDescription event description for event
     * @param eventName event name data
     * @param checkIn  event check in data
     * @param totalCheckIn event total check in data
     * @param attendeeIDList list of attendees
     * @param signUpIDList list of signed up attendees
     * @param locations list of locations
     */
    public EventDetails(String eventImageData, String inviteQrImageData, String promoQrImageData, long eventNum, String eventDescription,
                        String eventName, List<Long> checkIn, Long totalCheckIn, List<String> attendeeIDList, List<String> signUpIDList,List <Location> locations) {

        //event imageData
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
        this.location = locations;
    }


    /** getEventImageData() retrieves the event poster.
     * @return string of image
     */
    public String getEventImageData() {
        return eventImageData;
    }


    /** setEventImageData(String eventImageData) sets event poster.
     * @param eventImageData to set with
     */
    public void setEventImageData(String eventImageData) {
        this.eventImageData = eventImageData;
    }

    /** get qr image data()
     * @return string qr image data
     */
    public String getQrImageData() {
        return inviteQrImageData;
    }


    /**setInviteQrImageData(String qrImageData) sets the invite QR code image data..
     * @param qrImageData string qr image data
     */
    public void setQrImageData(String qrImageData) {
        this.inviteQrImageData = qrImageData;
    }


    /** getEventNum() retrieves the event number
     * @return event number
     */
    public Long getEventNum() {
        return eventNum;
    }


    /** setEventNum(Long eventNum) sets the event number.
     * @param eventNum event number
     */
    public void setEventNum(Long eventNum) {
        this.eventNum = eventNum;
    }


    /**getDescription() retrieves the event description
     * @return string of description
     */
    public String getDescription() {
        return description;
    }

    /** setDescription(String description) sets the event description.
     * @param description of string
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /** getEventImageData() retrieves the event poster.
     * @return of event name
     */
    public String getName() {
        return name;
    }


    /** getEventImageData() retrieves the event poster.
     * @param name  of event
     */
    public void setName(String name) {
        this.name = name;
    }

    /** getPromoQrImageData() retrieves the promo QR code image data.
     * @return qr image data string
     */
    public String getPromoQrImageData() {
        return promoQrImageData;
    }


    /** setPromoQrImageData(String qrImageData) sets the promo QR code image data.
     * @param promoQrImageData  promo QR code image data.
     */
    public void setPromoQrImageData(String promoQrImageData) {
        this.promoQrImageData = promoQrImageData;
    }


    /** getCheckIn() retrieves checkIn number.
     * @return long with number of check in
     */
    public Long getTotalCheckIn() {
        return totalCheckIn;
    }

    /** sets get CheckIn() retrieves checkIn number.
     * @param totalCheckIn long number of people check in
     */
    public void setTotalCheckIn(Long totalCheckIn) {
        this.totalCheckIn = totalCheckIn;
    }

    /** get checkInCountList
     * @return  List<Long> with list of get  checkInCountList
     */
    public List<Long> getCheckInCountList() {
        return checkInCountList;
    }

    /** stes check in count
     * @param checkInCountList  List<Long> with list of get  checkInCountList
     */
    public void setCheckInCountList(List<Long> checkInCountList) {
        this.checkInCountList = checkInCountList;
    }

    /** Gets AttendeeList
     * @return List<String> with AttendeeIDList
     */
    public List<String> getAttendeeIDList() {
        return attendeeIDList;
    }

    /** Sets AttendeeList
     * @param attendeeIDList  List<String> with AttendeeIDList
     */
    public void setAttendeeIDList(List<String> attendeeIDList) {
        this.attendeeIDList = attendeeIDList;
    }

    /** Gets Sign up list
     * @return List<String> with signUpList
     */
    public List<String> getSignUpIDList() {
        return signUpIDList;
    }

    /** Sets Sign up list
     * @param signUpIDList List<String> with signUpList
     */
    public void setSignUpIDList(List<String> signUpIDList) {
        this.signUpIDList = signUpIDList;
    }

    /** Gets list of locations
     * @return List<Location> list of locations
     */
    public List<Location> getLocation() {
        return location;
    }

    /** Sets list of locations
     * @param location list of location objects
     */
    public void setLocation(List<Location> location) {
        this.location = location;
    }
}