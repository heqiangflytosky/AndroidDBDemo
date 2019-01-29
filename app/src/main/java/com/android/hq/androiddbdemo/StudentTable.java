package com.android.hq.androiddbdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

/**
 * Created by heqiang on 2019/1/25.
 */

public class StudentTable implements Table {
    public static Uri URI_STUDENT = DemoProvider.AUTHORITY_URI.buildUpon().appendPath("student").build();

    private SchoolDatabase mSchoolDatabaseHelper;


    public static final String TABLE_NAME = "student";
    public static final int MATCH_STUDENT = 101;

    public interface Columns extends BaseColumns {
        String NAME = "name";
        String GENDER = "gender";
        String GRADE = "grade";
        String CLASS = "class";
        String COUNTRY = "country";
        String PROVINCE = "province";
        String SPECIALTY = "specialty";
        String IS_BOARDER = "is_boarder";
    }

    private static final String CREATE_TAB_STUDENTS =
            "CREATE TABLE " + TABLE_NAME
                    + "("
                    + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Columns.NAME + " TEXT,"
                    + Columns.GENDER + " TEXT,"
                    + Columns.GRADE +  " INTEGER,"
                    + Columns.CLASS + " INTEGER,"
                    + Columns.COUNTRY + " TEXT,"
                    + Columns.PROVINCE + " TEXT,"
                    + Columns.SPECIALTY + " TEXT,"
                    + Columns.IS_BOARDER + " INTEGER"
                    + ");";

    public StudentTable(@NonNull SchoolDatabase database) {
        mSchoolDatabaseHelper = database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TAB_STUDENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public Cursor query(int matchCode, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mSchoolDatabaseHelper.getReadableDatabase().query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public long insert(int matchCode, Uri uri, ContentValues values) {
        return mSchoolDatabaseHelper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public int update(int matchCode, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mSchoolDatabaseHelper.getWritableDatabase().updateWithOnConflict(TABLE_NAME, values, selection, selectionArgs, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public int delete(int matchCode, Uri uri, String selection, String[] selectionArgs) {
        return mSchoolDatabaseHelper.getWritableDatabase().delete(TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public String getType(int matchCode, Uri uri) {
        return null;
    }

    @Override
    public boolean respond(int matchCode) {
        return matchCode == MATCH_STUDENT;
    }
}
