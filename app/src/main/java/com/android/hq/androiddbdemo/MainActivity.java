package com.android.hq.androiddbdemo;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MyQueryHandler mQueryHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueryHandler = new MyQueryHandler(getContentResolver());
        getContentResolver().registerContentObserver(StudentProvider.StudentUri.URI_STUDENTS, true, mObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
    }

    public void addData(View view){
        addStudents();
    }

    private void addStudents(){
        final ArrayList<StudentBean> list = new ArrayList();
        StudentBean xiaoMing = new StudentBean();
        xiaoMing.setName("XiaoMing");
        xiaoMing.setGender("male");
        xiaoMing.setGrade(1);
        xiaoMing.setCls(1);
        xiaoMing.setBoarder(true);
        xiaoMing.setCountry("China");
        xiaoMing.setSpecialty("swimming");
        list.add(xiaoMing);

        StudentBean xiaoLing = new StudentBean();
        xiaoLing.setName("XiaoLing");
        xiaoLing.setGender("female");
        xiaoLing.setGrade(1);
        xiaoLing.setCls(1);
        xiaoLing.setBoarder(false);
        xiaoLing.setCountry("China");
        xiaoLing.setSpecialty("painting");
        list.add(xiaoLing);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DBOpenHelper.getInstance().saveStudent(list);
            }
        }).start();

        StudentBean James = new StudentBean();
        James.setName("James");
        James.setGender("male");
        James.setGrade(1);
        James.setCls(1);
        James.setBoarder(false);
        James.setCountry("USA");
        James.setSpecialty("basketball");

        mQueryHandler.startInsert(0 ,0 ,StudentProvider.StudentUri.URI_STUDENTS, DBOpenHelper.StudentTAB.parseContentValue(James));

        mQueryHandler.startQuery(0 ,0 ,StudentProvider.StudentUri.URI_STUDENTS,
                new String []{DBOpenHelper.StudentTAB.NAME},
                DBOpenHelper.StudentTAB.COUNTRY + "=? AND " + DBOpenHelper.StudentTAB.GENDER + "=?",
                new String []{"China", "male"},
                null);
    }

    private ContentObserver mObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mQueryHandler.startQuery(0,0,StudentProvider.StudentUri.URI_STUDENTS, null, null, null,null);
        }
    };

    private static class MyQueryHandler extends AsyncQueryHandler{

        public MyQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            Log.e("Test","---------------------->");
            if(cursor != null){
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(DBOpenHelper.StudentTAB.NAME));

                    Log.e("Test","onQueryComplete name = "+name);
                }
            }
            Log.e("Test","<----------------------");
        }
    }
}
