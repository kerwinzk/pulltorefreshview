package com.zhangke.pulltorefreshlib.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.zhangke.pulltorefreshlib.PullRefreshLayout;


public class PullImageView extends ImageView implements PullRefreshLayout.PullView {

    public PullImageView(Context context) {
        this(context, null);
    }

    public PullImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canRefresh() {
        return true;
    }

    @Override
    public boolean canLoadmore() {
        return false;

    }

}
