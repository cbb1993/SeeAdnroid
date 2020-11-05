package com.cbb.seeandroid.progress;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
    // 实际移动了的进度
    private float curProgress;
    // 外部设置进来的进度
    private int totalProgress;
    // 需要位移的长度
    private float moveLength;
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
    private static final float COUNT_2 = 4;
    private static final float COUNT_1_2 = 0.8f;
    private static final float COUNT_1_4 = 0.5f;
    private float canvasMax;

    // 左移还是右移
    private boolean isMoveLeft = false;
    // 是否是第一次传入
    private boolean isInit = true;

    // 给设置的分级
    // 给一次变化的progress分级 采取不同的绘制速度
    // 正常按100段来算
    enum Type {
        progress1to2, // 将每一小段再分为COUNT次绘制
        progress3to10, // 正常绘制每个小段
        progress10to50, // 一次绘制2小段
        progress50to100 // 一次绘制4小段
    }

    private Type type = Type.progress3to10;

    public ProgressBarView(Context context, int w, int h, int startColor, int endColor, int bgColor) {
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
    }

    // 返回设置进来的进度
    public int getProgress() {
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
        if (isInit) {
            // 第一次 直接绘制到传入的地方
            type = Type.progress3to10;
            canvasMax = max;
            moveLength = 1;
            curProgress = p - 1;
        }else {
            // 实际已经移动到什么地方了
            curProgress = getCurProgress();
            // 左移还是右移
            isMoveLeft = p - curProgress < 0;
            // 这次传入和上次实际移动的相差多少
            float absP = Math.abs(p - curProgress);
            if (absP < 3) {
                type = Type.progress1to2;
                canvasMax = max * COUNT_2;
                moveLength = absP * COUNT_2;
                curProgress = curProgress * COUNT_2;
            } else if (absP < 10) {
                type = Type.progress3to10;
                canvasMax = max;
                moveLength = absP;
                curProgress = curProgress;
            } else if (absP < 50) {
                type = Type.progress10to50;
                canvasMax = max * COUNT_1_2;
                moveLength = absP * COUNT_1_2;
                curProgress = curProgress * COUNT_1_2;
            } else {
                type = Type.progress50to100;
                canvasMax = max * COUNT_1_4;
                moveLength = absP * COUNT_1_4;
                curProgress = curProgress * COUNT_1_4;
            }
            if (isMoveLeft) {
                moveLength = -moveLength;
            }
        }
        progressStepWidth = mWidth / (canvasMax - min);
        // 保存上次实际传入的进度
        totalProgress = p;
        // 记录实际设置的进度
        if (moveLength != 0) {
            handler.sendEmptyMessage(1);
        }
        isInit = false;

    }

    private float getCurProgress() {
        switch (type) {
            case progress1to2:
                return curProgress / COUNT_2;
            case progress3to10:
                return curProgress;
            case progress10to50:
                return curProgress / COUNT_1_2;
            case progress50to100:
                return curProgress / COUNT_1_4;
        }
        return curProgress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        canvas.drawRoundRect(progressBgRect, progressRadius, progressRadius, mBgPaint);
        // 移动可能会有小数遗留
        if (moveLength != 0) {
            if (Math.abs(moveLength) < 1) {
                if (moveLength < 0) {
                    curProgress -= moveLength;
                } else if (moveLength > 0) {
                    curProgress += moveLength;
                }
                moveLength = 0;
            } else {
                // 计算位移 每次移动一位
                if (moveLength < 0) {
                    moveLength++;
                    curProgress--;
                } else if (moveLength > 0) {
                    moveLength--;
                    curProgress++;
                }
            }
        }

        // 实际右边距离
        curProgressRect.right = progressStepWidth * curProgress;
        // 绘制进度
        canvas.drawRoundRect(curProgressRect, progressRadius, progressRadius, curProgressPaint);
        // 设置回调
        switch (type) {
            case progress1to2:
                setMove((int) (curProgress / COUNT_2), curProgressRect.right);
                break;
            case progress3to10:
                setMove((int) curProgress, curProgressRect.right);
                break;
            case progress10to50:
                setMove((int) (curProgress / COUNT_1_2), curProgressRect.right);
                break;
            case progress50to100:
                setMove((int) (curProgress / COUNT_1_4), curProgressRect.right);
                break;
        }

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
