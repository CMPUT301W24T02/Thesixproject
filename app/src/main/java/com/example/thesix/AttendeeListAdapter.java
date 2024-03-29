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
 *  Formats Attendee Object in the List to be viewed
 */
public class AttendeeListAdapter extends ArrayAdapter<Attendee> {
    private ArrayList<Attendee> attendees;
    private Context context;

    /**
     * AttendeeListAdapter to set attendees and context
     * @param :Context context, ArrayList<Attendee> attendees
     */
    public AttendeeListAdapter(Context context, ArrayList<Attendee> attendees){
        super(context,0, attendees);
        this.attendees = attendees;
        this.context = context;
    }

    /**
     * Getting ViewAdapter
     * @param :int position, @Nullable View convertView, @NonNull ViewGroup parent
     * @return : View
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.attendee_list_content, parent,false);
        }

        Attendee attendee = attendees.get(position);

        TextView attendeeName = view.findViewById(R.id.name_text);
        TextView checkedIn = view.findViewById(R.id.checkedin_text);

        attendeeName.setText(attendee.getName());
        checkedIn.setText(String.valueOf(attendee.getCheckin()));

        return view;
    }
}

