package com.android.hq.androiddbdemo.sqlbrite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.hq.androiddbdemo.single.StudentBean;

/**
 * Created by heqiang on 17-9-1.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "Test";
    public static final String DB_NAME = "sqlbrite.db";
    private static final int DB_VERSION = 1;

    private static Context sContext;
    private static DBOpenHelper sInstance;

    private DBOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(SQLS.CREATE_TAB_STUDENTS);
    }

    public static synchronized DBOpenHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBOpenHelper(context);
            sContext = context;
        }
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
}
