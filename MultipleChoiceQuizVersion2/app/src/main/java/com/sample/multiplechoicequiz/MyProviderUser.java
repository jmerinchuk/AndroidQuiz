/*************************************************************************************
 * Program: MultipleChoiceQuizFinal
 * Class: MyProvider
 * Programmer: Jayce Merinchuk
 * Date: November 30, 2018
 * Description: This class handles the second application's access.
 *************************************************************************************/
package com.sample.multiplechoicequiz;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

/***************************************************************************************
 * Class: MyProvider - Helps to manage to other application access
 ***************************************************************************************/
public class MyProviderUser extends ContentProvider {

    // Database Variables
    private static final String DATABASE = "mydb.db";
    private static final int DATABASE_VERSION = 1;
    static final String PROVIDER_NAME = "com.sample.multiplechoicequiz";
    static final String URL = "content://" + PROVIDER_NAME + "/cte";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final int uriCode = 1;
    static final UriMatcher uriMatcher;
    private static HashMap<String, String> values;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "cte", uriCode);
        uriMatcher.addURI(PROVIDER_NAME, "cte/*", uriCode);
    }

    // Table User Variables
    private static final String TABLE_USER = "UserBank";
    private static final String USER = "user";
    private static final String PASS = "pass";
    private static final String SCORE = "score";

    // Helpers
    MyDataBaseHelper myDataBaseHelper;
    private SQLiteDatabase db;


    /***************************************************************************************
     * Method: onCreate()
     * Description: Creates Context and Databasehelper and sets up writeable database.
     * @return
     ***************************************************************************************/
    @Override
    public boolean onCreate() {
        Context context = getContext();
        myDataBaseHelper = new MyDataBaseHelper(context, DATABASE, null, DATABASE_VERSION);
        db = myDataBaseHelper.getWritableDatabase();
        if (db != null) {
            return true;
        }
        return false;
    }

    /***************************************************************************************
     * Method: Uri Insert()
     * Description: Inserts Table datafor ContentResolver.
     * @param uri
     * @param values
     * @return
     ***************************************************************************************/
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(TABLE_USER, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    /***************************************************************************************
     * Method: Update()
     * Description: Updates ContentResolver.
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return count
     ***************************************************************************************/
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode:
                count = db.update(TABLE_USER, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /***************************************************************************************
     * Method: Delete()
     * Description: Delete for the Content Resolver.
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return count
     ***************************************************************************************/
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode:
                count = db.delete(TABLE_USER, selection, selectionArgs); break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /***************************************************************************************
     * Method: getType()
     * Description: get's the package name.
     * @param uri
     * @return string
     ***************************************************************************************/
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case uriCode:
                return "com.sample.multiplechoicequiz/cte";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    /***************************************************************************************
     * Method: query()
     * Description: Queries with cursor data.
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return c
     ***************************************************************************************/
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_USER);
        switch (uriMatcher.match(uri)) {
            case uriCode:
                qb.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            sortOrder = USER;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }
}
