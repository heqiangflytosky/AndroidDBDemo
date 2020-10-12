package com.android.hq.androiddbdemo.multi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ConfigTable implements Table {

    public static final String URI_PATH_COMMON_BASE = "common";
    // 注意URI后面的*表示通配符，后面可以带其他参数
    public static final String URI_PATH_COMMON_GET_INFO = URI_PATH_COMMON_BASE + "/get_info/*";

    public static final int MATCH_COMMON_GET_INFO = 401;

    static {
        DemoProvider.addMatchURI(URI_PATH_COMMON_GET_INFO, MATCH_COMMON_GET_INFO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     *测试 MatrixCursor 的使用
     * @return
     */
    @Override
    public Cursor query(int matchCode, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (matchCode) {
            case MATCH_COMMON_GET_INFO:
                String name = uri.getLastPathSegment();
                MatrixCursor cursor = new MatrixCursor(new String[]{"name","id","age"});
                cursor.addRow(new Object[]{name,1,20});
                return cursor;
        }
        return null;
    }

    @Override
    public long insert(int matchCode, Uri uri, ContentValues values) {
        return 0;
    }

    @Override
    public int update(int matchCode, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(int matchCode, Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(int matchCode, Uri uri) {
        return null;
    }

    @Override
    public boolean respond(int matchCode) {
        return matchCode == MATCH_COMMON_GET_INFO;
    }
}
