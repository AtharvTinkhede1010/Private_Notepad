package com.atharv.notepad.LockScreen;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.atharv.notepad.LockScreen.LockContract.LockEntry;
import com.atharv.notepad.R;
import com.atharv.notepad.container;
import com.atharv.notepad.setting.Setting_Contract.SettingEntry;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LockActivity extends AppCompatActivity {

    public static final String TAG = "PinLockView";

    private PinLockView mPinLockView;
    private TextView Locktitle;
    private IndicatorDots mIndicatorDots;
    private String pin1;
    private Cursor cursorPin, cursorSetting;
    private RelativeLayout R1;
    private ImageView Img;


    private PinLockListener mPinLockListener = new PinLockListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onComplete(String pin) {

            if (cursorPin.moveToFirst()) {
                if (pin.equals(cursorPin.getString(cursorPin.getColumnIndex(LockEntry.COLUMN_PASS)))) {
                    R1.startAnimation(AnimationUtils.loadAnimation(LockActivity.this, R.anim.remove));
                    Intent intent = new Intent(LockActivity.this, container.class);
                    intent.putExtra("code", pin);
                    startActivity(intent);
                    finish();
                } else {
                    wrongpass();
                }
            } else {
                if (pin1 == null) {
                    mIndicatorDots.startAnimation(AnimationUtils.loadAnimation(LockActivity.this, R.anim.remove));
                    mPinLockView.startAnimation(AnimationUtils.loadAnimation(LockActivity.this, R.anim.remove));
                    pin1 = pin;
                    mPinLockView.resetPinLockView();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIndicatorDots.startAnimation(AnimationUtils.loadAnimation(LockActivity.this, R.anim.add));
                            mPinLockView.startAnimation(AnimationUtils.loadAnimation(LockActivity.this, R.anim.add));
                            Locktitle.setText("CONFIRM MPIN");
                        }
                    }, 500);

                } else if (pin1.equals(pin)) {
                    R1.startAnimation(AnimationUtils.loadAnimation(LockActivity.this, R.anim.remove));
                    save(pin);
                } else {
                    pin1 = null;
                    wrongpass();
                    Locktitle.setText("CREATE MPIN");
                }
            }

        }

        private void wrongpass() {
            mPinLockView.resetPinLockView();
            mIndicatorDots.startAnimation(AnimationUtils.loadAnimation(LockActivity.this, R.anim.shake));
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


    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
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
        Img = (ImageView) findViewById(R.id.profile_image);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();

        mPinLockView.setPinLength(4);
        Uri fbf = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.ic_baseline_person_24)
                + '/' + getResources().getResourceTypeName(R.drawable.ic_baseline_person_24) + '/' + getResources().getResourceEntryName(R.drawable.ic_baseline_person_24));

        getcursor();
        setSetting();

        //mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);


//Finger print system this code in onCreate method
//Create a thread pool with a single thread//
        Executor newExecutor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;

