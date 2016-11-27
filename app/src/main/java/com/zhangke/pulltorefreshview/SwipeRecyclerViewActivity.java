package com.zhangke.pulltorefreshview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangke.pulltorefreshlib.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SwipeRecyclerViewActivity extends AppCompatActivity {

    private SwipeRecyclerView swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<String> datas;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_recycler_view);

        swipeRefreshLayout = (SwipeRecyclerView) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = swipeRefreshLayout.getRecyclerView();
        datas = new ArrayList<String>();


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter();
        mRecyclerView.setAdapter(myAdapter);

        swipeRefreshLayout.setOnPullListener(onPullListener);
        swipeRefreshLayout.autoRefresh();

    }

    private int loadmoreCount;
    private int refreshCount;

    private SwipeRecyclerView.OnPullListener onPullListener = new SwipeRecyclerView.OnPullListener() {
        @Override
        public void onLoadmore() {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {



                    datas.add("loadmore" + ++loadmoreCount);

                    myAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.onComplete(false);

                    Toast.makeText(SwipeRecyclerViewActivity.this, "已经获取更多数据了", Toast.LENGTH_SHORT).show();

                }
            }, 2000);
        }

        @Override
        public void onRefresh() {

            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (datas.size() == 0) {
                        for (int i = 0; i < 15; i++) {
                            datas.add("item" + i);
                        }
                    }else{
                        datas.add(0, "refresh" + ++refreshCount);
                    }

                    myAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.onComplete(false);

                    Toast.makeText(SwipeRecyclerViewActivity.this, "已经获取最新数据了", Toast.LENGTH_SHORT).show();

                }
            }, 2000);

        }
    };

    /**
     * adapter
     */
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            return new MyViewHolder(View.inflate(parent.getContext(), android.R.layout.simple_list_item_1, null));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {


            private TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
