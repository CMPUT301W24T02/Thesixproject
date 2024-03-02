package com.example.thesix;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class QrCodeArrayAdapter extends ArrayAdapter<MyQRCode> {
    private ArrayList<MyQRCode> qrCodes;
    private Context context;
    public QrCodeArrayAdapter(Context context, ArrayList<MyQRCode> qrCodes) {
        super(context,0,qrCodes);
        this.qrCodes = qrCodes;
        this.context = context;
    }
}
