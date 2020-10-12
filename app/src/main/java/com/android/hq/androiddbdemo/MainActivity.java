package com.android.hq.androiddbdemo;

import android.content.AsyncQueryHandler;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.hq.androiddbdemo.multi.DemoProvider;
import com.android.hq.androiddbdemo.multi.RDCenterTable;
import com.android.hq.androiddbdemo.multi.StudentTable;
import com.android.hq.androiddbdemo.multi.TeacherTable;
import com.android.hq.androiddbdemo.single.DBOpenHelper;
import com.android.hq.androiddbdemo.sqlbrite.DataSource;
import com.facebook.stetho.Stetho;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MyQueryHandler mQueryHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mQueryHandler = new MyQueryHandler(getContentResolver());
        //getContentResolver().registerContentObserver(StudentProvider.StudentUri.URI_STUDENTS, true, mObserver);
        getContentResolver().registerContentObserver(StudentTable.URI_STUDENT, true, mObserver);
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

        final ContentValues xiaoming = new ContentValues();

        xiaoming.put(StudentTable.Columns.NAME,"XiaoMing");
        xiaoming.put(StudentTable.Columns.GENDER,"male");
        xiaoming.put(StudentTable.Columns.GRADE,1);
        xiaoming.put(StudentTable.Columns.CLASS,1);
        xiaoming.put(StudentTable.Columns.COUNTRY,"China");
        //xiaoming.put(StudentTable.Columns.PROVINCE,null);
        xiaoming.put(StudentTable.Columns.SPECIALTY,"swimming");
        xiaoming.put(StudentTable.Columns.IS_BOARDER,true);

        final ContentValues xiaoling = new ContentValues();

        xiaoling.put(StudentTable.Columns.NAME,"XiaoLing");
        xiaoling.put(StudentTable.Columns.GENDER,"male");
        xiaoling.put(StudentTable.Columns.GRADE,2);
        xiaoling.put(StudentTable.Columns.CLASS,3);
        xiaoling.put(StudentTable.Columns.COUNTRY,"USA");
        //xiaoling.put(StudentTable.Columns.PROVINCE,null);
        xiaoling.put(StudentTable.Columns.SPECIALTY,"draw");
        xiaoling.put(StudentTable.Columns.IS_BOARDER,true);


        final ContentValues teacherLi = new ContentValues();
        teacherLi.put(TeacherTable.Columns._ID, 100001);
        teacherLi.put(TeacherTable.Columns.NAME, "LiHua");
        teacherLi.put(TeacherTable.Columns.SENIORITY, 5);
        teacherLi.put(TeacherTable.Columns.UPDATE_TIME, System.currentTimeMillis());

        final ContentValues engineer1 = new ContentValues();
        engineer1.put(RDCenterTable.Columns._ID, 666666);
        engineer1.put(RDCenterTable.Columns.NAME, "HQ");
        engineer1.put(RDCenterTable.Columns.SENIORITY, 5);
        engineer1.put(RDCenterTable.Columns.UPDATE_TIME, System.currentTimeMillis());


        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = StudentTable.URI_STUDENT;

                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                ContentProviderOperation operation = ContentProviderOperation
                        .newInsert(uri).withValues(xiaoming).build();
                ops.add(operation);

                ContentProviderOperation operation2 = ContentProviderOperation
                        .newInsert(uri).withValues(xiaoling).build();
                ops.add(operation2);

                try {
                    getContentResolver().applyBatch(uri.getAuthority(), ops);

                    getContentResolver().insert(TeacherTable.URI_TEACHER, teacherLi);

                    getContentResolver().insert(RDCenterTable.URI_TEACHER, engineer1);
                } catch (Exception e) {
                    Log.d("Test", "doApplyOperations error!", e);
                }
            }
        }).start();

//        StudentBean James = new StudentBean();
//        James.setName("James");
//        James.setGender("male");
//        James.setGrade(1);
//        James.setCls(1);
//        James.setBoarder(false);
//        James.setCountry("USA");
//        James.setSpecialty("basketball");
//
//        mQueryHandler.startInsert(0 ,0 ,StudentProvider.StudentUri.URI_STUDENTS, DBOpenHelper.StudentTAB.parseContentValue(James));
//
//        mQueryHandler.startQuery(0 ,0 ,StudentProvider.StudentUri.URI_STUDENTS,
//                new String []{DBOpenHelper.StudentTAB.NAME},
//                DBOpenHelper.StudentTAB.COUNTRY + "=? AND " + DBOpenHelper.StudentTAB.GENDER + "=?",
//                new String []{"China", "male"},
//                null);
    }

    private ContentObserver mObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mQueryHandler.startQuery(0,0,StudentTable.URI_STUDENT, null, null, null,null);
        }
    };

    public void testGetInfo(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = getContentResolver().query(DemoProvider.AUTHORITY_URI.buildUpon().appendEncodedPath("common/get_info/James").build(),
                        null,null,null,null);
                if(cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        int age = cursor.getInt(cursor.getColumnIndex("age"));
                        Log.e("Test","name = "+name+",id="+id+",age="+age);
                    }
                }
            }
        }).start();
    }

    public void testMethod(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putString("testKey","testValue");
                Bundle result = getContentResolver().call(DemoProvider.AUTHORITY_URI,"testMethod","testArg",bundle);
                Log.e("Test",result.toString());
            }
        }).start();

    }

    public void testSqlBrite() {

    }

    public void testOpenFile(View view) {
        try {
            getContentResolver().openFileDescriptor(DemoProvider.AUTHORITY_URI.buildUpon()
                    .appendEncodedPath("test_file").build(),"r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void testSqlBriteAdd(View view) {
        DataSource.getInstance(this).saveData();
    }

    public void testSqlBriteQuery(View view) {
        DataSource.getInstance(this).queryData();
    }

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
