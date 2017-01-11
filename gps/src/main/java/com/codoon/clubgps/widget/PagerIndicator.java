package com.codoon.clubgps.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codoon.clubgps.R;
import com.codoon.clubgps.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frankie on 2017/1/11.
 */

public class PagerIndicator extends LinearLayout {

    private int item_width;//每个item最小宽度
    private LinearLayout itemLy;//item都放这里头
    private List<TextView> mTextViewList;
    private View lineView;
    private ViewPager mViewPager;
    private int textNormalColor;
    private int textSelectedColor;

    private int lineColor;
    private float currentX;//line当前的X坐标

    private Context mContext;
    private int currentIndex;

    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        item_width = CommonUtil.getScreenWidth(mContext) / 5;//暂时只能展示最多5个控件
        lineColor = ContextCompat.getColor(mContext, R.color.orange);
        textSelectedColor = lineColor;
        textNormalColor = ContextCompat.getColor(mContext, R.color.light_white);
        init();
    }

    private void init(){
        setOrientation(LinearLayout.VERTICAL);
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(mContext);
        itemLy = new LinearLayout(mContext);
        horizontalScrollView.addView(itemLy);
        horizontalScrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
        addView(horizontalScrollView);
    }

    public void setTitle(String... titles){
        //1.计算每个item占用宽度
        int width = CommonUtil.getScreenWidth(mContext) / titles.length;
        if(width > item_width)
            item_width = width;
        //2.获取view
        mTextViewList = new ArrayList<>();
        TextView itemTextView;
        int index = 0;
        for(String title : titles){
            itemTextView = getTextView(index, title);
            itemLy.addView(itemTextView, item_width, LayoutParams.MATCH_PARENT);
            mTextViewList.add(itemTextView);
            index++;
        }
        //3.添加一根和item一样宽度的线
        lineView = new View(mContext);
        lineView.setBackgroundColor(lineColor);
        lineView.setLayoutParams(new LayoutParams(item_width, 3));
        addView(lineView);

    }

    public void setViewPager(ViewPager viewPager){
        this.mViewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float newX = (position * item_width) + positionOffset * item_width;
                ObjectAnimator.ofFloat(lineView, "translationX" , currentX, newX).start();
                currentX = newX;
            }

            @Override
            public void onPageSelected(int position) {
                itemSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        itemSelected(viewPager.getCurrentItem());
    }

    private TextView getTextView(int index, String text){
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(textNormalColor);
        textView.setTextSize(14);
        textView.setTag(index);
        textView.setOnClickListener(itemClickListener);
        return textView;
    }

    private OnClickListener itemClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();
            itemSelected(index);
            moveTo(index);
        }
    };

    private android.widget.AbsListView.OnScrollListener mOnScrollChangeListener = new android.widget.AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }

    };

    private void moveTo(int index){
        mViewPager.setCurrentItem(index);
    }

    private void itemSelected(int position){
        mTextViewList.get(currentIndex).setTextColor(textNormalColor);
        mTextViewList.get(position).setTextColor(textSelectedColor);
        currentIndex = position;
    }


}
