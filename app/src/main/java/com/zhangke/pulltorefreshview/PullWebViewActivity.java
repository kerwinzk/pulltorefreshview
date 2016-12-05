package com.zhangke.pulltorefreshview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.zhangke.pulltorefreshlib.PullRefreshLayout;
import com.zhangke.pulltorefreshlib.pullview.PullGridView;
import com.zhangke.pulltorefreshlib.pullview.PullWebView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

public class PullWebViewActivity extends AppCompatActivity {

    private PullWebView webView;
    private List<String> datas;
    private PullRefreshLayout mPullRefreshLayout;
    private ArrayAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_web_view);

        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pull_refresh_layout);
        //设置只能刷新
        mPullRefreshLayout.setRefreshMode(PullRefreshLayout.RefreshMode.REFRESH_MODE);


        webView = (PullWebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });




        mPullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {

            private int count;

            @Override
            public void onRefresh() {

                mPullRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (++count % 2 == 0) {
                            webView.loadUrl("http://news.qq.com/");
                        } else {
                            webView.loadUrl("http://www.sina.com.cn/");
                        }

                        mPullRefreshLayout.onComplete(true);

                    }
                }, 2000);
            }
            @Override
            public void onLoadmore() {

            }
        });

        mPullRefreshLayout.autoRefresh();


    }

}
