package com.atharv.notepad.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class NoteContract {

    private NoteContract(){}

    public static final class NoteEntry implements BaseColumns{

        public static final String CONTENT_AUTHORITY = "com.atharv.notepad";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_NOTE = "Note";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTE;
        public static final String TABLE_NAME = "Note";
        public static final String COLUMN_SUB = "Subject";
        public static final String COLUMN_DIS = "Discription";
        public final static String _ID = BaseColumns._ID;

    }



}
