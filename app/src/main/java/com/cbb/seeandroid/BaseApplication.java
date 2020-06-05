package com.cbb.seeandroid;

import android.app.Application;

import com.cbb.skin.SkinManager;

/**
 * Created by 坎坎.
 * Date: 2020/6/5
 * Time: 13:13
 * describe:
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
