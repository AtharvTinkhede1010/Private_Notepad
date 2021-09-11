package com.atharv.notepad;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.atharv.notepad.data.NoteContract.NoteEntry;
import com.atharv.notepad.setting.Constant;
import com.atharv.notepad.setting.Setting;
import com.atharv.notepad.setting.Setting_Contract.SettingEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

@SuppressLint("Registered")
public class container extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    NoteCursorAdapter mCursorAdapter;
    boolean doubleBackToExitPressedOnce = false;
    private static boolean list_visibile = false;
    private ViewStub listview, gridview;
    private ListView list;
    private GridView grid;
    private MenuItem m1;
    Constant constant;
    SharedPreferences app_preferences;
    int appTheme;
    int themeColor;
    int appColor;
    private String[] projectionsetting;
    private Cursor cursorSetting;


    boolean mISCancel;
    float mBottomListStartY, mStart;
    boolean resetBottomList;


    @SuppressLint({"WrongViewCast", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        Log.v("", "AppColor:-" + appColor + " AppTheme:-" + appTheme);
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
        setContentView(R.layout.activity_container);


        //progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), Mode.SRC_IN);
        getSupportActionBar().setElevation(0);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(container.this, editpad.class);
                startActivity(intent);
            }
        });

        listview = (ViewStub) findViewById(R.id.list);
        gridview = (ViewStub) findViewById(R.id.grid);

        listview.inflate();
        gridview.inflate();

        list = (ListView) findViewById(R.id.listid);
        grid = (GridView) findViewById(R.id.gridid);

        onclick();

        getLoaderManager().initLoader(0, null, this);

        displayDatabaseInfo();
/*

        SearchView searchView=(SearchView)findViewById(R.id.search1);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
*/


    }

    private void onclick() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(container.this, editpad.class);
                Uri currentPetUri = ContentUris.withAppendedId(NoteEntry.CONTENT_URI, id);

                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(container.this, R.anim.paper_right, R.anim.paper_left);


                intent.setData(currentPetUri);
                container.this.startActivity(intent, options.toBundle());

            }
        });
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(container.this, editpad.class);
                Uri currentPetUri = ContentUris.withAppendedId(NoteEntry.CONTENT_URI, id);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(container.this, R.anim.paper_right, R.anim.paper_left);

                intent.setData(currentPetUri);
                startActivity(intent,options.toBundle());
            }
        });
    }

    private void displayDatabaseInfo() {
        String[] projection = {
                NoteEntry._ID,
                NoteEntry.COLUMN_SUB,
                NoteEntry.COLUMN_DIS
        };
        Cursor cursor = getContentResolver().query(NoteEntry.CONTENT_URI, projection, null, null, NoteEntry.COLUMN_SUB + " ASC");
        mCursorAdapter = new NoteCursorAdapter(container.this, cursor, 0);
        if (cursor.moveToNext()) {
            findViewById(R.id.empty_subtitle_text).setVisibility(View.VISIBLE);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_1, menu);
        m1 = menu.findItem(R.id.action_layout);
        MenuItem m2 = menu.findItem(R.id.settings);
        Layouts();
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    public void Layouts() {
        if (list_visibile) {
            m1.setTitle("List view");
            listview.setVisibility(View.GONE);
            gridview.setVisibility(View.VISIBLE);
            grid.setAdapter(mCursorAdapter);
        } else {
            m1.setTitle("Grid View");
            gridview.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            list.setAdapter(mCursorAdapter);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        //if the current view is the listview, passes to gridview
        switch (item.getItemId()) {
            case R.id.action_layout:
                if (list_visibile) {
                    m1.setTitle("Grid view");
                    listview.setVisibility(View.VISIBLE);
                    gridview.setVisibility(View.GONE);
                    list.setAdapter(mCursorAdapter);
                    list_visibile = false;
                } else {
                    m1.setTitle("List View");
                    gridview.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                    grid.setAdapter(mCursorAdapter);
                    list_visibile = true;
                }
                return true;
            case R.id.settings:
                Intent intent = new Intent(container.this, Setting.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                NoteEntry._ID,
                NoteEntry.COLUMN_SUB,
                NoteEntry.COLUMN_DIS};

        return new CursorLoader(this,
                NoteEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToNext()) {
            findViewById(R.id.empty_subtitle_text).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.empty_subtitle_text).setVisibility(View.VISIBLE);
        }
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

/*    public void animate(View view) {
        if (!mISCancel) {
            if (mStartX == 0.0f) {
                mStartX = view.getX();
                mStartY = view.getY();

                mBottomX = getBottomFilterXPosition();
                mBottomY = getBottomFilteryPosition();

                mBottomListStartY = mBinding.bottomListBackground.getY();
            }
            final int finalX = getBottomFilterXPosition();
            final int finalY = getBottomFilteryPosition();

            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.addUpdateListener(){
            float v = (float) animation.getAnimatedValue();
            mBinding.fab.setX(
                    finalX + (mStartX - finalX - ((mStartX - finalX) * v))
            );
            mBinding.fab.setY(
                    finalY + (mStartY - finalY - ((mStartY - finalY) * v))
            );
        }
        //https://www.youtube.com/watch?v=IECLEh98HnE
    }
}*/
}
