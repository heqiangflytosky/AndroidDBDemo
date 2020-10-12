package com.android.hq.androiddbdemo.multi;

import android.content.Context;

public class CommonDatabase extends AbstractDatabase {
    private static final String DB_NAME = "common.db";
    private static final int DB_VERSION = 1;
    public CommonDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        addTable(new ConfigTable());
    }
}
