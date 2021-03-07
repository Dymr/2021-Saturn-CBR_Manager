package com.example.cbr_manager.data.storage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cbr_manager.data.model.ClientContract;

public class ClientProvider extends ContentProvider {

    private static final int CLIENT_LIST = 1;
    private static final int CLIENT_ID = 2;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RoomDB myDB;

    @Override
    public boolean onCreate() {
        myDB = RoomDB.getDatabase(getContext());
        return true;
    }

    public static UriMatcher buildUriMatcher(){
        String content = ClientContract.CONTENT_AUTHORITY;

        //
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, ClientContract.PATH_CLIENT, CLIENT_LIST);
        matcher.addURI(content, ClientContract.PATH_CLIENT + "/#", CLIENT_ID);
        return matcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
