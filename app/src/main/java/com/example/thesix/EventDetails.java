

package com.example.thesix;

/**
 *EventDetails class encapsulates details of an event including QR code image data, event number, description, and name.
 * Provides a constructor to initialize the event details.
 * Accessor (get) and mutator (set) methods are provided for each attribute.
 * getQrImageData() retrieves the QR code image data.
 * setQrImageData(String qrImageData) sets the QR code image data.
 * getEventNum() retrieves the event number.
 * setEventNum(Long eventNum) sets the event number.
 * getDescription() retrieves the event description.
 * setDescription(String description) sets the event description.
 * getName() retrieves the event name.
 * setName(String name) sets the event name
 *
 */


public class EventDetails {

    private String eventImageData;
    private String inviteQrImageData;
    private String promoQrImageData;
    private Long eventNum;
    private String eventdescription;
    private String eventname;

    public EventDetails(String eventImageData, String inviteQrImageData,String promoQrImageData, long eventNum, String description, String name) {
        this.eventImageData = eventImageData;
        this.inviteQrImageData = inviteQrImageData;
        this.promoQrImageData = promoQrImageData;
        this.eventNum = eventNum;
        this.eventdescription = description;
        this.eventname = name;
    }
    public String getEventImageData() {
        return eventImageData;
    }

    public void setEventImageData(String eventImageData) {
        this.eventImageData = eventImageData;
    }

    public String getQrImageData() {
        return inviteQrImageData;
    }

    public void setQrImageData(String qrImageData) {
        this.inviteQrImageData = qrImageData;
    }

    public Long getEventNum() {
        return eventNum;
    }

    public void setEventNum(Long eventNum) {
        this.eventNum = eventNum;
    }

    public String getDescription() {
        return eventdescription;
    }

    public void setDescription(String description) {
        this.eventdescription = description;
    }

    public String getName() {
        return eventname;
    }

    public void setName(String name) {
        this.eventname = name;
    }

    public String getPromoQrImageData() {
        return promoQrImageData;
    }

    public void setPromoQrImageData(String promoQrImageData) {
        this.promoQrImageData = promoQrImageData;
    }
}