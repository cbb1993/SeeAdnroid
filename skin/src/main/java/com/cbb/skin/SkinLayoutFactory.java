package com.cbb.skin;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by 坎坎.
 * Date: 2020/6/5
 * Time: 10:32
 * describe:
 */
public class SkinLayoutFactory implements LayoutInflater.Factory2 , Observer {
    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    // view构造器缓存
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();

    // 对应view 两个参数的构造方法
    private static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    // 属性处理类
    SkinAttribute skinAttribute;

    public SkinLayoutFactory() {
        skinAttribute = new SkinAttribute();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = createViewFromTag(name, context, attrs);
        // 自定义view
        if(view == null){
            view = createView(name,context,attrs);
        }
        //筛选符合属性的View
        skinAttribute.load(view, attrs);
        return view;
    }

    private View createViewFromTag(String name, Context context, AttributeSet attrs) {
        // 自定义控件
        if (name.contains(".")) {
            return null;
        }
        View view = null;
        // 遍历 拼接 packageName + viewName  通过反射拿到实例
        for (int i = 0; i < mClassPrefixList.length; i++) {
            // 拿到实例
            view = createView(mClassPrefixList[i] + name, context, attrs);
            // view可能为空 比如view不在这个包下  不为空时表示已经找到
            if (null != view) {
                break;
            }
        }
        return view;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        // 获得缓存的构造器  避免同一类型view多次反射
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (null == constructor) {
            // 之前没有通过反射获得构造器
            Class<? extends View> aClass = null;
            try {
                aClass = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = aClass.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(name,constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        View view = null;
        if (null != constructor) {
            try {
                view = constructor.newInstance(context, attrs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        skinAttribute.applySkin();
    }
}
