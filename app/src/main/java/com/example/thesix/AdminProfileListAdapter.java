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

/**
 *  Gets Attendee Profiles in a list
 */
public class AdminProfileListAdapter extends ArrayAdapter<Attendee> {
    ///getting activity context
    private Context context;
    private ArrayList<Attendee> attendees;


    /** profile List Adapter
     * @param context with activity context
     * @param attendees list of attendees
     */
    public AdminProfileListAdapter(Context context, ArrayList<Attendee> attendees) {
        super(context, 0, attendees);
        this.context = context;
        this.attendees = attendees;
    }

    /** getting required View
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return View
     */
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

    /** decoding 64 to string
     * @param base64String to decode from string
     * @return getting Bitmap version of data
     */
    private Bitmap decodeBase64(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
