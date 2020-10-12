package com.android.hq.androiddbdemo.multi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class AbstractDatabase extends SQLiteOpenHelper {

    private List<Table> mTables;

    public AbstractDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mTables = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Table table : mTables) {
            table.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Table table : mTables) {
            table.onUpgrade(db, oldVersion, newVersion);
        }
    }

    public void addTable(Table table) {
        if (table != null) {
            mTables.add(table);
        }
    }

    public void addTables(List<Table> tables) {
        if (tables != null) {
            mTables.addAll(tables);
        }
    }

    public List<Table> getTables() {
        return mTables;
    }
}
