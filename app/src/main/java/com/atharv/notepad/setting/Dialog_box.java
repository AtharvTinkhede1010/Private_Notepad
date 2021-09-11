package com.atharv.notepad.setting;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.biometric.BiometricPrompt;

import com.atharv.notepad.R;
import com.atharv.notepad.setting.Setting_Contract.SettingEntry;

import static android.content.DialogInterface.OnClickListener;

public class Dialog_box {
    public String[] projection;
    private int pixel;
    public Cursor cursor;
    private ImageView mimageview;
    private ContentResolver CR;
    private Setting S1;


    public Dialog_box(Setting s, ContentResolver c) {
        S1 = s;
        CR = c;
    }

    public void Textsize() {
        projection = new String[]{
                SettingEntry.COLUMN_PROFILE,
                SettingEntry.COLUMN_THEME,
                SettingEntry.COLUMN_LOCK_TEXT_COLOR,
                SettingEntry.COLUMN_TEXTSIZE
        };
        cursor = CR.query(SettingEntry.CONTENT_URI, projection, null, null, null);
        cursor.moveToFirst();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(S1);
        alertDialog.setTitle("AlertDialog");
        String[] items = {"Small", "Medium", "Large"};
        int checkedItem = 1;
        if (SettingEntry.Textsize_Small.equals(cursor.getString(cursor.getColumnIndex(SettingEntry.COLUMN_TEXTSIZE))))
            checkedItem = 0;
        else if (SettingEntry.Textsize_Medium.equals(cursor.getString(cursor.getColumnIndex(SettingEntry.COLUMN_TEXTSIZE))))
            checkedItem = 1;
        else
            checkedItem = 2;
        alertDialog.setSingleChoiceItems(items, checkedItem, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ContentValues values1 = new ContentValues();
                        values1.put(SettingEntry.COLUMN_TEXTSIZE, SettingEntry.Textsize_Small);
                        CR.update(SettingEntry.CONTENT_URI, values1, null, null);
                        Toast.makeText(S1, SettingEntry.Textsize_Small, Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        ContentValues values2 = new ContentValues();
                        values2.put(SettingEntry.COLUMN_TEXTSIZE, SettingEntry.Textsize_Medium);
                        CR.update(SettingEntry.CONTENT_URI, values2, null, null);
                        Toast.makeText(S1, SettingEntry.Textsize_Medium, Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        ContentValues values3 = new ContentValues();
                        values3.put(SettingEntry.COLUMN_TEXTSIZE, SettingEntry.Textsize_Large);
                        CR.update(SettingEntry.CONTENT_URI, values3, null, null);
                        Toast.makeText(S1, SettingEntry.Textsize_Large, Toast.LENGTH_LONG).show();
                        break;
                }
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void LockBackground() {
        final Dialog dialog = new Dialog(S1);
        dialog.setContentView(R.layout.colorselector);
        dialog.setTitle("");
        mimageview = (ImageView) dialog.findViewById(R.id.imageView);
        final View mColorView = dialog.findViewById(R.id.colorView);
        final View mColorView2 = dialog.findViewById(R.id.colorView2);


        projection = new String[]{
                SettingEntry.COLUMN_PROFILE,
                SettingEntry.COLUMN_THEME,
                SettingEntry.COLUMN_LOCK_TEXT_COLOR,
                SettingEntry.COLUMN_TEXTSIZE
        };
        cursor = CR.query(SettingEntry.CONTENT_URI, projection, null, null, null);
        cursor.moveToFirst();
        mColorView.setBackgroundColor(cursor.getInt(cursor.getColumnIndex(SettingEntry.COLUMN_THEME)));
        Log.v("", "Dialog Created");
        Button button = (Button) dialog.findViewById(R.id.cancel);
        Button button2 = (Button) dialog.findViewById(R.id.apply);
        Button button3 = (Button) dialog.findViewById(R.id.Default);
        mimageview.setDrawingCacheEnabled(true);
        mimageview.buildDrawingCache(true);

        mimageview.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0 && motionEvent.getAction() != 2) return true;
                Bitmap bitmap = mimageview.getDrawingCache();
                Log.v("", "event:-" + motionEvent.getX() + "   Y:- " + motionEvent.getY());
                try {

                    pixel = bitmap.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());
                    int n = Color.red((int) pixel);
                    int n2 = Color.green((int) pixel);
                    int n3 = Color.blue((int) pixel);
                    mColorView2.setBackgroundColor(Color.rgb((int) n, (int) n2, (int) n3));
                    Log.v((String) "", "RGB:" + n + "," + n2 + "," + n3 + "\nHEX:#" + Integer.toHexString(pixel) + "\nPixel:-" + pixel);
                    if (n != 0 && n2 != 0 && n3 != 0) return true;

                } catch (IllegalArgumentException illegalArgumentException) {
                    Log.v((String) "", (String) "Out of box");
                    return true;

                }
                return true;
            }
        });
        button.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dialog.cancel();
                return true;

            }
        });
        button2.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {

                String[] projection = {
                        SettingEntry.COLUMN_PROFILE,
                        SettingEntry.COLUMN_THEME,
                        SettingEntry.COLUMN_LOCK_TEXT_COLOR,
                        SettingEntry.COLUMN_TEXTSIZE
                };


                cursor = CR.query(Setting_Contract.SettingEntry.CONTENT_URI, projection, null, null, null);
                if (cursor.moveToFirst()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(SettingEntry.COLUMN_THEME, Integer.valueOf((int) pixel));

                    CR.update(Setting_Contract.SettingEntry.CONTENT_URI, contentValues, null, null);

                }
                S1.setTheme(pixel);
                dialog.hide();
                return true;

            }
        });
        button3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                String[] projection = {
                        SettingEntry.COLUMN_PROFILE,
                        SettingEntry.COLUMN_THEME,
                        SettingEntry.COLUMN_LOCK_TEXT_COLOR,
                        SettingEntry.COLUMN_TEXTSIZE
                };


                cursor = CR.query(Setting_Contract.SettingEntry.CONTENT_URI, projection, null, null, null);
                if (cursor.moveToFirst()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(SettingEntry.COLUMN_THEME, "-13619152");
                    contentValues.put(SettingEntry.COLUMN_PROFILE,"");
                    contentValues.put(SettingEntry.COLUMN_LOCK_TEXT_COLOR, SettingEntry.Lock_forground_White);
                    CR.update(Setting_Contract.SettingEntry.CONTENT_URI, contentValues, null, null);

                }
                S1.setTheme(pixel);
                dialog.hide();
                return true;
            }
        });
        dialog.show();

    }

    public void Ltext() {
        final Dialog dialog = new Dialog(S1);

        dialog.setContentView(R.layout.lock_foreground);
        dialog.setTitle("");
        dialog.show();
    }

    public void fingerprint(final BiometricPrompt myBiometricPrompt,final BiometricPrompt.PromptInfo promptInfo) {
        final Dialog dialog = new Dialog(S1);
        dialog.setContentView(R.layout.activity_fingerprint);
        Switch sw = (Switch) dialog.findViewById(R.id.switch1);
        String[] projectionSetting = {
                SettingEntry.COLUMN_PROFILE,
                SettingEntry.COLUMN_fINGERPRINT,
                SettingEntry.COLUMN_THEME,
                SettingEntry.COLUMN_LOCK_TEXT_COLOR,
                SettingEntry.COLUMN_TEXTSIZE
        };
        Cursor cursorSetting = CR.query(SettingEntry.CONTENT_URI, projectionSetting, null, null, null);
        cursorSetting.moveToFirst();
        if(cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_fINGERPRINT)).equals(SettingEntry.Finger_Enable))
            sw.setChecked(true);
        else
            sw.setChecked(false);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myBiometricPrompt.authenticate(promptInfo);
                } else {ContentValues contentValues = new ContentValues();
                    contentValues.put(SettingEntry.COLUMN_fINGERPRINT, SettingEntry.Finger_Disable);

                    CR.update(Setting_Contract.SettingEntry.CONTENT_URI, contentValues, null, null);

                    //dialog.cancel();
                }
            }
        });
        dialog.show();
    }

}