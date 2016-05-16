package com.coollime.coollime.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SHIM on 16. 5. 12..
 */
public class DBhelper extends SQLiteOpenHelper{

    public DBhelper(Context context) {
        super(context, "user.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE favorite_table ( id INTEGER primary key autoincrement, phoneNumber TEXT, title TEXT, thumbnail TEXT UNIQUE, method TEXT, videourl TEXT, intensity TEXT, frequency TEXT, time TEXT);");
        db.execSQL("CREATE TABLE record_table (id INTEGER primary key autoincrement, phoneNumber TEXT, date TEXT, point TEXT, danger TEXT);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS dic");
        onCreate(db);
    }




}