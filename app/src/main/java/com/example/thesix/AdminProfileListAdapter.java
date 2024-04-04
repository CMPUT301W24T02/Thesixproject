package com.example.thesix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdminProfileListAdapter extends ArrayAdapter<Attendee> {

    private Context context;
    private ArrayList<Attendee> attendees;


    public AdminProfileListAdapter(Context context, ArrayList<Attendee> attendees) {
        super(context, 0, attendees);
        this.context = context;
        this.attendees = attendees;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.profile_list_content, parent, false);
        }
        // Get Profile Name
        Attendee attendee = attendees.get(position);
        TextView attendeeName = view.findViewById(R.id.profile_text);
        attendeeName.setText(attendee.getName());

        // Get Profile Image
        ImageView image = view.findViewById(R.id.profile_image);
        String imageString = attendee.getProfile_image();

        // Decode the image string to Bitmap
        Bitmap bitmap = decodeBase64(imageString);

        image.setImageBitmap(bitmap);

        return view;
    }

    private Bitmap decodeBase64(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
