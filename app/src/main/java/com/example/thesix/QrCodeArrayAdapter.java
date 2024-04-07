package com.example.thesix;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
/**
 DisplayingQrCode in Arraylist for admin implementation
 **/

public class QrCodeArrayAdapter extends ArrayAdapter<MyQRCode> {
    private ArrayList<MyQRCode> qrCodes;
    private Context context;

    /**  DisplayingQrCode in Arraylist for admin implementation
     * @param context context of Activity
     * @param qrCodes list of QR Codes
     */
    public QrCodeArrayAdapter(Context context, ArrayList<MyQRCode> qrCodes) {
        super(context,0,qrCodes);
        this.qrCodes = qrCodes;
        this.context = context;
    }
}
