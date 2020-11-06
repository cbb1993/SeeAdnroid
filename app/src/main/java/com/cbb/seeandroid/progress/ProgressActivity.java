package com.cbb.seeandroid.progress;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
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
        int i = (int) (Math.random() * 30);
        return i;
    }

    private int p;

    public void setProgress1(View view) {
        handler.removeCallbacksAndMessages(null);
       handler.sendEmptyMessage(1);
    }

    public void setProgress2(View view) {
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessage(2);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                        progress_bar.setProgress(progress_bar.getProgress() + getRound());
//                        sendEmptyMessageDelayed(1,200);
                    break;
                case 2:
                        progress_bar.setProgress(progress_bar.getProgress() - getRound());
//                        sendEmptyMessageDelayed(2,200);
                    break;
            }
        }
    };
}
