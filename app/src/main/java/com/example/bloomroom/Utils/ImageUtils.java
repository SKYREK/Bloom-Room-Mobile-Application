package com.example.bloomroom.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.bloomroom.R;

public class ImageUtils {

    // Function to load image from a URL into the ImageView using Glide
    public static void loadImageFromUrl(Context context, String imageUrl, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.image_upload_default) // Optional placeholder image while loading
                .error(R.drawable.image_upload_default) // Optional error image if the loading fails
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(imageUrl)
                .apply(requestOptions)
                .into(imageView);
    }
}
