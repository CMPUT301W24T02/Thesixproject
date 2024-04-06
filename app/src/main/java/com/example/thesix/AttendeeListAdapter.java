package com.example.thesix;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 *  Formats Attendee Object in the List to be viewed , as well as a view
 */
public class AttendeeListAdapter extends ArrayAdapter<Attendee> {
    private ArrayList<Attendee> attendees;
    private Context context;


    /**AttendeeListAdapter to set attendees and context
     * @param context for attendee list adapter
     * @param attendees list of attendees
     */
    public AttendeeListAdapter(Context context, ArrayList<Attendee> attendees){
        super(context,0, attendees);
        this.attendees = attendees;
        this.context = context;
    }

    /**
     *Getting adapter view
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return View
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;
        //if view !=null
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.attendee_list_content, parent,false);
        }
        //attendee getting position
        Attendee attendee = attendees.get(position);

        TextView attendeeName = view.findViewById(R.id.name_text);
        TextView checkedIn = view.findViewById(R.id.checkedin_text);
        //setting attendees
        attendeeName.setText(attendee.getName());
        checkedIn.setText(String.valueOf(attendee.getCheckin()));

        return view;
    }
}

