package com.cbb.seeandroid.aop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cbb.seeandroid.R;

/**
 * Created by 坎坎.
 * Date: 2020/5/24
 * Time: 20:02
 * describe: 测试Aop的类
 */
public class AopTest1Activity extends AppCompatActivity {
    public  static final String TAG = AopTest1Activity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aop_test);
    }

    public void start(View view) {
        aopTest();
    }
    @LogTrace
    public void aopTest(){
        Log.e(TAG,"-->aopTest()");
    }
}
