package com.example.thesix;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * CustomImageAdapter class formats bitmap images in the List to be viewed
 */

public class CustomImageAdapter extends ArrayAdapter<Bitmap> {

    private Context context;
    private ArrayList<Bitmap> data;

    /**
     * CustomImageAdapter to set context and data
     * @param :Context context, ArrayList<Bitmap> data
     */
    public CustomImageAdapter(Context context, ArrayList<Bitmap> data) {
        super(context, R.layout.image_list_content, data);
        this.context = context;
        this.data = data;
    }

    /**
     * Getting ViewAdapter
     * @param
     * @return : View
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.image_list_content, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_list);
        imageView.setImageBitmap(data.get(position));

        return convertView;
    }
}