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
    private String qrImageData;
    private Long eventNum;
    private String eventdescription;
    private String eventname;

    public EventDetails(String qrImageData, long eventNum, String description, String name) {
        this.qrImageData = qrImageData;
        this.eventNum = eventNum;
        this.eventdescription = description;
        this.eventname = name;
    }

    public String getQrImageData() {
        return qrImageData;
    }

    public void setQrImageData(String qrImageData) {
        this.qrImageData = qrImageData;
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
}
