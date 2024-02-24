package com.example.thesix;

public class MyQRCode {
    private String qrImageData;

    public MyQRCode() {
        // Default constructor required for calls to DataSnapshot.getValue(MyQRCode.class)
    }

    public MyQRCode(String qrImageData) {
        this.qrImageData = qrImageData;
    }

    public String getQrImageData() {
        return qrImageData;
    }

    public void setQrImageData(String qrImageData) {
        this.qrImageData = qrImageData;
    }
}
