package com.atharv.notepad.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class fingerprint extends AppCompatActivity {

    private static final String TAG = fingerprint.class.getName();
    private String[] Lprojection, projectionSetting;
    private ImageView profilepic;
    private PinLockView mPinLockView;
    private TextView Locktitle;
    private IndicatorDots mIndicatorDots;
    private String pin1;
    private Cursor Lcursor,cursorSetting;
    private RelativeLayout R1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock);


        profilepic = (ImageView) findViewById(R.id.profile_image);






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

        getcursor();
        setbackground();
        Locktitle.setText("Current MPIN");
    }

    @SuppressLint("ResourceAsColor")
    private void setbackground() {

        R1.setBackgroundColor(Integer.parseInt(cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_THEME))));

        if (cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_LOCK_TEXT_COLOR)).equals(SettingEntry.Lock_forground_Black)) {
            Locktitle.setTextColor(R.color.black);
            mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
        byte[] a = cursorSetting.getBlob(cursorSetting.getColumnIndex(SettingEntry.COLUMN_PROFILE));
        Bitmap bitmap = BitmapFactory.decodeByteArray(a, 0, a.length);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getApplication().getResources(), bitmap);
        drawable.setCircular(true);
        if (a.length>=10)
            profilepic.setImageBitmap(bitmap);
        cursorSetting.close();
    }

    private void getcursor() {
        projectionSetting = new String[]{
                SettingEntry.COLUMN_PROFILE,
                SettingEntry.COLUMN_THEME,
                SettingEntry.COLUMN_LOCK_TEXT_COLOR,
                SettingEntry.COLUMN_TEXTSIZE
        };
        cursorSetting = getContentResolver().query(SettingEntry.CONTENT_URI, projectionSetting, null, null, null);
        cursorSetting.moveToFirst();

        Lprojection = new String[]{
                LockEntry._ID,
                LockEntry.COLUMN_PASS
        };
        Lcursor = getContentResolver().query(LockEntry.CONTENT_URI, Lprojection, null, null, null);
    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onComplete(String pin) {
            if (Lcursor.moveToFirst()) {
                if (pin.equals(Lcursor.getString(Lcursor.getColumnIndex(LockEntry.COLUMN_PASS)))) {
                    setResult(-2);
                    finish();
                } else {
                    wrongpass();
                }
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

    private void wrongpass() {
        mPinLockView.resetPinLockView();
        mIndicatorDots.startAnimation(AnimationUtils.loadAnimation(fingerprint.this, R.anim.shake));
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(50);
        }
    }
}

/*Finger print system this code in onCreate method
//Create a thread pool with a single thread//
        Executor newExecutor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;

//Start listening for authentication events//

        final BiometricPrompt myBiometricPrompt = new BiometricPrompt(activity, newExecutor, new BiometricPrompt.AuthenticationCallback() {
            @Override
//onAuthenticationError is called when a fatal error occurrs//
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                    Log.v("", "HW Not available");
                }
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                } else {
//Print a message to Logcat//
                    Log.d(TAG, "An unrecoverable error occurred");
                }
            }

//onAuthenticationSucceeded is called when a fingerprint is matched successfully//

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
//Print a message to Logcat//
                Log.d(TAG, "Fingerprint recognised successfully");
            }

//onAuthenticationFailed is called when the fingerprint doesn’t match//

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
//Print a message to Logcat//
                Log.d(TAG, "Fingerprint not recognised");
            }
        });

//Create the BiometricPrompt instance//

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
//Add some text to the dialog//
                .setTitle("Title text goes here")
                .setSubtitle("Subtitle goes here")
                .setDescription("This is the description")
                .setNegativeButtonText("Cancel")
                .build();

        findViewById(R.id.launchAuthentication).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBiometricPrompt.authenticate(promptInfo);
            }
        });

*/