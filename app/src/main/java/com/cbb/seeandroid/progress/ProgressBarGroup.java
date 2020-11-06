package com.cbb.seeandroid.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cbb.seeandroid.R;

/**
 * @author chenbinbin
 * 创建日期：2020/11/04 16:30
 * 描述：viewGroup 内部有上放进度文字 和 下方进度条
 */

public class ProgressBarGroup extends RelativeLayout implements ProgressBarView.OnProgressListener {
    // 进度条
    private ProgressBarView progressBarView;
    // 进度文字
    private TextView progressTextView;
    private StringBuilder stringBuilder;
    // 进度条和文字之间的距离
    private int marginText;
    // 文字距离左边界初始距离
    private float progressTextViewLeft;
    // 进度条宽高
    private int progressBarHeight;
    private int progressBarWidth;
    // 进度条渐变色
    private int startColor;
    private int endColor;
    // 进度条底部颜色
    private int progressBarBgColor = Color.GRAY;
    // 进度条文字背景
    private int textBgRes;
    // 进度条文字大小
    private int textSize;
    // 进度条view外部view
    private RelativeLayout textGroup;
    // 整个view实际需要展示的高度
    private int viewHeight;
    // 父布局 做超出裁剪处理
    private ViewGroup parent;

    public ProgressBarGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 默认初始化
        marginText = dimen2px(R.dimen.download_dialog_progress_text_margin);
        progressBarHeight = dimen2px(R.dimen.download_dialog_progress_h);
        progressBarWidth = dimen2px(R.dimen.download_dialog_progress_w);
        textSize = dimen2px(R.dimen.download_dialog_progress_text_size);
        textSize = px2sp(getContext(), textSize);
        textBgRes = R.mipmap.progress_bar;
        startColor = getResources().getColor(R.color.progress_start);
        endColor = getResources().getColor(R.color.progress_end);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.group);
        int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.group_progressBarWidth:
                    progressBarWidth = a.getDimensionPixelSize(index, progressBarWidth);
                    break;
                case R.styleable.group_progressBarHeight:
                    progressBarHeight = a.getDimensionPixelSize(index, progressBarHeight);
                    break;
                case R.styleable.group_progressStartColor:
                    startColor = a.getColor(index, startColor);
                    break;
                case R.styleable.group_progressEndColor:
                    endColor = a.getColor(index, endColor);
                    break;
                case R.styleable.group_textMarginBar:
                    marginText = a.getDimensionPixelSize(index, marginText);
                    break;
                case R.styleable.group_progressTextSize:
                    float f = a.getDimension(index, textSize);
                    textSize = px2sp(getContext(), f);
                    break;
                case R.styleable.group_progressTextBg:
                    textBgRes = a.getResourceId(index, textBgRes);
                    break;
                case R.styleable.group_progressBgColor:
                    progressBarBgColor = a.getColor(index, progressBarBgColor);
                    break;
            }
        }
        a.recycle();
        // 计算view实际高度
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), textBgRes);
        viewHeight = marginText + progressBarHeight + bitmap.getHeight();
        bitmap.recycle();
    }

    private int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    private int dimen2px(int dimen) {
        return getResources().getDimensionPixelSize(dimen);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 初始化进度条
        createProgressBar();
        // 初始化文字
        createTextView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(parent == null){
            parent = (ViewGroup) getParent();
            if(parent!=null){
                parent.setClipChildren(false);
            }
        }
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int i = MeasureSpec.makeMeasureSpec(viewHeight, mode);
        super.onMeasure(widthMeasureSpec, i);
    }

    private void createProgressBar() {
        progressBarView = new ProgressBarView(getContext(),
                progressBarWidth,
                progressBarHeight,
                startColor,
                endColor,
                progressBarBgColor);

        progressBarView.setId(View.generateViewId());
        LayoutParams lp = new LayoutParams(progressBarWidth, progressBarHeight);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(progressBarView, lp);
        progressBarView.setOnProgressListener(this);
    }

    private float getProgressBarViewLeft() {
        return progressBarView.getLeft();
    }

    public void setProgress(int progress) {
        progressBarView.setProgress(progress);
    }

    public int getProgress() {
        return progressBarView.getProgress();
    }

    private void createTextView() {
        // 创建父布局
        textGroup = new RelativeLayout(getContext());
        LayoutParams lp_1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp_1.bottomMargin = marginText;
        lp_1.addRule(RelativeLayout.ABOVE, progressBarView.getId());

        // 创建背景image
        ImageView imageView = new ImageView(getContext());
        LayoutParams lp_2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (textBgRes != 0) {
            imageView.setImageResource(textBgRes);
        }
        textGroup.addView(imageView, lp_2);
        //文字
        progressTextView = new TextView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        progressTextView.setTextSize(textSize);
        progressTextView.setTextColor(Color.WHITE);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        textGroup.addView(progressTextView, layoutParams);
        addView(textGroup, lp_1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        progressTextViewLeft = getProgressBarViewLeft() - textGroup.getWidth();
    }

    @Override
    public void move(int p, float left) {
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder();
        }
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(p);
        stringBuilder.append("%");
        progressTextView.setText(stringBuilder);
        textGroup.setTranslationX(progressTextViewLeft + left);
        if (p == 0 || p == 100) {
            textGroup.setVisibility(GONE);
        } else {
            textGroup.setVisibility(VISIBLE);
        }
    }

}
