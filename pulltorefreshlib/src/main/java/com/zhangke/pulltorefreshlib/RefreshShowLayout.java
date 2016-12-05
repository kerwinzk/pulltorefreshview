package com.zhangke.pulltorefreshlib;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by zhangke on 2016/12/5.
 */

public class RefreshShowLayout extends FrameLayout{


    public RefreshShowLayout(Context context) {
        this(context, null);
    }

    public RefreshShowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshShowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
