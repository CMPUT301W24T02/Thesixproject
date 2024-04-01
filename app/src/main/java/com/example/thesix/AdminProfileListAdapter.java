package com.example.thesix;

import android.content.Context;
import android.graphics.Bitmap;
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

    public AdminProfileListAdapter(Context context, ArrayList<Attendee> data) {
        super(context, R.layout.image_list_content, data);
        this.context = context;
        this.attendees = attendees;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            Log.d("DD", "inflate the image list content");
            convertView = LayoutInflater.from(context).inflate(R.layout.profile_list_content, parent, false);
        }
        Log.d("DD", "attendees.get(position) is next");
        Attendee attendee = attendees.get(position);
        TextView attendeeName = view.findViewById(R.id.profile_text);

        attendeeName.setText(attendee.getName());

        //ImageView imageView = convertView.findViewById(R.id.image_list);
        //imageView.setImageBitmap(image.get(position));

        return convertView;
    }
}
