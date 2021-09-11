package com.atharv.notepad.setting;


import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.FragmentActivity;

import com.atharv.notepad.LockScreen.LockActivity;
import com.atharv.notepad.R;
import com.atharv.notepad.setting.Setting_Contract.SettingEntry;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
//import android.support.v7.app.ActionBarActivity;

//https://pillsfromtheweb.blogspot.com/2014/12/android-passare-da-listview-gridview.html
//https://stackoverflow.com/questions/19076463/passing-from-a-listview-to-a-gridview
public class Setting extends AppCompatActivity {


    private ImageView mimageview, profilepic;
    private View mColorView, mColorView2;
    private Bitmap bitmap;
    int pixel;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int fingerprint_dialog = 8;
    private Cursor cursorSetting;
    private String[] projectionSetting;
    private BiometricPrompt myBiometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    Dialog_box d1;
    Constant constant;
    SharedPreferences sharedPreferences, app_preferences;
    int appTheme = 0;
    int themeColor = 0;
    int appColor = 0;
    private SharedPreferences.Editor editor;
    private Methods methods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*//app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences=getSharedPreferences("Demo",MODE_MULTI_PROCESS);
        //appColor = app_preferences.getInt("color", 0);
        //appTheme = app_preferences.getInt("theme", 0);
        appColor=sharedPreferences.getInt("color",0);
        appTheme=sharedPreferences.getInt("theme",0);
*/
        projectionSetting = new String[]{
                SettingEntry.COLUMN_PROFILE,
                SettingEntry.COLUMN_THEME,
                SettingEntry.COLUMN_LOCK_TEXT_COLOR,
                SettingEntry.COLUMN_TEXTSIZE,
                SettingEntry.COLUMN_APP_THEME,
                SettingEntry.COLUMN_APP_COLOR
        };
        cursorSetting = getContentResolver().query(SettingEntry.CONTENT_URI, projectionSetting, null, null, null);
        cursorSetting.moveToFirst();
        //setTheme(cursor.getInt(cursor.getColumnIndex(SettingEntry.COLUMN_THEME)));


        appColor = Integer.parseInt(cursorSetting.getString(cursorSetting.getColumnIndex(SettingEntry.COLUMN_APP_COLOR)));
        appTheme = Integer.parseInt(cursorSetting.getString(cursorSetting
                .getColumnIndex(SettingEntry.COLUMN_APP_THEME)));

        themeColor = appColor;
        Constant.color = appColor;
        if (themeColor == 0) {
            setTheme(Constant.theme);
        } else if (appTheme == 0) {
            setTheme(Constant.theme);
        } else {
            setTheme(appTheme);
        }
        setContentView(R.layout.activity_setting);

        setTitle("Settings");
/*
            TypedValue typedValue = new TypedValue();
            TypedArray a = this.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorAccent });
            int color = a.getColor(0, 0);
            Log.v("","Accent color:-"+color+"  "+R.attr.colorAccent+"  ");
            a.recycle();
*/


        profilepic = findViewById(R.id.image);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(Setting.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Setting.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                        //profilepic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        byte[] a = cursorSetting.getBlob(cursorSetting.getColumnIndex(SettingEntry.COLUMN_PROFILE));
        Bitmap bitmap = BitmapFactory.decodeByteArray(a, 0, a.length);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getApplication().getResources(), bitmap);
        drawable.setCircular(true);
        if (a.length >= 10)
            profilepic.setImageBitmap(bitmap);


        //Create a thread pool with a single thread//
        Executor newExecutor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;

//Start listening for authentication events//

        myBiometricPrompt = new BiometricPrompt(activity, newExecutor, new BiometricPrompt.AuthenticationCallback() {
            @Override
//onAuthenticationError is called when a fatal error occurrs//
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                    Log.v("", "HW Not available");
                }
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                } else {
//Print a message to Logcat
                    Log.d("", "An unrecoverable error occurred");

                }
            }

//onAuthenticationSucceeded is called when a fingerprint is matched successfully//

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                ContentValues contentValues = new ContentValues();
                contentValues.put(SettingEntry.COLUMN_fINGERPRINT, SettingEntry.Finger_Enable);

                getContentResolver().update(Setting_Contract.SettingEntry.CONTENT_URI, contentValues, null, null);

            }

//onAuthenticationFailed is called when the fingerprint doesn’t match//

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
//Print a message to Logcat//
                Log.d("Setting.this", "Fingerprint not recognised");
            }
        });

