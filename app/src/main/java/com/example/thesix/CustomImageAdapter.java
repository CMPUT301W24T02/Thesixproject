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
    private ArrayList<Bitmap> image;


    /** CustomImageAdapter to set context and data
     * @param context Activity context
     * @param data activity data
     */
    public CustomImageAdapter(Context context, ArrayList<Bitmap> data) {
        super(context, R.layout.image_list_content, data);
        this.context = context;
        this.image = data;
    }

    /** Getting ViewAdapter
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.image_list_content, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_list);
        imageView.setImageBitmap(image.get(position));

        return convertView;
    }
}