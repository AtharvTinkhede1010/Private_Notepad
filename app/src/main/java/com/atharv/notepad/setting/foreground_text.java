package com.atharv.notepad.setting;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.atharv.notepad.R;
import com.atharv.notepad.setting.Setting_Contract.SettingEntry;

public class foreground_text extends AppCompatActivity {
    private RadioButton radiobutton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String[] projection = new String[]{
                SettingEntry.COLUMN_PROFILE,
                SettingEntry.COLUMN_THEME,
                SettingEntry.COLUMN_LOCK_TEXT_COLOR,
                SettingEntry.COLUMN_TEXTSIZE,
                SettingEntry.COLUMN_APP_COLOR,
                SettingEntry.COLUMN_APP_THEME
        };
        final Cursor cursorSetting = getContentResolver().query(SettingEntry.CONTENT_URI, projection, null, null, null);
        cursorSetting.moveToFirst();
        int appColor = Integer.parseInt(cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_APP_COLOR)));
        int appTheme = Integer.parseInt(cursorSetting.getString(cursorSetting
                .getColumnIndex(SettingEntry.COLUMN_APP_THEME)));
        Log.v("","AppColor:-"+appColor+" AppTheme:-"+appTheme);
        int themeColor = appColor;
        Constant.color = appColor;

        Constant.theme = R.style.AppTheme_pink;
        if (themeColor == 0) {
            setTheme(Constant.theme);
        } else if (appTheme == 0) {
            setTheme(Constant.theme);
        } else {
            setTheme(appTheme);
        }


        setContentView(R.layout.lock_foreground);

        setTitle("Forground");

        final RelativeLayout R1 = (RelativeLayout) findViewById(R.id.Lock_screen);
        RelativeLayout R2 = (RelativeLayout) findViewById(R.id.Lock_screen2);


        R1.setBackgroundColor(Integer.parseInt(cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_THEME))));
        R2.setBackgroundColor(Integer.parseInt(cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_THEME))));



        final ContentValues values = new ContentValues();


        RadioGroup RG = (RadioGroup) findViewById(R.id.checkbox);

        if (cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_LOCK_TEXT_COLOR)).equals(SettingEntry.Lock_forground_Black)) {
            RG.check(R.id.black);
        }else {
            RG.check(R.id.white);
        }
        RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radiobutton = (RadioButton) findViewById(selectedId);
                switch (radiobutton.getText().toString()) {
                    case "Black":
                        Log.v("", "Black");
                        values.put(SettingEntry.COLUMN_LOCK_TEXT_COLOR, SettingEntry.Lock_forground_Black);
                        //TransitionManager.go(Scene.getSceneForLayout(R1,R.layout.activity_setting,foreground_text.this));
                        finish();
                        break;
                    case "White":
                        Log.v("", "White");
                        values.put(SettingEntry.COLUMN_LOCK_TEXT_COLOR, SettingEntry.Lock_forground_White);
                        finish();
                        break;
                }
                getContentResolver().update(SettingEntry.CONTENT_URI, values, null, null);
                cursorSetting.close();
            }
        });
    }
}
