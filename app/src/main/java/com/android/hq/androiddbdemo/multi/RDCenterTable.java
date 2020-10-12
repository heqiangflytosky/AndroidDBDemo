package com.android.hq.androiddbdemo.multi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by heqiang on 2019/1/29.
 */

public class RDCenterTable implements Table {

    private CompanyDatabase mCompanyDatabaseHelper;

    public static final String TABLE_NAME = "rd_center";
    public static final int MATCH_RD_CENTER = 301;

    public static final String URI_PATH_RD_CENTER= "rd_center";
    public static Uri URI_TEACHER = DemoProvider.AUTHORITY_URI.buildUpon().appendPath(URI_PATH_RD_CENTER).build();

    static {
        DemoProvider.addMatchURI(URI_PATH_RD_CENTER, MATCH_RD_CENTER);
    }

    public interface Columns extends BaseColumns {
        String NAME = "name";
        String GENDER = "gender";
        String SENIORITY = "seniority";
        String UPDATE_TIME = "update_time";
    }

    private static final String CREATE_TAB_RD_CENTER =
            "CREATE TABLE " + TABLE_NAME
                    + "("
                    + RDCenterTable.Columns._ID + " INTEGER PRIMARY KEY,"
                    + RDCenterTable.Columns.NAME + " TEXT,"
                    + RDCenterTable.Columns.GENDER + " TEXT,"
                    + RDCenterTable.Columns.UPDATE_TIME + " INTEGER,"
                    + RDCenterTable.Columns.SENIORITY + " INTEGER"
                    + ");";

    public RDCenterTable(CompanyDatabase database) {
        mCompanyDatabaseHelper = database;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TAB_RD_CENTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public Cursor query(int matchCode, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mCompanyDatabaseHelper.getReadableDatabase().query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public long insert(int matchCode, Uri uri, ContentValues values) {
        return mCompanyDatabaseHelper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public int update(int matchCode, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mCompanyDatabaseHelper.getWritableDatabase().updateWithOnConflict(TABLE_NAME, values, selection, selectionArgs, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public int delete(int matchCode, Uri uri, String selection, String[] selectionArgs) {
        return mCompanyDatabaseHelper.getWritableDatabase().delete(TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public String getType(int matchCode, Uri uri) {
        return null;
    }

    @Override
    public boolean respond(int matchCode) {
        return matchCode == MATCH_RD_CENTER;
    }
}
