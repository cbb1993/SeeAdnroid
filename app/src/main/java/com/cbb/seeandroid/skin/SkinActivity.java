package com.cbb.seeandroid.skin;

import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cbb.seeandroid.R;
import com.cbb.skin.SkinManager;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by 坎坎.
 * Date: 2020/6/5
 * Time: 10:18
 * describe:
 */
public class SkinActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
    }

    public void update(View view) {
        File file =new File(Environment.getExternalStorageDirectory(),"app-skin.skin");
        SkinManager.getInstance().loadSkin(file.getPath());
    }

    public void reset(View view) {
        SkinManager.getInstance().loadSkin("");
    }
}
