package com.example.thesix;

public class EventDetails {
    private String qrImageData;
    private String eventImageData;
    private Long eventNum;
    private String eventdescription;
    private String eventname;

    public EventDetails(String eventImageData, String qrImageData, long eventNum, String description, String name) {
        this.eventImageData = eventImageData;
        this.qrImageData = qrImageData;
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