//Start listening for authentication events//
        BiometricPrompt myBiometricPrompt = new BiometricPrompt(activity, newExecutor, new BiometricPrompt.AuthenticationCallback() {
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
                Intent intent = new Intent(LockActivity.this, container.class);
                startActivity(intent);
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

                .setTitle("Login to Notepad")
                .setSubtitle("Touch your finger on sensor to login")
                .setNegativeButtonText("Use Pin")
                .build();



        if (!cursorPin.moveToFirst()) {
            Locktitle.setText("CREATE MPIN");
        } else {
            Log.v("","Fingerprint:-"+cursorSetting.getColumnIndex(SettingEntry.COLUMN_THEME));
            if(cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_fINGERPRINT)).equals(SettingEntry.Finger_Enable))
                myBiometricPrompt.authenticate(promptInfo);
        }


    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private void save(String pin) {

        ContentValues values = new ContentValues();
        values.put(LockEntry.COLUMN_PASS, pin);
        getContentResolver().insert(LockEntry.CONTENT_URI, values);
        Intent intent = new Intent(LockActivity.this, container.class);
        startActivity(intent);
        finish();
    }

    private void getcursor() {
        String[] projection = {
                LockEntry._ID,
                LockEntry.COLUMN_PASS
        };
        cursorPin = getContentResolver().query(LockEntry.CONTENT_URI, projection, null, null, null);
        String[] projectionSetting = {
                SettingEntry.COLUMN_PROFILE,
                SettingEntry.COLUMN_fINGERPRINT,
                SettingEntry.COLUMN_THEME,
                SettingEntry.COLUMN_LOCK_TEXT_COLOR,
                SettingEntry.COLUMN_TEXTSIZE,
                SettingEntry.COLUMN_APP_THEME,
                SettingEntry.COLUMN_APP_COLOR
        };
        cursorSetting = getContentResolver().query(SettingEntry.CONTENT_URI, projectionSetting, null, null, null);
        cursorSetting.moveToFirst();
    }

    @SuppressLint("ResourceAsColor")
    private void setSetting() {

        if (!cursorSetting.moveToFirst()) {
            /*Resources res = getResources();
            Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.img_no_avatar, null);
            Picture=
            Bitmap bitmap;
            bitmap = Bitmap.createBitmap(drawable);
            theImage = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            getImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
*/
            ContentValues values = new ContentValues();


           /* Bitmap bmp = null;
            try {
                getDrawable(R.drawable.img_no_avatar);

                Uri selectedImage = Uri.parse("res:///" + R.drawable.img_no_avatar);
                //Uri selectedImage = Uri.parse("android.resource://com.atharv.notepad/drawable/"+R.drawable.img_no_avatar);
                //Uri selectedImage = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +"://" + this.getResources().getResourcePackageName(R.drawable.ic_baseline_person_24)+ '/' + this.getResources().getResourceTypeName(R.drawable.ic_baseline_person_24) + '/' + this.getResources().getResourceEntryName(R.drawable.ic_baseline_person_24) );
                Log.v("", "bmp:-" + bmp + " IMG url:-" + selectedImage);
                bmp = getBitmapFromUri(selectedImage);

// convert bitmap to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                byte[] imageInByte = stream.toByteArray();
                values.put(SettingEntry.COLUMN_PROFILE, imageInByte);
            } catch (IOException e) {
                //TODO Auto-generated catch block
                //https://acadgild.com/blog/save-retrieve-image-sqlite-database-android
                Log.v("", "No");
                e.printStackTrace();
            }*/

            values.put(SettingEntry.COLUMN_PROFILE, "");
            values.put(SettingEntry.COLUMN_fINGERPRINT,SettingEntry.Finger_Enable);
            values.put(SettingEntry.COLUMN_THEME, "-13619152");
            values.put(SettingEntry.COLUMN_APP_COLOR,"0");
            values.put(SettingEntry.COLUMN_APP_THEME,"0");
            values.put(SettingEntry.COLUMN_LOCK_TEXT_COLOR, SettingEntry.Lock_forground_White);
            values.put(SettingEntry.COLUMN_TEXTSIZE, SettingEntry.Textsize_Medium);
            getContentResolver().insert(SettingEntry.CONTENT_URI, values);

        } else {

            R1.setBackgroundColor(Integer.parseInt(cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_THEME))));
            byte[] a = cursorSetting.getBlob(cursorSetting.getColumnIndex(SettingEntry.COLUMN_PROFILE));
            Bitmap bitmap = BitmapFactory.decodeByteArray(a, 0, a.length);
            Log.v("","Image:-"+a.length);
            if (a.length>=10)
                Img.setImageBitmap(bitmap);

            if (cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_LOCK_TEXT_COLOR)).equals(SettingEntry.Lock_forground_Black)) {
                Locktitle.setTextColor(R.color.black);
                mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.black));
            }


        }


    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        Log.v("", "parcelFileDescriptor:-" + parcelFileDescriptor);
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


}

