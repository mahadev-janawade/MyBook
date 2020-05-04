package com.myapp.maddy.mybook;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maddy on 5/2/2020.
 */

public class MyBookHelperClass extends SQLiteOpenHelper {

    MyBookHelperClass(Context context){
        super(context,"mydata.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE MYBOOK ( date text, content text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MYBOOK");
    }
}
