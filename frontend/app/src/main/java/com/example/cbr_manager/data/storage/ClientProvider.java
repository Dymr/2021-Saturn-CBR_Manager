package com.example.cbr_manager.data.storage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cbr_manager.data.model.ClientValues;
import com.example.cbr_manager.service.client.Client;

public class ClientProvider extends ContentProvider {

    // Basic info for exposing the database to other application
    public static final String TAG = ClientProvider.class.getName();
    private ClientDao clientDao;

    // Authority of this content provider, permission for changing database
    public static final String AUTHORITY = "com.example.cbr_manager.data.storage";
    public static final String CLIENT_TABLE_NAME = "client";

    // Match code for getting a list of object or a specific item from table
    public static final int CLIENT_LIST = 1;
    public static final int CLIENT_ID = 2;

    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sUriMatcher.addURI(AUTHORITY,
                            CLIENT_TABLE_NAME,
                            CLIENT_LIST);
        sUriMatcher.addURI(AUTHORITY,
                            CLIENT_TABLE_NAME+"/*",
                            CLIENT_ID);
    }

    @Override
    public boolean onCreate() {
        clientDao = RoomDB.getDatabase(getContext()).clientDao();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query");
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case CLIENT_LIST:
                cursor = clientDao.getClients();
                if(getContext() != null){
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursor;
                }

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "insert");
        switch (sUriMatcher.match(uri)){
            case CLIENT_LIST:
                if(getContext()!= null){
                    long id = clientDao.insert(ClientValues.fromContentValues(values));
                    if(id != 0){
                        getContext().getContentResolver().notifyChange(uri, null);
                        return ContentUris.withAppendedId(uri, id);
                    }
                }
            case CLIENT_ID:
                throw new IllegalArgumentException("Invalid URI: Insert failed" + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "delete");
        switch (sUriMatcher.match(uri)){
            case CLIENT_LIST:
                throw new IllegalArgumentException("Invalid URI: cannot delete");
            case CLIENT_ID:
                if(getContext()!=null){
                    int count = clientDao.delete(ContentUris.parseId(uri));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return count;
                }
            default:
                throw new IllegalArgumentException("Unknown URI:" + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update");
        switch (sUriMatcher.match(uri)){
            case CLIENT_LIST:
                if(getContext()!=null){
                    int count = clientDao.update(ClientValues.fromContentValues(values));
                    if(count!=0){
                        getContext().getContentResolver().notifyChange(uri, null);
                        return count;
                    }
                }
            case CLIENT_ID:
                throw new IllegalArgumentException("Invalid URI:  cannot update");
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
