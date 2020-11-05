package com.cbb.seeandroid.progress;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;
import android.view.View;

/**
 * @author chenbinbin
 * 创建日期：2020/11/04 16:30
 * 描述：进度条 如果单独使用  需要在外面包裹一层父布局来固定位置
 */
class ProgressBarView4 extends View {
    // progressbar 宽度
    private int mWidth = 0;
    // progressbar 高度
    private int mHeight = 0;
    // 渐变
    private int startColor = 0;
    private int endColor = 0;
    // 背景色
    private int mBgPaintColor = 0;
    // 背景画笔
    private Paint mBgPaint;
    // 实际进度画笔
    private Paint curProgressPaint;
    // progressbar 背景
    private RectF progressBgRect;
    // 进度条圆角
    private float progressRadius;
    // 实际移动了的进度
    private int curProgress;
    // 外部设置进来的进度
    private int totalProgress;
    // 最大/最小
    private int max = 100;
    private int min = 0;
    // progressbar 的被等分的宽度
    private float progressStepWidth;
    // 实际进度的区域
    private RectF curProgressRect;
    private static final int TIME = 50;
    // 是否第一次
    private boolean isInit = true;
    // 动画
    private ObjectAnimator objectAnimator;
    // 已经移动的距离
    private float moved = 0f;

    public ProgressBarView4(Context context, int w, int h, int startColor, int endColor, int bgColor) {
        super(context);
        this.mWidth = w;
        this.mHeight = h;
        this.startColor = startColor;
        this.endColor = endColor;
        this.mBgPaintColor = bgColor;
        progressRadius = mHeight / 2;
        initView();
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
        progressStepWidth = ((float) mWidth) / max;
    }

    // 返回设置进来的进度
    public int getProgress() {
        return totalProgress;
    }

    public void setProgress(int p) {
        if (p != 100 & p != 0 && p == totalProgress) {
            return;
        }
        if (p < min) {
            p = min;
        }
        if (p > max) {
            p = max;
        }
        totalProgress = p;

        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        //第一次直接绘制
        if (isInit) {
            curProgress = p;
            isInit = false;
            moved = curProgress * progressStepWidth;
            invalidate();
            return;
        }
        float cur = moved;
        float dest = p * progressStepWidth;
        objectAnimator = ObjectAnimator.ofFloat(this, "moved", cur, dest ).setDuration(TIME);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moved = Float.parseFloat(animation.getAnimatedValue().toString());
                curProgress = (int) ( moved * 100 / mWidth);
                invalidate();
            }
        });
        objectAnimator.start();
    }

    private void setMoved(float moved) {
        this.moved = moved;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        canvas.drawRoundRect(progressBgRect, progressRadius, progressRadius, mBgPaint);
        // 实际右边距离
        curProgressRect.right = moved;
        // 绘制进度
        canvas.drawRoundRect(curProgressRect, progressRadius, progressRadius, curProgressPaint);
        setMove(curProgress, curProgressRect.right);
    }

    private OnProgressListener onProgressListener;

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    private void setMove(int p, float left) {
        if (onProgressListener != null) {
            onProgressListener.move(p, left);
        }
    }

    // 返回当前进度 和 移动距离
    public interface OnProgressListener {
        void move(int p, float left);
    }
}