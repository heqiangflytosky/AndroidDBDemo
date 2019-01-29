package com.android.hq.androiddbdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by heqiang on 2019/1/24.
 */

public class CompanyDatabase extends AbstractDatabase {
    private static final String DB_NAME = "company.db";
    private static final int DB_VERSION = 1;

    private Context mContext;

    public CompanyDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }
}
