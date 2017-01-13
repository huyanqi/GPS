package com.codoon.clubgps.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.codoon.clubgps.R;
import com.codoon.clubgps.adapter.FragmentPagerAdapter;
import com.codoon.clubgps.bean.HistoryDetail;
import com.codoon.clubgps.ui.fragment.history.HistoryDetailFragment;
import com.codoon.clubgps.ui.fragment.history.HistoryPagerChatFragment;
import com.codoon.clubgps.ui.fragment.history.HistoryPagerDetailFragment;
import com.codoon.clubgps.ui.fragment.history.HistoryPagerPaceFragment;
import com.codoon.clubgps.ui.fragment.history.HistoryPagerPathFragment;
import com.codoon.clubgps.widget.PagerIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frankie on 2017/1/10.
 *
 * 运动历史详情
 */

public class HistoryDetailActivity extends FragmentActivity {

    private PagerIndicator mPagerIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mPagerAdapter;
    private List<Fragment> mFragmentList;
    private HistoryDetail mHistoryDetail;

    private ViewGroup detailLy;

    private HistoryDetailFragment mHistoryDetailFragment;
    private HistoryPagerPathFragment mHistoryPagerPathFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        init();
    }

    private void init(){
        loadIntent();
        initFragments();
        detailLy = (ViewGroup) findViewById(R.id.detail_ly);
        mPagerIndicator = (PagerIndicator) findViewById(R.id.pager_indicator);
        mPagerIndicator.setTitle(getResources().getStringArray(R.array.detail_tab_titles));
        mPagerIndicator.setViewPager(mViewPager);
    }

    private void initFragments(){
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mHistoryDetailFragment = (HistoryDetailFragment) HistoryDetailActivity.this.getSupportFragmentManager().findFragmentById(R.id.history_detail_fragment);
        mHistoryDetailFragment.setData(mHistoryDetail);

        mFragmentList = new ArrayList<>();
        mFragmentList.add(mHistoryPagerPathFragment = new HistoryPagerPathFragment());
        mFragmentList.add(new HistoryPagerDetailFragment());
        mFragmentList.add(HistoryPagerPaceFragment.newInstance(mHistoryDetail));
        mFragmentList.add(HistoryPagerChatFragment.newInstance(mHistoryDetail));
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);

        mViewPager.setAdapter(mPagerAdapter);

        mHistoryDetailFragment.setActivity(this);
        mHistoryPagerPathFragment.setActivity(this);
    }

    private void loadIntent(){
        mHistoryDetail = getIntent().getParcelableExtra("bean");

    }

    public void switchViewPager(){
        //隐藏PathFragment的viewpager
        detailLy.setVisibility(detailLy.isShown() ? View.INVISIBLE : View.VISIBLE);
        //显示DetailFragment的内容框
        mHistoryDetailFragment.switchContent();
    }

    public void switchKmView(){
        mHistoryDetailFragment.switchKmPosition();
    }

    public void switchMapType(){
        mHistoryDetailFragment.switchMapType();
    }

    @Override
    public void onBackPressed() {
        if(mHistoryDetailFragment.isContentIsShown()){
            switchViewPager();
        }else{
            super.onBackPressed();
        }
    }

}
