package com.android.hq.androiddbdemo.signle;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heqiang on 17-9-1.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "Test";
    private static final String DB_NAME = "demo.db";
    private static final int DB_VERSION = 1;

    private static Context sContext;
    private static DBOpenHelper sInstance;
    public final Object STUDENT_TABLE_LOCK = new Object();

    private DBOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    // onUpgrade 里面对数据库的改动也要在 onCreate 里面体现出来，使用户清除数据时重新建表是也会生效
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("Test","onUpgrade oldVersion = "+oldVersion+", newVersion = "+newVersion);
        if(oldVersion < 2){
            db.execSQL("ALTER TABLE " + StudentTAB.TABLE + " ADD " + StudentTAB.SPECIALTY + " TEXT");
        }
        if(oldVersion < 3){
            db.execSQL("ALTER TABLE " + StudentTAB.TABLE + " ADD " + StudentTAB.IS_BOARDER + " INTEGER");
        }
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(SQLS.CREATE_TAB_STUDENTS);
    }

    public static synchronized DBOpenHelper newInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBOpenHelper(context);
            sContext = context;
        }
        return sInstance;
    }

    public static synchronized DBOpenHelper getInstance() {
        return sInstance;
    }

    interface SQLS {
        String CREATE_TAB_STUDENTS =
                "CREATE TABLE " + StudentTAB.TABLE
                        + "("
                        + StudentTAB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + StudentTAB.NAME + " TEXT,"
                        + StudentTAB.GENDER + " TEXT,"
                        + StudentTAB.GRADE +  " INTEGER,"
                        + StudentTAB.CLASS + " INTEGER,"
                        + StudentTAB.COUNTRY + " TEXT,"
                        + StudentTAB.PROVINCE + " TEXT,"
                        + StudentTAB.SPECIALTY + " TEXT,"
                        + StudentTAB.IS_BOARDER + " INTEGER"
                        + ");";
    }

    public static final class StudentTAB {
        public static final String TABLE = "students_tab";

        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String GENDER = "gender";
        public static final String GRADE = "grade";
        public static final String CLASS = "class";
        public static final String COUNTRY = "country";
        public static final String PROVINCE = "province";
        public static final String SPECIALTY = "specialty";
        public static final String IS_BOARDER = "is_boarder";

        public static ContentValues parseContentValue(StudentBean bean){
            ContentValues contentValues = new ContentValues();

            contentValues.put(NAME,bean.getName());
            contentValues.put(GENDER,bean.getGender());
            contentValues.put(GRADE,bean.getGrade());
            contentValues.put(CLASS,bean.getCls());
            contentValues.put(COUNTRY,bean.getCountry());
            contentValues.put(PROVINCE,bean.getProvince());
            contentValues.put(SPECIALTY,bean.getSpecialty());
            contentValues.put(IS_BOARDER,bean.isBoarder());

            return contentValues;
        }
    }


    //----------------------
    private void doApplyOperations(Uri uri,
                                   ArrayList<ContentProviderOperation> ops) {
        try {
            sContext.getContentResolver().applyBatch(uri.getAuthority(), ops);
        } catch (Exception e) {
            Log.d(TAG, "doApplyOperations error!", e);
        }
    }

    public void saveStudent(List<StudentBean> studentList){
        if(studentList == null || studentList.size() == 0){
            return;
        }

        Uri uri = StudentProvider.StudentUri.URI_STUDENTS;
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        for(StudentBean bean : studentList){
            ContentValues contentValues = StudentTAB.parseContentValue(bean);
            ContentProviderOperation operation = ContentProviderOperation
                    .newInsert(uri).withValues(contentValues).build();
            ops.add(operation);
        }

        synchronized (STUDENT_TABLE_LOCK) {
            doApplyOperations(uri, ops);
        }
    }
}
