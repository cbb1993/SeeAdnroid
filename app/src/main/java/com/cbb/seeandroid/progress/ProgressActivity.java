package com.cbb.seeandroid.progress;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.cbb.seeandroid.R;

/**
 * @author chenbinbin
 * 创建日期：2020/11/5 11:02
 * 描述：
 */
public class ProgressActivity extends Activity {
    private ProgressBarGroup progress_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        progress_bar = findViewById(R.id.progress_bar);
    }

    public int getRound() {
        int i = (int) (Math.random() * 100);
        return i;
    }

    public void setProgress1(View view) {
        progress_bar.setProgress(progress_bar.getProgress() + getRound());
    }

    public void setProgress2(View view) {
        progress_bar.setProgress(progress_bar.getProgress() - getRound());
    }
}
