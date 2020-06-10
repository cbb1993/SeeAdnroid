package com.cbb.skin;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cbb.skin.utils.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by 坎坎.
 * Date: 2020/6/5
 * Time: 10:30
 * describe:
 */
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    HashMap<Activity, SkinLayoutFactory> mLayoutFactoryMap = new HashMap<>();
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Log.e("-------","--------"+activity.getClass().getSimpleName());
        /**
         * 更新状态栏
         */
        SkinThemeUtils.updateStatusBar(activity);

//        /**
//         *  字体
//         */
//        Typeface typeface = SkinThemeUtils.getSkinTypeface(activity);
//
        // 获得对应Activity的布局加载器
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        SkinLayoutFactory skinLayoutFactory = new SkinLayoutFactory(activity);

        // setFactory2 时会判断 mFactorySet的值  mFactorySet为true的时候会报异常
        try {
            Field mFactorySet = LayoutInflater.class.getDeclaredField("mFactorySet");
            mFactorySet.setAccessible(true);
            mFactorySet.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        layoutInflater.setFactory2(skinLayoutFactory);
        //注册观察者
        SkinManager.getInstance().addObserver(skinLayoutFactory);
        mLayoutFactoryMap.put(activity, skinLayoutFactory);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        //删除观察者
        SkinLayoutFactory skinLayoutFactory = mLayoutFactoryMap.remove(activity);
        SkinManager.getInstance().deleteObserver(skinLayoutFactory);
    }
}
