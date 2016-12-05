package com.zhangke.pulltorefreshview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhangke.pulltorefreshlib.pullview.PullGridView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pullListView(View view) {
        startActivity(new Intent(this, PullListViewActivity.class));
    }

    public void pullGridView(View view) {
        startActivity(new Intent(this, PullGridViewActivity.class));
    }

    public void swipeRecyclerView(View view) {
        startActivity(new Intent(this, SwipeRecyclerViewActivity.class));
    }


}
