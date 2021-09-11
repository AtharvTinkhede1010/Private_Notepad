package com.atharv.notepad;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.atharv.notepad.data.NoteContract.NoteEntry;
import com.atharv.notepad.setting.Constant;
import com.atharv.notepad.setting.Methods;
import com.atharv.notepad.setting.Setting_Contract.SettingEntry;


public class editpad extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    EditText E1, E2;
    Uri mCurrentPetUri;
    private static final int EXISTING_LOADER = 0;
    Constant constant;
    String[] projectionsetting;
    Cursor cursorSetting;
    int appTheme=0;
    int themeColor=0;
    int appColor=0;
    private SharedPreferences.Editor editor;


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    };
    private SharedPreferences sharedPreferences;
    private Methods methods;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        projectionsetting = new String[]{
                SettingEntry.COLUMN_PROFILE,
                SettingEntry.COLUMN_THEME,
                SettingEntry.COLUMN_LOCK_TEXT_COLOR,
                SettingEntry.COLUMN_TEXTSIZE,
                SettingEntry.COLUMN_APP_COLOR,
                SettingEntry.COLUMN_APP_THEME
        };
        cursorSetting = getContentResolver().query(SettingEntry.CONTENT_URI, projectionsetting, null, null, null);
        cursorSetting.moveToFirst();
        appColor = Integer.parseInt(cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_APP_COLOR)));
        appTheme = Integer.parseInt(cursorSetting.getString(cursorSetting
                .getColumnIndex(SettingEntry.COLUMN_APP_THEME)));
Log.v("","AppColor:-"+appColor+" AppTheme:-"+appTheme);
        themeColor = appColor;
        Constant.color = appColor;

        Constant.theme = R.style.AppTheme_pink;
        if (themeColor == 0) {
            setTheme(Constant.theme);
        } else if (appTheme == 0) {
            setTheme(Constant.theme);
        } else {
            setTheme(appTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);



        E1 = (EditText)

                findViewById(R.id.Sub);

        E2 = (EditText)

                findViewById(R.id.Dis);

        getSupportActionBar().

                setElevation(0);

        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();
        if (mCurrentPetUri == null) {
            setTitle(getString(R.string.editor_activity_title_new));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit));
            getLoaderManager().initLoader(EXISTING_LOADER, null, this);
            setText();
        }


        E1.setOnTouchListener(mTouchListener);
        E2.setOnTouchListener(mTouchListener);
        Log.v("", "TextSize:-" + E1.getTextSize());

        TextSize();

    }

    private void TextSize() {

        if (SettingEntry.Textsize_Small.equals(cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_TEXTSIZE)))) {
            E1.setTextSize(15);
            E2.setTextSize(15);
        } else if (SettingEntry.Textsize_Medium.equals(cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_TEXTSIZE)))) {
            E1.setTextSize(20);
            E2.setTextSize(20);
        } else {
            E1.setTextSize(25);
            E2.setTextSize(25);
        }

    }

    private void setText() {

        String[] projection = {
                NoteEntry._ID,
                NoteEntry.COLUMN_SUB,
                NoteEntry.COLUMN_DIS
        };
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(mCurrentPetUri, projection, NoteEntry._ID, new String[]{String.valueOf(ContentUris.parseId(mCurrentPetUri))}, null);


        cursor.moveToFirst();


        int nameColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_SUB);
        int breedColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_DIS);

        Log.v("", "nameColumnIndex" + nameColumnIndex + " " + breedColumnIndex);
        String name = cursor.getString(nameColumnIndex);
        String breed = cursor.getString(breedColumnIndex);

        Log.v("", "Subject:-" + name);
        E1.setText(name);
        E2.setText(breed);


    }


    @Override
    public void onBackPressed() {
        save();
        Intent intent = new Intent(editpad.this, container.class);

        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(editpad.this, R.anim.paper_right, R.anim.paper_left);


        editpad.this.startActivity(intent, options.toBundle());


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_2, menu);
        for(int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }


    @SuppressLint("ResourceAsColor")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                showDeleteConfirmationDialog();
                return true;
            case R.id.cancel:
                finish();
                return true;
            case R.id.save:
                save();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        //dbHandler.addHandler(E1.getText().toString(), E2.getText().toString());
        String Subject = E1.getText().toString().trim();
        String Discription = E2.getText().toString().trim();

        if (mCurrentPetUri == null && TextUtils.isEmpty(Subject) && TextUtils.isEmpty(Discription)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_SUB, Subject);
        values.put(NoteEntry.COLUMN_DIS, Discription);
        if (mCurrentPetUri == null) {
            getContentResolver().insert(NoteEntry.CONTENT_URI, values);
        } else {
            getContentResolver().update(mCurrentPetUri, values, null, null);
            if (TextUtils.isEmpty(Subject) && TextUtils.isEmpty(Discription)) {
                delete();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                NoteEntry._ID,
                NoteEntry.COLUMN_SUB,
                NoteEntry.COLUMN_DIS,
        };
        CursorLoader sbs = new CursorLoader(this,
                mCurrentPetUri,
                projection,
                null,
                null,
                null);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_SUB);
            int breedColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_DIS);

            String name = cursor.getString(nameColumnIndex);
            String breed = cursor.getString(breedColumnIndex);


            E1.setText(name);
            E2.setText(breed);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        E1.setText("");
        E2.setText("");
    }

    private void delete() {
        if (mCurrentPetUri != null) {
            getContentResolver().delete(mCurrentPetUri, null, null);
        }
        finish();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete();
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        builder.show();
    }
}