//Create the BiometricPrompt instance//

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
//Add some text to the dialog//
                .setTitle("Title text goes here")
                .setSubtitle("Subtitle goes here")
                .setDescription("This is the description")
                .setNegativeButtonText("Cancel")
                .build();

    }

    public void theme(View view) {

        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences("Demo", MODE_MULTI_PROCESS);
        editor = sharedPreferences.edit();
        methods = new Methods();
        final ColorChooserDialog dialog = new ColorChooserDialog(Setting.this);
        dialog.setColorListener(new ColorListener() {
            @Override
            public void OnColorClick(View v, int color) {
                colorize();
                Constant.color = color;

                methods.setColorTheme();
                editor.putInt("color", color);
                editor.putInt("theme", Constant.theme);
                editor.apply();

                ContentValues values = new ContentValues();
                values.put(SettingEntry.COLUMN_APP_THEME, Constant.theme);
                values.put(SettingEntry.COLUMN_APP_COLOR, color);
                getContentResolver().update(SettingEntry.CONTENT_URI, values, null, null);


                Intent intent = new Intent(getApplicationContext(), LockActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        dialog.show();

    }

    private void colorize() {
        ShapeDrawable d = new ShapeDrawable(new OvalShape());
        d.setBounds(58, 58, 58, 58);

        d.getPaint().setStyle(Paint.Style.FILL);
        d.getPaint().setColor(Constant.color);

        //button.setBackground(d);
    }


    public void password(View view) {
        Intent intent = new Intent(Setting.this, Change_password.class);
        startActivity(intent);
    }

/*

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Layout Selector");
        String[] items = {"ListView", "GridView"};
        int checkedItem;
        if (SettingEntry.List_view.equals(cursor.getString(cursor.getColumnIndex(SettingEntry.COLUMN_LAYOUT))))
            checkedItem = 0;
        else
            checkedItem = 1;
        setlayout(alertDialog, checkedItem, items);


        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    private void setlayout(AlertDialog.Builder alertDialog, int checkedItem, String[] items) {
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ContentValues values = new ContentValues();
                        values.put(SettingEntry.COLUMN_LAYOUT, SettingEntry.List_view);
                        getContentResolver().update(SettingEntry.CONTENT_URI, values, null, null);
                        Toast.makeText(Setting.this, "Clicked on ListView", Toast.LENGTH_LONG).show();
                        //finish();
                        break;
                    case 1:
                        ContentValues values2 = new ContentValues();
                        values2.put(SettingEntry.COLUMN_LAYOUT, SettingEntry.Grid_view);
                        getContentResolver().update(SettingEntry.CONTENT_URI, values2, null, null);
                        Toast.makeText(Setting.this, "Clicked on GridView", Toast.LENGTH_LONG).show();
                        //finish();
                        break;
                }
                dialog.dismiss();
            }F
        });
    }
*/


    public void textsize(View view) {
        Dialog_box D1 = new Dialog_box(this, getContentResolver());
        D1.Textsize();
    }

    public void aboutus(View view) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Developed by:-Atharv Tinkhede");
        dialog.show();
    }
/*
    private void showcolorpickerDialog() {
        //https://stackoverflow.com/questions/23669296/create-a-alertdialog-in-android-with-custom-xml-view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.colorpad);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        builder.show();
    }*/

    /*
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
            switch (requestCode) {
                case PICK_FROM_GALLERY:
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                    } else {
                        //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                    }
                    break;
            }
        }*/


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void Ltheme(View view) {
        this.setFinishOnTouchOutside(false);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        String[] item = {"Background", "Text Color"};
        dialog.setTitle("Lock Theme");

        final Dialog_box D1 = new Dialog_box(this, getContentResolver());
        dialog.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.v("", "DialogInterface:-" + dialogInterface + " , i:-" + i);
                if (i == 0) {
                    D1.LockBackground();
                } else if (i == 1) {
                    Intent intent = new Intent(Setting.this, foreground_text.class);
                    startActivity(intent);
                }
            }
        });
        dialog.show();

        /*
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.colorselector);
        dialog.setTitle("Title...");
        mimageview = (ImageView) dialog.findViewById(R.id.imageView);
        mColorView = dialog.findViewById(R.id.colorView);
        mColorView2 = dialog.findViewById(R.id.colorView2);
        mColorView.setBackgroundColor(cursor.getInt(cursor.getColumnIndex(SettingEntry.COLUMN_THEME)));
        Button button = (Button) dialog.findViewById(R.id.cancel);
        Button button2 = (Button) dialog.findViewById(R.id.apply);
        mimageview.setDrawingCacheEnabled(true);
        mimageview.buildDrawingCache(true);

        this.mimageview.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0 && motionEvent.getAction() != 2) return true;
                bitmap =mimageview.getDrawingCache();
                Log.v("", "event:-"+motionEvent.getX()+"   Y:- "+motionEvent.getY());
                try {

                    pixel = bitmap.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());
                    int n = Color.red((int) pixel);
                    int n2 = Color.green((int) pixel);
                    int n3 = Color.blue((int) pixel);
                    mColorView2.setBackgroundColor(Color.rgb((int) n, (int) n2, (int) n3));
                    Log.v((String) "", "RGB:"+n+","+n2+","+n3+"\nHEX:#"+Integer.toHexString(pixel)+"\nPixel:-"+pixel);
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

                projection = new String[]{"Profile_pic", "Theme_Color", "Layout", "Text_Size"};

                cursor = getContentResolver().query(Setting_Contract.SettingEntry.CONTENT_URI, projection, null, null, null);
                if (cursor.moveToFirst()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("Theme_Color", Integer.valueOf((int) pixel));

                    getContentResolver().update(Setting_Contract.SettingEntry.CONTENT_URI, contentValues, null, null);

                }
                setTheme(pixel);
                dialog.hide();
                return true;

            }
        });
        dialog.show();
*/

    }


    public void Fingerprint(View view) {
        Intent intent = new Intent(Setting.this, fingerprint.class);
        startActivityForResult(intent, fingerprint_dialog);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("", "Requestcode" + requestCode + " " + resultCode + " " + RESULT_OK);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
                Log.v("", "bmp:-" + bmp + " IMG url:-" + selectedImage);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 15, stream);
                byte[] imageInByte = stream.toByteArray();
                ContentValues values = new ContentValues();
                Log.v("", "||||" + imageInByte + "||||");
                values.put(SettingEntry.COLUMN_PROFILE, imageInByte);
                getContentResolver().update(SettingEntry.CONTENT_URI, values, null, null);
            } catch (IOException e) {
                //TODO Auto-generated catch block
                //https://acadgild.com/blog/save-retrieve-image-sqlite-database-android
                e.printStackTrace();
            }
            profilepic.setImageBitmap(bmp);
        } else if (requestCode == fingerprint_dialog && resultCode == -2) {
            d1 = new Dialog_box(Setting.this, getContentResolver());
            d1.fingerprint(myBiometricPrompt, promptInfo);
        }
    }
}