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

/**
 * @author chenbinbin
 * 创建日期：2020/11/04 16:30
 * 描述：进度条 如果单独使用  需要在外面包裹一层父布局来固定位置
 */
class ProgressBarView extends View {
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
    private float progressStepWidth;
    // 每次重绘间隔的时间
    private int moveTime = 10;
    // 实际进度的区域
    private RectF curProgressRect;
    // 将实际等分 分为原来的5倍 这样绘制显示比较圆滑
    private static final int COUNT = 5;
    private int canvasMax = max * COUNT;
    // 两次进度相差距离 ，如果差距过大  就一次性绘制完成 防止分段绘制导致绘制过慢
    private int maxCurAndLast = 5;

    public ProgressBarView(Context context, int w, int h, int startColor, int endColor, int bgColor) {
        super(context);
        this.mWidth = w;
        this.mHeight = h;
        this.startColor = startColor;
        this.endColor = endColor;
        this.mBgPaintColor = bgColor;
        progressRadius  = mHeight / 2;
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
        progressStepWidth = ((float) mWidth) / (canvasMax - min);

    }

    // 返回设置进来的进度
    public int getCurProgress() {
        return totalProgress;
    }

    public void setProgress(int p) {
        if (p == totalProgress) {
            return;
        }
        if (p < min) {
            p = min;
        }
        if (p > max) {
            p = max;
        }
        handler.removeMessages(1);
        // 如果当前进度差距过大，直接跳过动画
        if (p - totalProgress >= maxCurAndLast) {
            // 移动一次 直接到对应位置
            moveLength = 1;
            curProgress = p * COUNT - 1;
        } else if (p - totalProgress <= -maxCurAndLast) {
            // 移动一次 直接到对应位置
            moveLength = -1;
            curProgress = p * COUNT + 1;
        } else {
            // 设置换算的进度
            int canvasP = p * COUNT;
            // 算出这次位移大小
            moveLength = canvasP - curProgress;
        }
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
        // 计算位移 每次移动一位
        if (moveLength < 0) {
            moveLength++;
            curProgress--;
        } else if (moveLength > 0) {
            moveLength--;
            curProgress++;
        }
        // 实际右边距离
        curProgressRect.right = progressStepWidth * curProgress;
        // 绘制进度
        canvas.drawRoundRect(curProgressRect, progressRadius, progressRadius, curProgressPaint);
        // 设置回调
        setMove(curProgress / COUNT, curProgressRect.right);
        // 继续重绘
        if (moveLength != 0) {
            handler.sendEmptyMessageDelayed(1, moveTime);
        }
    }
    // 定时重绘
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };

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
