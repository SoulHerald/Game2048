package com.example.game2048;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDelegate;

public class StatusBar {
    Activity activity;
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor editor;

    public StatusBar(Activity activity) {
        this.activity = activity;
    }

    public void setWhiteStatusBar() {
        activity.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | 0x00002000);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    // An highlighted block

    /**
     * bDark   true   黑色     false   白色
     */
    public void setDarkStatusWhite(boolean bDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (bDark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }

    public void DarkAndLight() {

//        深色模式的值为:0x21
//        浅色模式的值为:0x11
        mySharedPreferences = activity.getSharedPreferences("setting", 0);
        editor = mySharedPreferences.edit();
        if (mySharedPreferences.getString("dark_follow_system", null) == null &&
                mySharedPreferences.getString("dark_no", null) == null &&
                mySharedPreferences.getString("dark_yes", null) == null) {

            editor.putString("dark_no", "0");
            editor.putString("dark_yes", "0");
            editor.putString("dark_follow_system", "1");
            editor.apply();
        }

        if (mySharedPreferences.getString("dark_follow_system", null).equals("1")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            if (activity.getResources().getConfiguration().uiMode == 0x21) {
                new StatusBar(activity).setDarkStatusWhite(false);
            } else if (activity.getResources().getConfiguration().uiMode == 0x11) {
                new StatusBar(activity).setDarkStatusWhite(true);
            }
        } else if (mySharedPreferences.getString("dark_yes", null).equals("1")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            new StatusBar(activity).setDarkStatusWhite(false);
        } else if (mySharedPreferences.getString("dark_no", null).equals("1")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            new StatusBar(activity).setDarkStatusWhite(true);
        }
    }

    public void initBar() {
        activity.getWindow().requestFeature(Window.FEATURE_NO_TITLE); //取消状态栏的标题
        //判断SDK的版本是否>=21
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); //允许页面可以拉伸到顶部状态栏并且定义顶部状态栏透名
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |  //设置全屏显示
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT); //设置状态栏为透明
        window.setNavigationBarColor(Color.TRANSPARENT); //设置虚拟键为透明

    }
}
