package com.example.cbr_manager.helper;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.cbr_manager.BuildConfig;
import com.example.cbr_manager.service.auth.AuthResponse;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class Helper {
    public static String getAPIUrl() {
        return BuildConfig.API_URL;
    }

    public static String formatTokenHeader(AuthResponse authToken) {
        return "Token " + authToken.token;
    }

    public static Drawable getImageFromUrl(String url) throws IOException {
        URL urlObj = new URL(url);
        InputStream stream = (InputStream) urlObj.getContent();
        return Drawable.createFromStream(stream, url);
    }

    public static void setImageViewFromURL(String url, ImageView view){
        Picasso picasso = Picasso.get();
        picasso.load(url).into(view);
    }
}
