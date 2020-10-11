package com.mad.trafficclient.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Fragment1DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database_f1.db";
    private static final String TABLE_NAME = "recharge_log";

    public Fragment1DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /*
    * 调用时刻：当数据库第一次创建时调用
    * 作用： 创建数据库 表 & 初始化数据
    * SQLite数据库创建支持的数据类型：整型数据、字符串类型、日期类型、二进制
    * */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTbStr = "create table if not exists " + TABLE_NAME + "(_id integer primary key autoincrement," +
                " car_number integer, recharge_money integer, name varchar, time text)";
        sqLiteDatabase.execSQL(createTbStr);
        // 注： 数据库实际上是没被创建 / 打开的（因该方法还没调用）
        // 直到getWritableDatabase() / getReadableDatabase() 第一次被调用才会进行创建 / 打开

    }

    /*
    * 调用时刻：当数据库升级时则自动调用（即 数据库版本 发生变化时)
    * 作用：更新数据库表结构
    * 注： 创建SQLiteOpenHelper字类对象时，必须传入一个version参数，该参数 = 当前数据库版本，若该版本高于之前版本，就调用onUpgrade()
    * */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
