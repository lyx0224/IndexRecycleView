/*
 * Copyright 2011 woozzu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ht.framework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.widget.SectionIndexer;

public class IndexScroller {

    private float mIndexbarWidth;
    private float mIndexbarMargin;
    private float mDensity;
    private float mScaledDensity;
    private int mCurrentSection = -1;
    private RecyclerView mListView = null;
    private SectionIndexer mIndexer = null;
    private String[] mSections = null;
    private RectF mIndexbarRect;
    private Paint mTextPaint;
    private int itemLetterPaddingTb;
    private int itemLetterHeight;

    public IndexScroller(Context context, RecyclerView lv) {
        mDensity = context.getResources().getDisplayMetrics().density;
        mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        mListView = lv;
        setAdapter(mListView.getAdapter());

        mIndexbarWidth = 20 * mDensity;
        mIndexbarMargin = 10 * mDensity;

        itemLetterPaddingTb = (int) (7 * mDensity);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(12 * mScaledDensity);

        itemLetterHeight = getTextHeight("A", mTextPaint);
    }

    public void draw(Canvas canvas) {
        if (mSections != null && mSections.length > 0) {
//			float sectionHeight = (mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length;
//			float paddingTop = (sectionHeight - (mTextPaint.descent() - mTextPaint.ascent())) / 2;
//			for (int i = 0; i < mSections.length; i++) {
//				float paddingLeft = (mIndexbarWidth - mTextPaint.measureText(mSections[i])) / 2;
//				canvas.drawText(mSections[i], mIndexbarRect.left + paddingLeft
//						, mIndexbarRect.top + mIndexbarMargin + sectionHeight * i + paddingTop - mTextPaint.ascent(), mTextPaint);
//			}
            for (int i = 0; i < mSections.length; i++) {
                float paddingLeft = (mIndexbarWidth - mTextPaint.measureText(mSections[i])) / 2;
                float y = mIndexbarRect.top + (i * (itemLetterHeight + itemLetterPaddingTb)) + itemLetterPaddingTb / 2;
                canvas.drawText(mSections[i], mIndexbarRect.left + paddingLeft, y, mTextPaint);
            }
        }
    }

    private void scrollTo(int pos) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mListView.getLayoutManager();
        linearLayoutManager.scrollToPositionWithOffset(pos, 0);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // If down event occurs inside index bar region, start indexing
                if (contains(ev.getX(), ev.getY())) {
                    // It demonstrates that the motion event started from index bar
                    // Determine which section the point is in, and move the list to that section
                    mCurrentSection = getSectionByPoint(ev.getY());
                    scrollTo(mIndexer.getPositionForSection(mCurrentSection));
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // If this event moves inside index bar
                if (contains(ev.getX(), ev.getY())) {
                    // Determine which section the point is in, and move the list to that section
                    mCurrentSection = getSectionByPoint(ev.getY());
                    scrollTo(mIndexer.getPositionForSection(mCurrentSection));
                }
                return true;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        int top = (h - (itemLetterHeight + itemLetterPaddingTb) * mSections.length) / 2;
        mIndexbarRect = new RectF(w - mIndexbarMargin - mIndexbarWidth, top, w - mIndexbarMargin, h - top);
    }

    private void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter instanceof SectionIndexer) {
            mIndexer = (SectionIndexer) adapter;
            mSections = (String[]) mIndexer.getSections();
        }
    }


    public boolean contains(float x, float y) {
        return (x >= mIndexbarRect.left && y >= mIndexbarRect.top && y <= mIndexbarRect.top + mIndexbarRect.height());
    }

    private int getSectionByPoint(float y) {
        if (mSections == null || mSections.length == 0){
            return  0;
        }

        if (y - mIndexbarRect.top > 0){
            return (int) Math.floor((y - mIndexbarRect.top) / (itemLetterHeight + itemLetterPaddingTb));
        }
        return 0;
    }

    public static int getTextHeight(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.bottom + bounds.height();
        return height;
    }


}
