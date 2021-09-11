package com.atharv.notepad.setting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.atharv.notepad.setting.Setting_Contract.SettingEntry;

public class Settingdb extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "setting.db";
    public static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_GIFT_TABLE = "CREATE TABLE " + SettingEntry.TABLE_NAME + "(" + SettingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SettingEntry.COLUMN_THEME + " TEXT," + SettingEntry.COLUMN_PROFILE + " BLOB," + SettingEntry.COLUMN_LOCK_TEXT_COLOR + " TEXT," + SettingEntry.COLUMN_TEXTSIZE + " INTEGER,"+SettingEntry.COLUMN_APP_THEME + " INTEGER,"+SettingEntry.COLUMN_APP_COLOR + " INTEGER,"+SettingEntry.COLUMN_fINGERPRINT+" TEXT);";
        db.execSQL(SQL_CREATE_GIFT_TABLE);
    }

    public Settingdb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
