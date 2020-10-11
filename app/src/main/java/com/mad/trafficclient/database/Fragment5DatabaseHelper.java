package com.mad.trafficclient.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Fragment5DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database_f5.db";
    private static final String TABLE_NAME = "env_info";

    public Fragment5DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "create table if not exists " + TABLE_NAME + " (_id integer primary key autoincrement, " +
                "temperature integer, humidity integer, lightIntensity integer, pm25 integer, co2 integer, road integer)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
