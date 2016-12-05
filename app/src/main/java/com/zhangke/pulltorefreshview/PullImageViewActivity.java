package com.zhangke.pulltorefreshview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhangke.pulltorefreshlib.PullRefreshLayout;
import com.zhangke.pulltorefreshlib.pullview.PullImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PullImageViewActivity extends AppCompatActivity {

    private PullRefreshLayout mPullRefreshLayout;
    private PullImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_imageview);

        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pull_refresh_layout);
        imageView = (PullImageView) findViewById(R.id.pullImageView);

        mPullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {

            private int count;

            @Override
            public void onRefresh() {

                mPullRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                    }
                }, 2000);
            }

            @Override
            public void onLoadmore() {

            }
        });

    }


}
