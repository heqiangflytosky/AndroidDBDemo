package com.android.hq.androiddbdemo.sqlbrite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.android.hq.androiddbdemo.multi.DemoProvider;
import com.android.hq.androiddbdemo.multi.StudentTable;
import com.android.hq.androiddbdemo.multi.TeacherTable;
import com.squareup.sqlbrite2.BriteContentResolver;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DataSource {
    private static DataSource INSTANCE;

    private Context mContext;
    private final BriteDatabase mDatabaseHelper;
    private final BriteContentResolver mContentResolverHelper;

    private DataSource(Context context) {
        DBOpenHelper dbHelper = DBOpenHelper.getInstance(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        // 由于数据库操作是耗时操作，因此调度器选用 Schedulers.io()
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());

        mContentResolverHelper = sqlBrite.wrapContentProvider(context.getContentResolver(), Schedulers.io());
    }

    public static DataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DataSource(context);
        }
        return INSTANCE;
    }

    public void saveData() {
        final ContentValues xiaoming = new ContentValues();

        xiaoming.put(StudentTable.Columns.NAME,"XiaoMing");
        xiaoming.put(StudentTable.Columns.GENDER,"male");
        xiaoming.put(StudentTable.Columns.GRADE,1);
        xiaoming.put(StudentTable.Columns.CLASS,1);
        xiaoming.put(StudentTable.Columns.COUNTRY,"China");
        //xiaoming.put(StudentTable.Columns.PROVINCE,null);
        xiaoming.put(StudentTable.Columns.SPECIALTY,"swimming");
        xiaoming.put(StudentTable.Columns.IS_BOARDER,true);

        mDatabaseHelper.insert(DBOpenHelper.StudentTAB.TABLE, xiaoming);
    }

    public void queryData() {
        String[] projection = new String []{DBOpenHelper.StudentTAB.NAME, DBOpenHelper.StudentTAB._ID};
        String sql = String.format("SELECT %s FROM %s",
                TextUtils.join(",", projection), DBOpenHelper.StudentTAB.TABLE);
        mDatabaseHelper.createQuery(DBOpenHelper.StudentTAB.TABLE, sql)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SqlBrite.Query>() {
                    @Override
                    public void accept(SqlBrite.Query query) throws Exception {
                        Cursor cursor = query.run();
                        List<String> list = new ArrayList<String>();
                        while (cursor.moveToNext()) {
                            String username = cursor.getString(cursor.getColumnIndex(DBOpenHelper.StudentTAB.NAME));
                            int id = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.StudentTAB._ID));
                            list.add(id + "--" + username);
                        }
                        cursor.close();
                    }
                });
    }

    public void queryData1() {
        mContentResolverHelper.createQuery(DemoProvider.AUTHORITY_URI.buildUpon().appendEncodedPath("common/get_info/James").build(),
                null,null,null,null,false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SqlBrite.Query>() {
                    @Override
                    public void accept(SqlBrite.Query query) throws Exception {
                        Cursor cursor = query.run();
                        List<String> list = new ArrayList<String>();
                        while (cursor.moveToNext()) {
                            String name = cursor.getString(cursor.getColumnIndex("name"));
                            int id = cursor.getInt(cursor.getColumnIndex("id"));
                            int age = cursor.getInt(cursor.getColumnIndex("age"));
                            Log.e("Test","name = "+name+",id="+id+",age="+age);
                            list.add(id + "--" + id+"--"+age);
                        }
                        cursor.close();
                    }
                });
    }
}
