package com.zhangke.pulltorefreshview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.zhangke.pulltorefreshlib.PullRefreshLayout;
import com.zhangke.pulltorefreshlib.pullview.PullGridView;
import com.zhangke.pulltorefreshlib.pullview.PullListView;

import java.util.ArrayList;
import java.util.List;

public class PullGridViewActivity extends AppCompatActivity {

    private PullGridView gridview;
    private List<String> datas;
    private PullRefreshLayout mPullRefreshLayout;
    private ArrayAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_grid_view);

        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pull_refresh_layout);
        gridview = (PullGridView) findViewById(R.id.listview);
        datas = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            datas.add("item" + i);
        }
        myAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, datas);

        gridview.setAdapter(myAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                System.out.println("onitemclick" + arg2);
            }
        });

        mPullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            private int loadmoreCount;
            private int refreshCount;

            @Override
            public void onRefresh() {

                mPullRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        datas.add(0, "refresh" + ++refreshCount);

                        myAdapter.notifyDataSetChanged();

                        mPullRefreshLayout.onComplete(false);

                        Toast.makeText(PullGridViewActivity.this, "已经获取最新数据了", Toast.LENGTH_SHORT).show();

                    }
                }, 2000);
            }

            @Override
            public void onLoadmore() {
                mPullRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        datas.add("loadmore" + ++loadmoreCount);

                        myAdapter.notifyDataSetChanged();

                        mPullRefreshLayout.onComplete(false);

                        Toast.makeText(PullGridViewActivity.this, "已经获取更多数据了", Toast.LENGTH_SHORT).show();

                    }
                }, 2000);
            }
        });

    }

}
