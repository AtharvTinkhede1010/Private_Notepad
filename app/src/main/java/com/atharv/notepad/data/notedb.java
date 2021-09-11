package com.atharv.notepad.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.atharv.notepad.data.NoteContract.NoteEntry;

public class notedb extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Nodata.db";
    public static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_GIFT_TABLE = "CREATE TABLE " + NoteEntry.TABLE_NAME + "(" + NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NoteEntry.COLUMN_SUB + " TEXT," + NoteEntry.COLUMN_DIS + " TEXT);";
        db.execSQL(SQL_CREATE_GIFT_TABLE);
    }

    public notedb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void addHandler(String a, String b) {
        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_SUB, a);
        values.put(NoteEntry.COLUMN_DIS, b);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(NoteEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void updateHandler() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(NoteEntry.COLUMN_SUB, 1);
        args.put(NoteEntry.COLUMN_DIS, 1);
        db.update(NoteEntry.TABLE_NAME, args, NoteEntry.COLUMN_SUB + "=" + 0, null);
    }

    public boolean loadhander() {
        String query = "Select * FROM " + NoteEntry.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        cursor.moveToNext();
        int a = cursor.getInt(0);
        db.close();
        if (a == 0) {
            return false;
        } else {
            return true;
        }
    }
}