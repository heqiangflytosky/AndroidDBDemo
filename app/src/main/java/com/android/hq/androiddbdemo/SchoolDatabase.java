package com.android.hq.androiddbdemo;

import android.content.Context;

/**
 * Created by heqiang on 2019/1/24.
 */

public class SchoolDatabase extends AbstractDatabase {
    private static final String DB_NAME = "school.db";
    private static final int DB_VERSION = 2;

    private Context mContext;

    public SchoolDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
        // 添加该数据库包含的所有表
        addTable(new StudentTable(this));
        addTable(new TeacherTable(this));
    }

    public Context getContext() {
        return mContext;
    }
}
