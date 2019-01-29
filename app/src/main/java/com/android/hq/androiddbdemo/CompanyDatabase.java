package com.android.hq.androiddbdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class CompanyDatabase extends AbstractDatabase {
    private static final String DB_NAME = "company.db";
    private static final int DB_VERSION = 1;

    private Context mContext;

    public CompanyDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
        // 添加该数据库包含的所有表
        addTable(new RDCenterTable(this));
    }

    public Context getContext() {
        return mContext;
    }
}
