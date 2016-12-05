package com.zhangke.pulltorefreshlib;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zhangke on 2016/12/5.
 */

public class RefreshHeaderView extends FrameLayout {
    /**
     * 刷新
     */
    public static final int REFRESH_FLAG = 1;
    /**
     * 加载
     */
    public static final int LOADMROE_FLAG = 2;
    /**
     * 刷新提示
     */
    private TextView mTvRefresh;
    /**
     * 上拉下拉图标
     */
    private ImageView mIvPullStatus;
    /**
     * 刷新中
     */
    private ImageView mIvRefresh;
    /**
     * 标记是刷新还是加载
     */
    private int mRefreshFlag;
    /**
     * 当前状态
     */
    private RefreshStatus mRefreshStatus;
    /**
     * 刷新动画
     */
    private ObjectAnimator mRefreshAnimator;


    public RefreshHeaderView(Context context) {
        this(context, null);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.refresh_header_layout, this);

        mTvRefresh = (TextView) findViewById(R.id.tv_refresh);
        mIvPullStatus = (ImageView) findViewById(R.id.iv_pull_status);
        mIvRefresh = (ImageView) findViewById(R.id.iv_refresh);

        //定义刷新动画
        mRefreshAnimator = ObjectAnimator.ofFloat(mIvRefresh, "rotation", 0f, 360);
        mRefreshAnimator.setDuration(1000);
        mRefreshAnimator.setRepeatCount(Integer.MAX_VALUE);
    }

    /**
     * 设置是刷新View还是加载View
     *
     * @param flag
     */
    public void setRefreshFlag(int flag) {

        if (flag != REFRESH_FLAG && flag != LOADMROE_FLAG) {
            throw new IllegalArgumentException("参数异常");
        }

        mRefreshFlag = flag;
    }


    /**
     * 根据不同状态更新View
     *
     * @param status  状态
     * @param success 刷新是否成功
     */
    public void updateView(RefreshStatus status, boolean success) {
        /*
         * 两种情况下不刷新UI：
         *     1、状态相同，不刷新UI
         *     2、刷新中，只有为刷新完成状态才更新UI
         */
        if (status == mRefreshStatus || (mRefreshStatus == RefreshStatus.REFRESHING && status != RefreshStatus.COMPLETE)) {
            return;
        }

        mRefreshStatus = status;
        if (mRefreshFlag == REFRESH_FLAG) {
            //更新刷新
            updateRefresh(status, success);
        } else if (mRefreshFlag == LOADMROE_FLAG) {
            //更新加载
            updateLoadmore(status, success);
        }

    }

    /**
     * 刷新状态更新
     *
     * @param status  状态
     * @param success 刷新是否成功
     */
    private void updateRefresh(RefreshStatus status, boolean success) {
        if (status == RefreshStatus.PULL) {
            // 下拉刷新
            mTvRefresh.setText(R.string.pull_down_refresh);
            mTvRefresh.setCompoundDrawables(null, null, null, null);
            mTvRefresh.setVisibility(View.VISIBLE);

            mIvPullStatus.setVisibility(View.VISIBLE);
            mIvPullStatus.setImageLevel(0);

        } else if (status == RefreshStatus.RELEASE) {
            // 释放开始刷新
            mTvRefresh.setText(R.string.release_to_refresh);
            mIvPullStatus.setImageLevel(1);

        } else if (status == RefreshStatus.REFRESHING) {
            // 刷新中
            mTvRefresh.setText(R.string.refreshing);
            mIvPullStatus.setVisibility(View.GONE);
            mIvRefresh.setVisibility(View.VISIBLE);
            mRefreshAnimator.start();
        } else if (status == RefreshStatus.COMPLETE) {
            if (success) {
                mTvRefresh.setText(R.string.refresh_success);
                Drawable drawable = getResources().getDrawable(R.drawable.refresh_success);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mTvRefresh.setCompoundDrawables(drawable, null, null, null);

            } else {
                mTvRefresh.setText(R.string.refresh_failure);
                Drawable drawable = getResources().getDrawable(R.drawable.refresh_failed);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mTvRefresh.setCompoundDrawables(drawable, null, null, null);
            }
            //停止动画
            mRefreshAnimator.cancel();
            mIvRefresh.setVisibility(View.GONE);
            mRefreshStatus = null;

        }
    }

    /**
     * 加载状态更新
     *
     * @param status  状态
     * @param success 刷新是否成功
     */
    private void updateLoadmore(RefreshStatus status, boolean success) {
        if (status == RefreshStatus.PULL) {
            // 下拉刷新
            mTvRefresh.setText(R.string.pull_up_laodmore);
            mTvRefresh.setCompoundDrawables(null, null, null, null);
            mTvRefresh.setVisibility(View.VISIBLE);

            mIvPullStatus.setVisibility(View.VISIBLE);
            mIvPullStatus.setImageLevel(1);

        } else if (status == RefreshStatus.RELEASE) {
            // 释放开始刷新
            mTvRefresh.setText(R.string.release_to_laodmore);
            mIvPullStatus.setImageLevel(0);

        } else if (status == RefreshStatus.REFRESHING) {
            // 刷新中
            mTvRefresh.setText(R.string.loadmoreing);
            mIvPullStatus.setVisibility(View.GONE);
            mIvRefresh.setVisibility(View.VISIBLE);
            mRefreshAnimator.start();
        } else if (status == RefreshStatus.COMPLETE) {
            if (success) {
                mTvRefresh.setText(R.string.loadmore_success);
                Drawable drawable = getResources().getDrawable(R.drawable.refresh_success);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mTvRefresh.setCompoundDrawables(drawable, null, null, null);
            } else {
                mTvRefresh.setText(R.string.loadmore_failure);
                Drawable drawable = getResources().getDrawable(R.drawable.refresh_failed);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mTvRefresh.setCompoundDrawables(drawable, null, null, null);
            }
            //停止动画
            mRefreshAnimator.cancel();
            mIvRefresh.setVisibility(View.GONE);
            mRefreshStatus = null;
        }
    }


    /**
     * 定义刷新状态
     */
    public enum RefreshStatus {
        /**
         * 提示下拉刷新
         */
        PULL,
        /**
         * 提示释放开始刷新
         */
        RELEASE,
        /**
         * 刷新中
         */
        REFRESHING,
        /**
         * 刷新完成
         */
        COMPLETE
    }

}
