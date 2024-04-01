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

public class AdminProfileListAdapter extends ArrayAdapter<Bitmap> {

    private Context context;
    private ArrayList<Bitmap> image;

    public AdminProfileListAdapter(Context context, ArrayList<Bitmap> data) {
        super(context, R.layout.image_list_content, data);
        this.context = context;
        this.image = data;
    }

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
