package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AttendeeMainActivity extends AppCompatActivity {
    private Button scanButton;
    TextView testing;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_main_activity);
        scanButton = findViewById(R.id.scanButton);
        testing = findViewById(R.id.textView);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://www.youtube.com/watch?v=bWEt-_z7BOY
                IntentIntegrator intentIntegrator = new IntentIntegrator(AttendeeMainActivity.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan a QR Code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });
    }
    //https://www.youtube.com/watch?v=bWEt-_z7BOY
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult != null) {
            String contents = intentResult.getContents();
            Log.d("scanner",contents);
            if(contents != null) {
                //String inviteQrString = num+ "device id"+deviceID;

                if (contents.startsWith("promo")) {
                    contents = contents.replace("promo","");
                    testing.setText(contents);
                }
                else {
                    String[] stringArray = contents.split("device id", 2);
                    Log.d("scanner", stringArray[0] + stringArray[1]);
                    testing.setText(contents);
                }

            }
        }
    }
}
