package com.android.hq.androiddbdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heqiang on 2019/1/24.
 */

public class DemoProvider extends ContentProvider {
    public static final String AUTHORITY = "com.android.hq";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    private static List<Table> sTables = new ArrayList<>();
    private static List<AbstractDatabase> sDatabases = new ArrayList<>();

    static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URI_MATCHER.addURI(AUTHORITY, StudentTable.TABLE_NAME, StudentTable.MATCH_STUDENT);
    }

    @Override
    public boolean onCreate() {
        // 添加该provider中所有的表
        sDatabases.add(new SchoolDatabase(getContext()));
        for (AbstractDatabase database : sDatabases) {
            addTable(database.getTables());
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = URI_MATCHER.match(uri);
        for(Table table : sTables) {
            if (table.respond(match)) {
                return table.query(match, uri, projection, selection, selectionArgs, sortOrder);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = URI_MATCHER.match(uri);
        for(Table table : sTables) {
            if (table.respond(match)) {
                long id = table.insert(match, uri, contentValues);
                postNotify(uri);
                return ContentUris.withAppendedId(uri, id);
            }
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = URI_MATCHER.match(uri);
        for(Table table : sTables) {
            if (table.respond(match)) {
                int count = 0;
                count = table.delete(match, uri, selection, selectionArgs);
                if (count > 0) {
                    postNotify(uri);
                }
                return count;
            }
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = URI_MATCHER.match(uri);
        for(Table table : sTables) {
            if (table.respond(match)) {
                int count = 0;
                count =  table.update(match, uri, contentValues, selection, selectionArgs);
                if (count > 0) {
                    postNotify(uri);
                }
                return count;
            }
        }
        return 0;
    }

    private static void addTable(List<Table> tables) {
        if (tables != null) {
            sTables.addAll(tables);
        }
    }

    private void postNotify(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }

}
