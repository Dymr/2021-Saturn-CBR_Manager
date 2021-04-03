package com.example.cbr_manager.utils;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.cbr_manager.BuildConfig;
import com.squareup.picasso.Picasso;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;


public class Helper {
    public static String getAPIUrl() {
        return BuildConfig.API_URL;
    }

    public static String formatTokenHeader(String token) {
        return "Token " + token;
    }

    public static Drawable getImageFromUrl(String url) throws IOException {
        URL urlObj = new URL(url);
        InputStream stream = (InputStream) urlObj.getContent();
        return Drawable.createFromStream(stream, url);
    }

    public static void setImageViewFromURL(String url, ImageView view, int defaultDrawableResId) {
        Picasso picasso = Picasso.get();
        picasso.load(url).into(view);
        if (view.getDrawable() == null) {
            view.setImageResource(defaultDrawableResId);
        }
    }

    public static String riskToColourCode(int riskScore) {
        if (riskScore >= 10) {
            return "#b02323";
        } else if (riskScore < 10 && riskScore >= 5) {
            return "#c45404";
        } else {
            return "#c49704";
        }
    }

    public static Boolean validateTextViewNotNull(TextView editTextView) {
        if (editTextView.getText().toString().trim().isEmpty()) {
            return false;
        }
        return true;

    }

    public static String convertToString(Timestamp date) {
        Format formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

    public static ZonedDateTime parseUTCDateTime(String date) {
        DateTimeFormatter f = DateTimeFormatter.ISO_DATE_TIME;
        return ZonedDateTime.parse(date, f);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTime convertToLocalDateTime(ZonedDateTime zonedDateTime, ZoneId zoneId) {
        ZonedDateTime zdt = zonedDateTime.withZoneSameInstant(zoneId);
        return zdt.toLocalDateTime();
    }

    public static Instant getCurrentUTCTime(){
        return Instant.now();
    }

    public static String formatDateTimeToLocalString(String dateString, FormatStyle formatStyle){
        Instant instant = Instant.parse(dateString);
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(formatStyle);
        return instant.atZone(zoneId).format(dateTimeFormatter);
    }
}
