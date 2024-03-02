package com.example.thesix;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;

public class OrganizerUseNewQRActivity extends AppCompatActivity {
    private EditText descriptionEditText;
    private Button createEventButton;
    private ImageView qrCodeImageView;
    private QrCodeDB firestoreHelper;
    private Button backButton;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_use_new_qr_screen);
        descriptionEditText = findViewById(R.id.editTextText);
        createEventButton = findViewById(R.id.generateButton);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        firestoreHelper = new QrCodeDB();
        backButton = findViewById(R.id.backButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID); //get device ID
                    String description = descriptionEditText.getText().toString();
                    String qrString = deviceID + description;
                    QRCodeWriter writer = new QRCodeWriter();
                    BitMatrix bitMatrix = writer.encode(qrString, BarcodeFormat.QR_CODE, 512, 512);
                    Bitmap bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565);

                    for (int x = 0; x < 512; x++) {
                        for (int y = 0; y < 512; y++) {
                            bitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                        }
                    }


                    // Display QR Code
                    qrCodeImageView.setImageBitmap(bitmap);


                    // Convert Bitmap to Base64 String
                    String qrImageBase64 = bitmapToBase64(bitmap);


                    // Save invite QR Code in Firestore
                    MyQRCode qrCode = new MyQRCode(qrImageBase64);
                    firestoreHelper.saveInviteQRCode(deviceID, qrCode);


                } catch (WriterException e) {
                    Log.e("MainActivity", "Error generating QR code", e);
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerUseNewQRActivity.this, OrganizerCreateActivity.class));
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
