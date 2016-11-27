package com.zhangke.pulltorefreshlib;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * 实现刷新加载的Layout
 *
 * @author zhangke
 */
public class PullRefreshLayout extends LinearLayout {
    /**
     * pull速率，减少偏移量
     */
    private final float PULL_RATE = 2f;
    /**
     * 正在刷新标记
     */
    private final int PULL_STATE_REFRESH = 1;
    /**
     * 正在加载标记
     */
    private final int PULL_STATE_LOADMORE = 2;
    /**
     * 正在刷新加载状态标记
     */
    private int mRefreshStateFlag = 0;
    /**
     * 下拉刷新View
     */
    private View mRefreshView;
    /**
     * 执行刷新加载的View
     */
    private View mPullView;
    /**
     * 上拉加载View
     */
    private View mLoadmoreView;
    /**
     * 子View的高度
     */
    private int mRefreshViewHeight, mPullViewHeight, mLoadmoreViewHeight;
    /**
     * 刷新加载移动偏移量
     */
    private int mPullOffset;
    /**
     * 是否可以刷新或者加载
     */
    private boolean refreshEnable = true, loadmoreEnable = true;
    /**
     * down下时的坐标
     */
    private int downY;
    /**
     * 移动是的x和y
     */
    private float lastY;
    /**
     * 移动的最小距离
     */
    private int mScaledTouchSlop;

    public PullRefreshLayout(Context context) {
        this(context, null);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 1、首先获取三个View
        mRefreshView = getChildAt(0);
        mPullView = getChildAt(1);
        mLoadmoreView = getChildAt(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 2、获取view的高度
        mRefreshViewHeight = mRefreshView.getMeasuredHeight();
        mPullViewHeight = getMeasuredHeight();
        mLoadmoreViewHeight = mLoadmoreView.getMeasuredHeight();
        // 保证刷新页面填充这个页面
        mPullView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        // 3、通过移动偏移量重新layout位置
        mRefreshView
                .layout(l, mPullOffset - mRefreshViewHeight, r, mPullOffset);

        mPullView.layout(l, mPullOffset, r, mPullOffset + mPullViewHeight);

        mLoadmoreView.layout(l, mPullOffset + mPullViewHeight, r, mPullOffset
                + mPullViewHeight + mLoadmoreViewHeight);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 4、
                downY = (int) ev.getY();
                lastY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) (ev.getY() - lastY);
                lastY = ev.getY();

                if (moveY > 0) {
                    // 向下滑动两种情况： 1、一种是需要显示刷新View 2、就是在显示加载view时向下滑动，此时需要移动布局
                    if (mPullOffset < 0
                            || (((PullView) mPullView).canRefresh()
                            && refreshEnable && mRefreshStateFlag != PULL_STATE_LOADMORE)) {

                        mPullOffset = (int) (mPullOffset + moveY / PULL_RATE);
                    }
//                    if ((mPullOffset < 0
//                            || ((PullView) mPullView).canRefresh())
//                            && refreshEnable && mRefreshStateFlag != PULL_STATE_LOADMORE) {
//
//                        mPullOffset = (int) (mPullOffset + moveY / PULL_RATE);
//                    }

                } else if (moveY < 0) {
                    // 向上滑动两种情况： 1、一种是需要显示加载View 2、就是在显示刷新view时向上滑动，此时需要移动布局
                    if (mPullOffset > 0
                            || (((PullView) mPullView).canLoadmore()
                            && loadmoreEnable && mRefreshStateFlag != PULL_STATE_REFRESH)) {
                        mPullOffset = (int) (mPullOffset + moveY / PULL_RATE);
                    }
                }

                if (mPullOffset != 0) {
                    requestLayout();
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                } else if (mRefreshStateFlag != 0) {
                    requestLayout();
                }


                break;

            case MotionEvent.ACTION_UP:
                // 7、显示刷新加载页面, 只有当偏移量大于等于RefreshView、LoadmoreView的高度时，才开始刷新
                if (mPullOffset > 0 && Math.abs(mPullOffset) >= mRefreshViewHeight) {
                    // 刷新
                    mPullOffset = mRefreshViewHeight;
                    requestLayout();
                    if (mRefreshStateFlag != PULL_STATE_REFRESH) {
                        startRefresh();
                    }

                } else if (mPullOffset < 0
                        && Math.abs(mPullOffset) >= mLoadmoreViewHeight) {
                    // 加载
                    mPullOffset = -mLoadmoreViewHeight;
                    requestLayout();
                    if (mRefreshStateFlag != PULL_STATE_LOADMORE) {
                        startLoadmore();
                    }
                } else {
                    finishRefreshView();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                mPullOffset = 0;
                requestLayout();

            default:
                break;
        }

        super.dispatchTouchEvent(ev);
        return true;
    }

    /**
     * 刷新加载结束隐藏页面
     */
    private void finishRefreshView() {
        mPullOffset = 0;
        mRefreshStateFlag = 0;
        requestLayout();
    }

    /**
     * 开始刷新
     */
    private void startRefresh() {
        mRefreshStateFlag = PULL_STATE_REFRESH;
        success();

    }

    /**
     * 开始加载
     */
    private void startLoadmore() {
        mRefreshStateFlag = PULL_STATE_LOADMORE;
        success();
    }

    /**
     * 测试使用
     */
    void success() {

        new Thread() {
            @Override
            public void run() {
                System.out.println("后台访问中" + mRefreshStateFlag);
                SystemClock.sleep(5000);
                handler.sendEmptyMessage(++code);
            }

            ;
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);

            if (msg.what == code) {

                finishRefreshView();
            }

        }

        ;
    };

    int code = 0;

    /**
     * 是否开启刷新功能
     *
     * @param canRefresh
     */
    public void setCanRefresh(boolean canRefresh) {
        this.refreshEnable = canRefresh;
    }

    /**
     * 是否开启加载更多功能
     *
     * @param canLoadmore
     */
    public void setCanLoadmore(boolean canLoadmore) {
        this.loadmoreEnable = canLoadmore;
    }

    /**
     * 数据刷新接口
     */
    private OnRefreshListener mOnRefreshListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    /**
     * 刷新接口
     *
     * @author zhangke
     */
    public interface OnRefreshListener {
        /**
         * 刷新数据
         */
        boolean onRefresh();

        /**
         * 加载更多数据
         */
        boolean onLoadmore();
    }

    /**
     * 刷新加载View
     */
    public interface PullView {
        /**
         * 是否可以刷新
         *
         * @return
         */
        boolean canRefresh();

        /**
         * 是否可以加载
         *
         * @return
         */
        boolean canLoadmore();
    }

}
