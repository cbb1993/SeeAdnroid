package com.cbb.skin;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.TextView;

import com.cbb.skin.utils.SkinPreference;
import com.cbb.skin.utils.SkinResources;

import java.lang.reflect.Method;
import java.util.Observable;

/**
 * Created by 坎坎.
 * Date: 2020/6/5
 * Time: 10:27
 * describe:
 */
public class SkinManager extends Observable {
    private static SkinManager instance;
    private Application application;


    public static void init(Application application) {
        synchronized (SkinManager.class) {
            if (null == instance) {
                instance = new SkinManager(application);
            }
        }
    }

    public static SkinManager getInstance() {
        return instance;
    }

    private SkinManager(Application application) {
        this.application = application;
        SkinPreference.init(application);
        SkinResources.init(application);

        // 对所有activity生命周期监听
        application.registerActivityLifecycleCallbacks(new ActivityLifecycle());

        loadSkin(SkinPreference.getInstance().getSkin());

    }
 // 加载资源
    public void loadSkin(String path) {
        if(TextUtils.isEmpty(path)){
            // 还原默认
            SkinPreference.getInstance().setSkin("");
            SkinResources.getInstance().reset();
        }else {
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                // 添加资源进入资源管理器
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String
                        .class);
                addAssetPath.setAccessible(true);
                // 添加
                addAssetPath.invoke(assetManager, path);

                // 配置到工具类 方便后期获取各种属性值
                // 实例Resources
                Resources resources = application.getResources();
                Resources skinResource = new Resources(assetManager,resources.getDisplayMetrics(),resources.getConfiguration());
                // 获得外部的皮肤包包名
                PackageManager packageManager = application.getPackageManager();
                PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                String packageName=packageArchiveInfo.packageName;
                SkinResources.getInstance().applySkin(skinResource,packageName);

                //保存当前使用的皮肤包
                SkinPreference.getInstance().setSkin(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //应用皮肤包
        setChanged();
        //通知观察者
        notifyObservers();

    }

}
