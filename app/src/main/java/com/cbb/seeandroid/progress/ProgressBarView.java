package com.cbb.seeandroid.progress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

public class ProgressBarView extends View {

    public ProgressBarView(Context context, int w, int h, int startColor, int endColor,int bgColor) {
        super(context);
        this.mWidth = w;
        this.mHeight = h;
        this.startColor = startColor;
        this.endColor = endColor;
        this.mBgPaintColor = bgColor;
        initView();
    }

    private int mWidth  ;
    private int mHeight;
    private int startColor;
    private int endColor;
    private int mBgPaintColor;

    private Paint mBgPaint;
    private Paint curProgressPaint;
    // progressbar 背景
    private RectF progressBgRect;
    // progressbar 高度
    private float progressH = 20;
    // 进度条圆角
    private float progressRadius = progressH / 2;
    // 当前进度
    private int curProgress;
    // 外部设置进来的进度
    private int totalProgress;
    // 需要位移的长度
    private int moveLength;
    // 最大/最小
    private int max = 100;
    private int min = 0;
    // progressbar 的被等分的宽度
    private int progressStepWidth;
    // 每次重绘间隔的时间
    private int moveTime = 20;
    // 实际进度的区域
    private RectF curProgressRect;

    public int getCurProgress() {
        return totalProgress;
    }
    public int getProgressStepWidth() {
        return progressStepWidth;
    }
    private void initView() {
        mBgPaint = new Paint();
        mBgPaint.setColor(mBgPaintColor);
        mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setDither(true);

        curProgressPaint = new Paint();
        curProgressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        curProgressPaint.setAntiAlias(true);
        curProgressPaint.setDither(true);
        Shader shader = new LinearGradient(0, 0, mWidth, mHeight, new int[]{startColor, endColor}, null, Shader.TileMode.REPEAT);
        curProgressPaint.setShader(shader);

        progressBgRect = new RectF(0, 0, mWidth, mHeight);
        curProgressRect = new RectF(0, 0, 0, mHeight);
        progressStepWidth = mWidth / (max - min);
    }

    public void setProgress(int p) {
        if (p < min) {
            p = min;
        }
        if (p > max) {
            p = max;
        }
        // 移除之前的动画
        handler.removeMessages(1);
        // 算出这次位移大小
        moveLength = p - curProgress;
        // 记录实际设置的进度
        totalProgress = p;
        if (moveLength != 0) {
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        canvas.drawRoundRect(progressBgRect, progressRadius, progressRadius, mBgPaint);
        // 计算位移
        if (moveLength < 0) {
            moveLength++;
            curProgress--;
        } else if (moveLength > 0) {
            moveLength--;
            curProgress++;
        }
        onProgressListener(curProgress);
        curProgressRect.right = curProgress * progressStepWidth;
        // 实际进度
        canvas.drawRoundRect(curProgressRect, progressRadius, progressRadius, curProgressPaint);
        // 继续重绘
        if (moveLength != 0) {
            handler.sendEmptyMessageDelayed(1, moveTime);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };

    private OnProgressListener onProgressListener;

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    private void onProgressListener(int progress) {
        if (onProgressListener != null) {
            onProgressListener.progress(progress);
        }
    }

    public interface OnProgressListener {
        void progress(int p);
    }
}
