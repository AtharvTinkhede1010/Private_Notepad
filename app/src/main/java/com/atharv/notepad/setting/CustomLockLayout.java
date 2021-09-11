package com.atharv.notepad.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class CustomLockLayout extends LinearLayout {

    public CustomLockLayout(Context context) {
        super(context);
    }

    public CustomLockLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLockLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomLockLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureheight=MeasureSpec.getSize(widthMeasureSpec)*17/9;
        int measureheightspec=MeasureSpec.makeMeasureSpec(measureheight,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, measureheightspec);
    }

}
