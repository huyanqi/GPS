package com.codoon.clubcodoongps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codoon.clubcodoongps.adapter.PaceChatAdapter;
import com.codoon.clubgps.bean.PaceChatBean;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PaceChatAdapter mAdapter;
    private List<PaceChatBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
    }

    private void init(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new PaceChatAdapter(this, mList = new ArrayList<PaceChatBean>()));

        mList.add(new PaceChatBean(1, 88, 12, ""));
        mList.add(new PaceChatBean(2, 178, 45, ""));
        mList.add(new PaceChatBean(3, 92, 231, ""));
        mList.add(new PaceChatBean(4, 68, 34, ""));
        mList.add(new PaceChatBean(5, 68, 67, ""));
        mList.add(new PaceChatBean(5, 68, 67, ""));
        mList.add(new PaceChatBean(0, 0, 0, "5公里 00:08:33"));
        mList.add(new PaceChatBean(6, 68, 17, ""));
        mList.add(new PaceChatBean(7, 68, 37, ""));

        mAdapter.setMaxDuration(231);
        mAdapter.setMaxPace(68);
        mAdapter.notifyDataSetChanged();
    }

}
