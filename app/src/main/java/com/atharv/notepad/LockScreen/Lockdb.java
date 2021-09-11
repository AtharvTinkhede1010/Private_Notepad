package com.atharv.notepad.LockScreen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.atharv.notepad.LockScreen.LockContract.LockEntry;
public class Lockdb extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "data.db";
    public static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_GIFT_TABLE = "CREATE TABLE " + LockEntry.TABLE_NAME + "(" + LockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LockEntry.COLUMN_PASS + " TEXT);";
        db.execSQL(SQL_CREATE_GIFT_TABLE);
    }

    public Lockdb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
