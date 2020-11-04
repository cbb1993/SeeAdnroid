package com.cbb.seeandroid.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cbb.seeandroid.R;
/** use
 <com.cbb.progressbar.ProgressBarGroup
 android:id="@+id/progress_view"
 android:layout_width="match_parent"
 android:layout_height="400dp"
 android:layout_centerVertical="true"
 app:progressBarHeight="10dp"
 app:progressBarWidth="300dp"
 app:textMarginBar="10dp"
 app:progressStartColor="@color/black"
 app:progressTextSize="12dp"
 app:progressEndColor="@color/white"/>
 * */
public class ProgressBarGroup extends RelativeLayout implements ProgressBarView.OnProgressListener {
    private ProgressBarView progressBarView;
    private TextView progressTextView;
    private StringBuilder stringBuilder;
    private int marginText = 10;
    private float progressTextViewLeft;
    private int progressBarHeight = 20;
    private int progressBarWidth = 800;
    private int startColor = Color.BLUE;
    private int endColor = Color.GREEN;
    private int progressBarBgColor = Color.GRAY;
    private int textBgRes;
    private int textSize = 14;
    private RelativeLayout textGroup;

    public ProgressBarGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
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
                    startColor = a.getResourceId(index, startColor);
                    break;
                case R.styleable.group_progressEndColor:
                    endColor = a.getResourceId(index, endColor);
                    break;
                case R.styleable.group_textMarginBar:
                    marginText = a.getDimensionPixelSize(index, marginText);
                    break;
                case R.styleable.group_progressTextSize:
                    textSize = a.getDimensionPixelSize(index, textSize);
                    break;
                case R.styleable.group_progressTextBg:
                    textBgRes = a.getResourceId(index, 0);
                    break;
                case R.styleable.group_progressBgColor:
                    progressBarBgColor = a.getResourceId(index, progressBarBgColor);
                    break;
            }
        }
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflateProgressBar();
        createTextView();
    }

    private void inflateProgressBar() {
        progressBarView = new ProgressBarView(getContext(),
                progressBarWidth,
                progressBarHeight,
                startColor,
                endColor,
                progressBarBgColor);
        progressBarView.setId(View.generateViewId());
        LayoutParams lp = new LayoutParams(progressBarWidth, progressBarHeight);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        lp.bottomMargin = 20;
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
        return progressBarView.getCurProgress();
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
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textGroup.addView(progressTextView, layoutParams);

        addView(textGroup, lp_1);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        progressTextViewLeft = getProgressBarViewLeft() - textGroup.getWidth();
    }

    @Override
    public void progress(int p) {
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder();
        }
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(p);
        stringBuilder.append("%");
        progressTextView.setText(stringBuilder);
        float w = progressTextViewLeft + p * progressBarView.getProgressStepWidth();
        textGroup.setTranslationX(w);
    }

}
