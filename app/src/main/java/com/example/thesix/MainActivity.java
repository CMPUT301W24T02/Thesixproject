package com.example.thesix;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.util.Base64;
import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private Button generateButton;
    private ImageView qrCodeImageView;
    private Firebase firestoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateButton = findViewById(R.id.generateButton);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        firestoreHelper = new Firebase();

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String inviteQrData = "Unique Data Here"; // Replace with unique data
                    String promoQrData = ""; // Replace with unique data
                    QRCodeWriter inviteWriter = new QRCodeWriter();
                    QRCodeWriter promoWriter = new QRCodeWriter();
                    BitMatrix inviteBitMatrix = inviteWriter.encode(inviteQrData, BarcodeFormat.QR_CODE, 512, 512);
                    Bitmap inviteBitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565);
                    BitMatrix promoBitMatrix = promoWriter.encode(promoQrData, BarcodeFormat.QR_CODE, 512, 512);
                    Bitmap promoBitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565);

                    for (int x = 0; x < 512; x++) {
                        for (int y = 0; y < 512; y++) {
                            inviteBitmap.setPixel(x, y, inviteBitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                        }
                    }
                    for (int x = 0; x < 512; x++) {
                        for (int y = 0; y < 512; y++) {
                            promoBitmap.setPixel(x, y, promoBitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                        }
                    }

                    // Display QR Code
                    qrCodeImageView.setImageBitmap(inviteBitmap);
                    qrCodeImageView.setImageBitmap(promoBitmap);

                    // Convert Bitmap to Base64 String
                    String inviteQrImageBase64 = bitmapToBase64(inviteBitmap);
                    String promoQrImageBase64 = bitmapToBase64(promoBitmap);

                    // Save invite QR Code in Firestore
                    MyQRCode inviteQrCode = new MyQRCode(inviteQrImageBase64);
                    firestoreHelper.saveInviteQRCode(inviteQrCode);
                    MyQRCode promoQrCode = new MyQRCode(promoQrImageBase64);
                    firestoreHelper.savePromoQRCode(promoQrCode);

                } catch (WriterException e) {
                    Log.e("MainActivity", "Error generating QR code", e);
                }
            }
        });
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
