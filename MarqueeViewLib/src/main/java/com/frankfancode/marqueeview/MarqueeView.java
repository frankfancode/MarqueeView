package com.frankfancode.marqueeview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by fxd on 2016/11/17.
 */

public class MarqueeView extends LinearLayout {
    private Context mContext;
    private RecyclerView mRv;
    private MarqueeRunnable marqueeRunnable;
    private static Handler marqueeHandler;

    private static final int MILLISECONDS_MARQUEE_STAND = 3000;
    private static final int MILLISECONDS_MARQUEE_TURNING = 3000;

    private int mStandDuration = MILLISECONDS_MARQUEE_STAND;
    private int mTurningDuration = MILLISECONDS_MARQUEE_TURNING;

    private int mMarqueeHeight = 100;
    private int mScrollItemCount = 1;
    private int mshowItemCount = 1;

    private final boolean DEBUG = true;
    private final String TAG = "MarqueeTag";

    private MarqueeScrollListener marqueeScrollListener;
    private SmoothScrolLinearLayoutManager layoutManager;

    public MarqueeView(Context context) {
        this(context, null);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MarqueeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        if (attrs != null) {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            int height = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.MATCH_PARENT);
            mMarqueeHeight = height;
            a = context.obtainStyledAttributes(attrs, R.styleable.MarqueeViewStyle);
            mScrollItemCount = a.getInteger(R.styleable.MarqueeViewStyle_scrollItemCount, 1);
            mshowItemCount = a.getInteger(R.styleable.MarqueeViewStyle_showItemCount, 1);
            mStandDuration = a.getInteger(R.styleable.MarqueeViewStyle_standDuration, MILLISECONDS_MARQUEE_STAND);
            mTurningDuration = a.getInteger(R.styleable.MarqueeViewStyle_turningDuration, MILLISECONDS_MARQUEE_TURNING);
            a.recycle();
        }

        if (DEBUG) {
            Log.i(TAG, "item count " + mScrollItemCount);
        }
        mRv = new RecyclerView(context);
        mRv.setHasFixedSize(true);
        layoutManager = new SmoothScrolLinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(layoutManager);

        marqueeScrollListener = new MarqueeScrollListener();
        mRv.clearOnScrollListeners();
        mRv.addOnScrollListener(marqueeScrollListener);

        marqueeRunnable = new MarqueeRunnable();
        synchronized (this) {
            if (marqueeHandler == null) {
                marqueeHandler = new Handler();
            }
        }
        addView(mRv);
        mRv.getLayoutParams().height = mMarqueeHeight;
    }

    public void startScroll() {
        LinearLayoutManager llm = (LinearLayoutManager) mRv.getLayoutManager();
        if (llm.getItemCount() > mshowItemCount) {
            if (marqueeHandler != null && marqueeRunnable != null) {
                marqueeHandler.removeCallbacks(marqueeRunnable);
                marqueeHandler.postDelayed(marqueeRunnable, mStandDuration);
            }
        } else {
            marqueeHandler.removeCallbacks(marqueeRunnable);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRv.setAdapter(adapter);
    }

    private class MarqueeScrollListener extends RecyclerView.OnScrollListener {
        MarqueeScrollListener() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    if (DEBUG) {
                        Log.i(TAG, " sroll state idle ");
                    }
                    LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (llm.getItemCount() > mshowItemCount) {
                        if (llm.getItemCount() == llm.findLastVisibleItemPosition() + 1) {
                            recyclerView.scrollToPosition(0);
                        }
                        startScroll();
                    }
                    break;
            }
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }
    }


    private void smoothNextPosition(RecyclerView rv) {
        if (rv != null) {
            LinearLayoutManager llm = (LinearLayoutManager) rv.getLayoutManager();

            int lastVisibleItemPosition = llm.findLastVisibleItemPosition();
            int totalCount = llm.getItemCount();
            if (totalCount == lastVisibleItemPosition + 1) {
                rv.scrollToPosition(0);
            }
            int nextPosition = llm.findLastVisibleItemPosition() + mScrollItemCount;
            if (nextPosition < totalCount) {
                rv.smoothScrollToPosition(nextPosition);
            }
        }
    }

    private class MarqueeRunnable implements Runnable {
        @Override
        public void run() {
            smoothNextPosition(mRv);
            if (DEBUG) {
                Log.i(TAG, "smooth next position ");
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            ev.setAction(MotionEvent.ACTION_DOWN);
            mRv.dispatchTouchEvent(ev);
            ev.setAction(MotionEvent.ACTION_UP);
            mRv.dispatchTouchEvent(ev);
        }
        super.dispatchTouchEvent(ev);
        return true;

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (marqueeHandler != null) {
            marqueeHandler.removeCallbacks(marqueeRunnable);
            mRv.scrollToPosition(0);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (DEBUG) {
            Log.i(TAG, "onacctached");
        }
        startScroll();
    }
}
