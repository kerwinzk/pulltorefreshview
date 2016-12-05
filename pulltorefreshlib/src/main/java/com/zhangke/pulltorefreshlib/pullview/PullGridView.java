package com.zhangke.pulltorefreshlib.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.zhangke.pulltorefreshlib.PullRefreshLayout;


public class PullGridView extends GridView implements PullRefreshLayout.PullView {

    public PullGridView(Context context) {
        this(context, null);
    }

    public PullGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canRefresh() {
        if (getCount() == 0) {
            return true;
        }
        // 判断是否滑动到顶部
        if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0) {
            return true;
        }
        return false;

    }

    @Override
    public boolean canLoadmore() {
        if (getCount() == 0) {
            return false;
        }

        // 判断是否滑动到底部
        if (getLastVisiblePosition() == (getCount() - 1)) {
            View childView = getChildAt(getChildCount() - 1);
            if (childView != null
                    && childView.getBottom() <= getMeasuredHeight()) {
                return true;
            }
        }
        return false;

    }

}
