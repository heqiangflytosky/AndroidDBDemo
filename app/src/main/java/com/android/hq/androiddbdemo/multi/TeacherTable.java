package com.android.hq.androiddbdemo.multi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;


public class TeacherTable implements Table {
    private SchoolDatabase mSchoolDatabaseHelper;

    public static final String TABLE_NAME = "teacher";
    public static final int MATCH_TEACHER = 201;

    public static final String URI_PATH_TEACHER = "teacher";
    public static Uri URI_TEACHER = DemoProvider.AUTHORITY_URI.buildUpon().appendPath(URI_PATH_TEACHER).build();

    static {
        DemoProvider.addMatchURI(URI_PATH_TEACHER, MATCH_TEACHER);
    }

    public interface Columns extends BaseColumns {
        String NAME = "name";
        String GENDER = "gender";
        String COUNTRY = "country";
        String SENIORITY = "seniority";
        String UPDATE_TIME = "update_time";
    }

    private static final String CREATE_TAB_TEACHERS =
            "CREATE TABLE " + TABLE_NAME
                    + "("
                    + TeacherTable.Columns._ID + " INTEGER PRIMARY KEY,"
                    + TeacherTable.Columns.NAME + " TEXT,"
                    + TeacherTable.Columns.GENDER + " TEXT,"
                    + TeacherTable.Columns.COUNTRY + " TEXT,"
                    + TeacherTable.Columns.UPDATE_TIME + " INTEGER,"
                    + TeacherTable.Columns.SENIORITY + " INTEGER"
                    + ");";

    public TeacherTable(@NonNull SchoolDatabase database) {
        mSchoolDatabaseHelper = database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TAB_TEACHERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(CREATE_TAB_TEACHERS);
        }
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
        return matchCode == MATCH_TEACHER;
    }
}
