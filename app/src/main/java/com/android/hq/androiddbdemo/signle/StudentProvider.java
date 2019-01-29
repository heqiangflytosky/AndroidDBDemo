package com.android.hq.androiddbdemo.signle;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by heqiang on 17-9-1.
 */

public class StudentProvider extends SQLiteContentProvider {
    public static final String AUTHORITY = "com.android.hq.test";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PARAM_GROUP_BY = "groupBy";
    public static final String PARAM_LIMIT = "limit";

    private SQLiteOpenHelper mOpenHelper;

    public interface StudentUri{
        String STUDENTS = "students";
        Uri URI_STUDENTS = Uri.withAppendedPath(AUTHORITY_URI, STUDENTS);
    }

    interface StudentsMatch {
        int STUDENTS = 0x0001;
    }

    static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        final UriMatcher matcher = URI_MATCHER;
        matcher.addURI(AUTHORITY, StudentUri.STUDENTS, StudentsMatch.STUDENTS);
    }

    @Override
    public SQLiteOpenHelper getDatabaseHelper(Context context) {
        synchronized (this) {
            if (mOpenHelper == null) {
                mOpenHelper = DBOpenHelper.newInstance(context);
            }
            return mOpenHelper;
        }
    }

    @Override
    public Uri insertInTransaction(Uri uri, ContentValues values, boolean callerIsSyncAdapter) {
        int match = URI_MATCHER.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long id = -1;
        switch (match) {
            case StudentsMatch.STUDENTS:
                id = db.insert(DBOpenHelper.StudentTAB.TABLE, null, values);
                break;

            default:
                throw new UnsupportedOperationException("Unknown insert URI " + uri);
        }
        if (id >= 0) {
            postNotifyUri(uri);
            return ContentUris.withAppendedId(uri, id);
        } else {
            return super.insert(uri, values);
        }
    }

    @Override
    public int updateInTransaction(Uri uri, ContentValues values, String selection, String[] selectionArgs, boolean callerIsSyncAdapter) {
        int match = URI_MATCHER.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int modified = 0;
        switch (match) {
            case StudentsMatch.STUDENTS:
                modified = db.update(DBOpenHelper.StudentTAB.TABLE, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown update URI " + uri);
        }
        if (modified > 0) {
            postNotifyUri(uri);
        }
        return modified;
    }

    @Override
    public int deleteInTransaction(Uri uri, String selection, String[] selectionArgs, boolean callerIsSyncAdapter) {
        final int match = URI_MATCHER.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int deleted = 0;
        switch (match) {
            case StudentsMatch.STUDENTS:
                deleted = db.delete(DBOpenHelper.StudentTAB.TABLE, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown delete URI " + uri);
        }
        if (deleted > 0) {
            postNotifyUri(uri);
        }
        return deleted;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = URI_MATCHER.match(uri);

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String limit = uri.getQueryParameter(PARAM_LIMIT);
        String groupBy = uri.getQueryParameter(PARAM_GROUP_BY);
        switch (match) {
            case StudentsMatch.STUDENTS:
                qb.setTables(DBOpenHelper.StudentTAB.TABLE);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URL "
                        + uri.toString());
        }

        Cursor cursor = qb.query(db, projection, selection, selectionArgs,
                groupBy, null, sortOrder, limit);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
