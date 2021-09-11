package com.atharv.notepad.setting;

import android.util.Log;

import com.atharv.notepad.R;

/**
 * Created by delaroy on 3/21/17.
 */
public class Methods {

    public void setColorTheme(){
        Log.v("","Color:-"+Constant.color);
        switch (Constant.color){
            case 0xffF44336:
                Constant.theme = R.style.AppTheme_red;
                break;
            case 0xffE91E63:
                Constant.theme = R.style.AppTheme_pink;
                break;
            case 0xff9C27B0:
                Constant.theme = R.style.AppTheme_darpink;
                break;
            case 0xff673AB7:
                Constant.theme = R.style.AppTheme_violet;
                break;
            case 0xff3F51B5:
                Constant.theme = R.style.AppTheme_blue;
                break;
            case 0xff03A9F4:
                Constant.theme = R.style.AppTheme_skyblue;
                break;
            case 0xff4CAF50:
                Constant.theme = R.style.AppTheme_green;
                break;
            case 0xffFF9800:
                Constant.theme = R.style.AppTheme;
                break;
            case 0xff9E9E9E:
                Constant.theme = R.style.AppTheme_grey;
                break;
            case 0xff795548:
                Constant.theme = R.style.AppTheme_brown;
                break;
            case 0xff2196F3:
                Constant.theme = R.style.AppTheme_darkblue;
                break;
            case 0xff00BCD4:
                Constant.theme = R.style.AppTheme;
                break;
            case 0xff009688:
                Constant.theme = R.style.AppTheme_teal;
                break;
            case 0xff8BC34A:
                Constant.theme = R.style.AppTheme_lightgreen;
                break;
            case 0xffCDDC39:
                Constant.theme = R.style.AppTheme_lime;
                break;
            case 0xffFFEB3B:
                Constant.theme = R.style.AppTheme_yellow;
                break;
            case 0xffFFC107:
                Constant.theme = R.style.AppTheme_amber;
                break;
            case 0xffFF5722:
                Constant.theme = R.style.AppTheme_deeporange;
                break;
            case 0xff607D8B:
                Constant.theme = R.style.AppTheme_bluegray;
                break;
            case 0xff000000:
                Constant.theme = R.style.AppTheme_black;
                break;
            case 0xffffffff:
                Constant.theme = R.style.AppTheme_white;
                break;
            default:
                Constant.theme = R.style.AppTheme;
                break;
        }
    }
}
/*

    public final int DeepPurple = 0xff673AB7;
    public final int Indigo =     0xff3F51B5;
    public final int Blue =       0xff2196F3;
    public final int LightBlue =  0xff03A9F4;
    public final int Cyan =       0xff00BCD4;
    public final int Teal =       0xff009688;
    public final int Green =      0xff4CAF50;
    public final int LightGreen = 0xff8BC34A;
    public final int Lime =       0xffCDDC39;
    public final int Yellow =     0xffFFEB3B;
    public final int Amber =      0xffFFC107;
    public final int Orange =     0xffFF9800;
    public final int DeepOrange = 0xffFF5722;
    public final int Brown =      0xff795548;
    public final int Grey =       0xff9E9E9E;
    public final int BlueGray =   0xff607D8B;
    public final int Black =      0xff000000;
    public final int White =      0xffffffff;
*/
