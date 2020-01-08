package de.baumann.browser.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class BubbleSeekBarScrollView extends ScrollView {

    private OnScrollChangedListener mOnScrollChangedListener;

    public BubbleSeekBarScrollView(Context context) {
        super(context);
    }

    public BubbleSeekBarScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BubbleSeekBarScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.mOnScrollChangedListener = onScrollChangedListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false; // 此处为了演示，阻止ScrollView拦截Touch事件
    }

    interface OnScrollChangedListener {

        void onScrollChanged(BubbleSeekBarScrollView scrollView, int x, int y, int oldx, int oldy);

    }
}