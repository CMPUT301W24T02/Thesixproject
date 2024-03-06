package com.example.thesix;


/** Create attributes for QrImage , includes getters and setters
 * methods : MyQRCode,getQrImageData,setQrImageData
 **/

public class MyQRCode {
    private String qrImageData;
    private Long eventNum;
    private String description;



    public MyQRCode(String qrImageData, long eventNum,String description) {
        this.qrImageData = qrImageData;
        this.eventNum = eventNum;
        this.description = description;
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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

