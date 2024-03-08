package com.example.thesix;
/**
 * MyQRCode is a class representing a QR code object.
 * It contains data fields for QR code image, event number, and description.
 * @Author Jingru, David Lee
 */
public class MyQRCode {
    private String qrImageData;
    private Long eventNum;
    private String description;


    /**
     * Constructs a new MyQRCode object with the specified QR code image data, event number, and description.
     *
     * @param qrImageData   The image data of the QR code.
     * @param eventNum      The event number associated with the QR code.
     * @param description   The description associated with the QR code.
     */
    public MyQRCode(String qrImageData, long eventNum,String description) {
        this.qrImageData = qrImageData;
        this.eventNum = eventNum;
        this.description = description;
    }
    /**
     * Retrieves the QR code image data.
     *
     * @return The image data of the QR code.
     */
    public String getQrImageData() {
        return qrImageData;
    }
    /**
     * Sets the QR code image data.
     *
     * @param qrImageData The image data of the QR code to be set.
     */
    public void setQrImageData(String qrImageData) {
        this.qrImageData = qrImageData;
    }
    /**
     * Retrieves the event number associated with the QR code.
     *
     * @return The event number associated with the QR code.
     */
    public Long getEventNum() {
        return eventNum;
    }
    /**
     * Sets the event number associated with the QR code.
     *
     * @param eventNum The event number to be set.
     */
    public void setEventNum(Long eventNum) {
        this.eventNum = eventNum;
    }
    /**
     * Retrieves the description associated with the QR code.
     *
     * @return The description associated with the QR code.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Sets the description associated with the QR code.
     *
     * @param description The description to be set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

