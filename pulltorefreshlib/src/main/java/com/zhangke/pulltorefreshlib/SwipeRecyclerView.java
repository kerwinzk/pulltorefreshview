package com.zhangke.pulltorefreshlib;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 可刷新加载RecyclerView
 *
 * @author zhangke
 */

public class SwipeRecyclerView extends SwipeRefreshLayout {
    /**
     * 正在刷新
     */
    public static final int PULL_STATE_REFRESH = 1;
    /**
     * 正在加载
     */
    public static final int PULL_STATE_LOADMORE = 2;
    /**
     * 滑动速率
     */
    private final int PULL_RATE = 2;
    /**
     * 刷新加载表示
     */
    private int mRefreshStateFlag;
    /**
     * recyclerview
     */
    private RecyclerView mRecyclerView;
    /**
     * 空数据提示
     */
    private TextView mTvEmpty;
    /**
     * 加载更多view
     */
    private ProgressBar mPbLoadmore;
    /**
     * 加载更多view高度
     */
    private int mLoadmoreViewHeight;
    /**
     * recyclreview高度
     */
    private int mRecyclerViewHeight;
    /**
     * 滑动偏移量
     */
    private int mPullOffset;
    /**
     * 变量
     */
    private int lastY;
    /**
     * 刷新加载监听
     */
    private OnPullListener mOnPullListener;
    /**
     * 刷新加载类型
     */
    private RefreshMode mRefreshMode = RefreshMode.BOTH;

    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.swipe_recyclerview, this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mTvEmpty = (TextView) findViewById(R.id.tv_empty);
        mPbLoadmore = (ProgressBar) findViewById(R.id.pb_loadmore);
        mRecyclerView.addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (mRefreshMode == mRefreshMode.DISABLE || mRefreshMode == mRefreshMode.LOADMORE_MODE) {
                    setEnabled(false);
                    return;
                }

                // SwipeRefreshLayout 和 RecyclerView下拉冲突
                int top = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0
                        : recyclerView.getChildAt(0).getTop();
                setEnabled(top >= 0);

            }

        });

        setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mLoadmoreViewHeight = mPbLoadmore.getMeasuredHeight();
        mRecyclerViewHeight = mRecyclerView.getMeasuredHeight();
        mRecyclerView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mRecyclerView.layout(left, mPullOffset, right, mPullOffset + mRecyclerViewHeight);

        mPbLoadmore.layout(left, mPullOffset + mRecyclerViewHeight, right,
                mPullOffset + mRecyclerViewHeight + mLoadmoreViewHeight);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) (ev.getY() - lastY);
                lastY = (int) ev.getY();
                if (moveY < 0 && (mPullOffset > 0 || canLoadmore()) && mRefreshStateFlag != PULL_STATE_REFRESH
                        && (mRefreshMode == RefreshMode.BOTH || mRefreshMode == RefreshMode.LOADMORE_MODE)) {

                    mPullOffset = mPullOffset + moveY / PULL_RATE;

                } else if (moveY > 0 && mPullOffset < 0) {

                    mPullOffset = mPullOffset + moveY / PULL_RATE;
                }

                if (mPullOffset != 0) {
                    requestLayout();
                    // 屏蔽recyclerview item的点击及长按点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }

                break;
            case MotionEvent.ACTION_UP:

                if (mPullOffset == 0) {
                    break;
                }

                if (mPullOffset < 0 && Math.abs(mPullOffset) >= mLoadmoreViewHeight) {
                    mPullOffset = -mLoadmoreViewHeight;
                    requestLayout();

                    // 刷新数据,正在加载中不能重复刷新
                    if (mRefreshStateFlag != PULL_STATE_LOADMORE && mOnPullListener != null) {
                        mRefreshStateFlag = PULL_STATE_LOADMORE;
                        mOnPullListener.onLoadmore();
                    }

                } else {
                    mPullOffset = 0;
                    requestLayout();
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
     * 判断recyclerview是否滑动到底部
     *
     * @return
     */
    private boolean canLoadmore() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        int childCount = layoutManager.getChildCount();
        int itemCount = layoutManager.getItemCount();
        int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

        if (childCount > 0 && lastVisibleItemPosition == itemCount - 1
                && layoutManager.getChildAt(childCount - 1).getBottom() <= getMeasuredHeight()) {
            return true;
        }
        return false;
    }

    /**
     * 获取recyclerview
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        if (mOnPullListener != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    setRefreshing(true);
                    mRefreshStateFlag = PULL_STATE_REFRESH;
                    mOnPullListener.onRefresh();
                }
            });

        }
    }

    /**
     * 设置刷新加载类型
     *
     * @param mode
     */
    public void setRefreshMode(RefreshMode mode) {
        this.mRefreshMode = mode;

        if (mode == RefreshMode.BOTH || mode == RefreshMode.REFRESH_MODE) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }

    /**
     * 是否可以使用刷新加载
     *
     * @author zhangke
     */
    public enum RefreshMode {
        BOTH, REFRESH_MODE, LOADMORE_MODE, DISABLE
    }

    /**
     * 设置刷新加载监听
     *
     * @param listener
     */
    public void setOnPullListener(OnPullListener listener) {
        this.mOnPullListener = listener;

        // 当设置监听时，默认开启刷新和加载
        mRefreshMode = RefreshMode.BOTH;

        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mOnPullListener != null) {
                    mRefreshStateFlag = PULL_STATE_REFRESH;
                    mOnPullListener.onRefresh();
                }
            }
        });
    }

    /**
     * 刷新加载完成
     *
     * @param empty true：显示空提示 false 不显示空提示
     */
    public void onComplete(boolean empty) {
        if (empty) {
            mRecyclerView.setVisibility(View.GONE);
            mTvEmpty.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mTvEmpty.setVisibility(View.GONE);

            if (mRefreshStateFlag == PULL_STATE_LOADMORE) {
                // 上移一部分
                mRecyclerView.smoothScrollBy(0, 100);
            }
        }

        if (mRefreshStateFlag == PULL_STATE_REFRESH) {
            setRefreshing(false);
            mRefreshStateFlag = 0;
        } else if (mRefreshStateFlag == PULL_STATE_LOADMORE) {
            if (mPullOffset != 0) {
                mPullOffset = 0;
                requestLayout();
            }
            mRefreshStateFlag = 0;
        }
    }

    /**
     * 加载更多数据
     */
    public interface OnPullListener {
        /**
         * 加载数据
         */
        void onLoadmore();

        /**
         * 刷新数据
         */
        void onRefresh();
    }
}
