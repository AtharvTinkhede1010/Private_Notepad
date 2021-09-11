package com.atharv.notepad.setting;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.atharv.notepad.LockScreen.LockContract.LockEntry;
import com.atharv.notepad.R;
import com.atharv.notepad.setting.Setting_Contract.SettingEntry;

public class Change_password extends AppCompatActivity {

    public static final String TAG = "PinLockView";

    private PinLockView mPinLockView;
    private TextView Locktitle;
    private IndicatorDots mIndicatorDots;
    private String pin1;
    private Cursor cursor;
    private RelativeLayout R1;
    private boolean Correct = false;
    private ImageView profilepic;


    private PinLockListener mPinLockListener = new PinLockListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onComplete(String pin) {
            if (!Correct) {
                if (cursor.moveToFirst()) {
                    if (pin.equals(cursor.getString(cursor.getColumnIndex(LockEntry.COLUMN_PASS)))) {
                        mIndicatorDots.startAnimation(AnimationUtils.loadAnimation(Change_password.this, R.anim.remove));
                        mPinLockView.startAnimation(AnimationUtils.loadAnimation(Change_password.this, R.anim.remove));
                        Correct = true;
                        mPinLockView.resetPinLockView();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mIndicatorDots.startAnimation(AnimationUtils.loadAnimation(Change_password.this, R.anim.add));
                                mPinLockView.startAnimation(AnimationUtils.loadAnimation(Change_password.this, R.anim.add));
                                Locktitle.setText("NEW MPIN");
                            }
                        }, 500);
                    } else {
                        wrongpass();
                    }
                }
            } else {
                if (pin1 == null) {
                    mIndicatorDots.startAnimation(AnimationUtils.loadAnimation(Change_password.this, R.anim.remove));
                    mPinLockView.startAnimation(AnimationUtils.loadAnimation(Change_password.this, R.anim.remove));
                    pin1 = pin;
                    mPinLockView.resetPinLockView();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIndicatorDots.startAnimation(AnimationUtils.loadAnimation(Change_password.this, R.anim.add));
                            mPinLockView.startAnimation(AnimationUtils.loadAnimation(Change_password.this, R.anim.add));
                            Locktitle.setText("CONFIRM MPIN");
                        }
                    }, 500);
                } else if (pin1.equals(pin)) {
                    R1.startAnimation(AnimationUtils.loadAnimation(Change_password.this, R.anim.remove));
                    Updater(pin);
                    Toast.makeText(Change_password.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    wrongpass();
                    Locktitle.setText("Try again");
                }
            }
        }

        private void wrongpass() {
            mPinLockView.resetPinLockView();
            mIndicatorDots.startAnimation(AnimationUtils.loadAnimation(Change_password.this, R.anim.shake));
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(50);
            }
        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock);


        Locktitle = (TextView) findViewById(R.id.profile_name);
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        R1 = (RelativeLayout) findViewById(R.id.Lock_screen);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();

        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));
        profilepic = findViewById(R.id.profile_image);
        getcursor();
        setbackground();
        Locktitle.setText("Current MPIN");
        //mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);


    }

    @SuppressLint("ResourceAsColor")
    private void setbackground() {
        String[] projectionSetting = {
                SettingEntry.COLUMN_PROFILE,
                SettingEntry.COLUMN_THEME,
                SettingEntry.COLUMN_LOCK_TEXT_COLOR,
                SettingEntry.COLUMN_TEXTSIZE
        };
        Cursor cursorSetting = getContentResolver().query(SettingEntry.CONTENT_URI, projectionSetting, null, null, null);
        cursorSetting.moveToFirst();
        R1.setBackgroundColor(Integer.parseInt(cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_THEME))));

        byte[] a = cursorSetting.getBlob(cursorSetting.getColumnIndex(SettingEntry.COLUMN_PROFILE));
        Bitmap bitmap = BitmapFactory.decodeByteArray(a, 0, a.length);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getApplication().getResources(), bitmap);
        drawable.setCircular(true);
        if (a.length >= 10)
            profilepic.setImageBitmap(bitmap);

        if (cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_LOCK_TEXT_COLOR)).equals(SettingEntry.Lock_forground_Black)) {
            Locktitle.setTextColor(R.color.black);
            mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.black));
        }


        cursorSetting.close();
    }


    private void Updater(String pin) {

        ContentValues values = new ContentValues();
        values.put(LockEntry.COLUMN_PASS, pin);
        Log.v("", "Pin:-" + pin);
        getContentResolver().update(LockEntry.CONTENT_URI, values, null, null);
        finish();
    }

    private void getcursor() {
        String[] projection = {
                LockEntry._ID,
                LockEntry.COLUMN_PASS
        };
        cursor = getContentResolver().query(LockEntry.CONTENT_URI, projection, null, null, null);
    }
}
