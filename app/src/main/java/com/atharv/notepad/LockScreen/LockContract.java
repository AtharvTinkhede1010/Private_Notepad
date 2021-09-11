package com.atharv.notepad.LockScreen;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class LockContract {

    private LockContract(){}

    public static final class LockEntry implements BaseColumns {

        public static final String CONTENT_AUTHORITY = "com.atharv.notepad.LockScreen";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_NOTE = "lock";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTE;
        public static final String TABLE_NAME = "lock";
        public static final String COLUMN_PASS = "Password";
        public final static String _ID = BaseColumns._ID;

    }



}
