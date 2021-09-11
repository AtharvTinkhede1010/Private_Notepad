package com.atharv.notepad.setting;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class Setting_Contract {

    private Setting_Contract(){}

    public static final class SettingEntry implements BaseColumns {

        public static final String CONTENT_AUTHORITY = "com.atharv.notepad.setting";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_NOTE = "setting";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTE;
        public static final String TABLE_NAME = "setting";


        public static final String COLUMN_PROFILE="Profile_pic";
        public static final String COLUMN_THEME = "Theme_Color";
        public static final String COLUMN_APP_THEME="App_Theme";
        public static final String COLUMN_APP_COLOR="App_Color";
        public static final String COLUMN_LOCK_TEXT_COLOR="Lock_text_color";
        public static final String COLUMN_TEXTSIZE="Text_Size";
        public static final String COLUMN_fINGERPRINT="finger_print";
        public final static String _ID = BaseColumns._ID;

        public static final String Finger_Enable="ENABLE";
        public static final String Finger_Disable="DISABLE";




        public static final String Textsize_Small="Small";
        public static final String Textsize_Medium="Medium";
        public static final String Textsize_Large="Large";

        public static final String Lock_forground_White="White";
        public static final String Lock_forground_Black="Black";
    }



}
