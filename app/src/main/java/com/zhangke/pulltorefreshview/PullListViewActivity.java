package com.zhangke.pulltorefreshview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.zhangke.pulltorefreshlib.pullview.PullListView;

import java.util.ArrayList;
import java.util.List;

public class PullListViewActivity extends AppCompatActivity {

    private PullListView listview;
    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_list_view);

        listview = (PullListView) findViewById(R.id.listview);
        datas = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            datas.add("item " + i);
        }
        listview.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, datas));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                System.out.println("onitemclick" + arg2);
            }
        });

    }

}
