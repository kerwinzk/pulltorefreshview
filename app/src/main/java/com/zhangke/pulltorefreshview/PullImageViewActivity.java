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
                        int length = images.length;

                        try {
                            URL url = new URL(images[++count % length]);

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout(10 * 1000);
                            conn.setConnectTimeout(10 * 1000);
                            conn.setRequestMethod("GET");
                            InputStream inputStream = conn.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            BitmapDrawable drawable = new BitmapDrawable(bitmap);
                            imageView.setImageDrawable(drawable);

                            inputStream.close();

                            mPullRefreshLayout.onComplete(true);
                        } catch (Exception e) {

                        }



                    }
                }, 2000);
            }

            @Override
            public void onLoadmore() {

            }
        });


    }

    public static String[] images = {"http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_8243.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383248_3693.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383243_5120.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383242_3127.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383242_9576.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383242_1721.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383219_5806.jpg",};

}
