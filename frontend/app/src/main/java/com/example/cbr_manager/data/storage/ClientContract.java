package com.example.cbr_manager.data.storage;

import android.net.Uri;

public final class ClientContract {

    // Name for the entire content provider, it should use the package name of the app
    public static final String CONTENT_AUTHORITY = "com.example.cbr_manager.data.storage";

    // Base of all URIs for accessing data with content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // TODO: List of all possible path to append to base URI for each of the tables



}
