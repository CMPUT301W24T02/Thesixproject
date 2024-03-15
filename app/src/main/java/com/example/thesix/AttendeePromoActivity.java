package com.example.thesix;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AttendeePromoActivity extends AppCompatActivity {
    private TextView eventName;
    private TextView eventDescription;
    private ImageView eventPoster;
    private Button backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_promo_activity);
        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        eventPoster = findViewById(R.id.eventPoster);
        backButton = findViewById(R.id.backButton);
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String description = bundle.getString("description");
        String imageData = bundle.getString("imageData");
        Log.d("showing",name+description);
        eventName.setText(name);
        eventDescription.setText(description);
        Bitmap bitmap = StringToBitMap(imageData);
        eventPoster.setImageBitmap(bitmap);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeePromoActivity.this, AttendeeMainActivity.class));
            }
        });

    }
    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }


}
