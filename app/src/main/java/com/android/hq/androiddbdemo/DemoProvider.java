package com.android.hq.androiddbdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class DemoProvider extends ContentProvider {
    public static final String AUTHORITY = "com.android.hq";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    private static List<Table> sTables = new ArrayList<>();
    private static List<AbstractDatabase> sDatabases = new ArrayList<>();

    static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    @Override
    public boolean onCreate() {
        // 添加该provider中所有的数据库，以及所有数据库中的表
        sDatabases.add(new SchoolDatabase(getContext()));
        sDatabases.add(new CompanyDatabase(getContext()));
        sDatabases.add(new CommonDatabase(getContext()));
        for (AbstractDatabase database : sDatabases) {
            addTable(database.getTables());
        }
        return true;
    }

    public static void addMatchURI(String path, int code) {
        URI_MATCHER.addURI(AUTHORITY, path, code);
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

    /**
     * 测试 方法调用
     * @param method
     * @param arg
     * @param extras
     * @return
     */
    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        switch (method){
            case "testMethod":
                Bundle bundle = new Bundle();
                bundle.putString("testResult","testValue");
                return  bundle;
        }
        return super.call(method, arg, extras);
    }

    /**
     * 测试打开文件
     * @param uri
     * @param mode
     * @return
     * @throws FileNotFoundException
     */
    @Nullable
    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        return super.openFile(uri, mode);
    }

    @Nullable
    @Override
    public AssetFileDescriptor openAssetFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        return super.openAssetFile(uri, mode);
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
