package com.zhangke.pulltorefreshlib.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import com.zhangke.pulltorefreshlib.PullRefreshLayout;


public class PullWebView extends WebView implements PullRefreshLayout.PullView {

    public PullWebView(Context context) {
        this(context, null);
    }

    public PullWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canRefresh() {
        if (getScrollY() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canLoadmore() {
        return false;
    }

}
