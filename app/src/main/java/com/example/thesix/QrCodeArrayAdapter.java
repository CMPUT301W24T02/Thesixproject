package com.example.thesix;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
/**
 * QrCodeArrayAdapter is a custom ArrayAdapter for displaying MyQRCode objects in a ListView.
 * It extends ArrayAdapter and overrides necessary methods to handle the display of QR codes.
 * @Author David Lee
 * @See ArrayAdapter
 */
public class QrCodeArrayAdapter extends ArrayAdapter<MyQRCode> {
    private ArrayList<MyQRCode> qrCodes;
    private Context context;
    /**
     * Constructs a new QrCodeArrayAdapter.
     *
     * @param context       The context of the adapter.
     * @param qrCodes       The ArrayList of MyQRCode objects to be displayed.
     */
    public QrCodeArrayAdapter(Context context, ArrayList<MyQRCode> qrCodes) {
        super(context,0,qrCodes);
        this.qrCodes = qrCodes;
        this.context = context;
    }
}
