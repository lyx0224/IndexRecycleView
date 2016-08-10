package com.ht.framework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ht.framework.widget.htrecyclerview.BaseRecyclerViewAdapter;
import com.ht.framework.widget.htrecyclerview.HTRecyclerView;

/**
 * Created by ethan on 2016/8/8.
 */
public class IndexHTRecycleView extends HTRecyclerView {

    private IndexScroller mScroller = null;

    public IndexHTRecycleView(Context context) {
        super(context);
    }

    public IndexHTRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexHTRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setAdapter(BaseRecyclerViewAdapter adapter) {
        super.setAdapter(adapter);
        if (mScroller == null) {
            mScroller = new IndexScroller(getContext(), getRecyclerView());
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mScroller != null) {
            mScroller.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mScroller != null && mScroller.onTouchEvent(ev))
            return true;
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mScroller.contains(ev.getX(), ev.getY()))
            return true;

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mScroller != null)
            mScroller.onSizeChanged(w, h, oldw, oldh);
    }
}
