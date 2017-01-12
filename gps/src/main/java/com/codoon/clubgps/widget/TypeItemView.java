package com.codoon.clubgps.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codoon.clubgps.R;
import com.codoon.clubgps.util.CommonUtil;

/**
 * Created by huan on 16/1/6.
 */
public class TypeItemView extends RelativeLayout {

    private TextView mTvValue;
    private TextView mTvUnit;

    public TypeItemView(Context context) {
        super(context);
        initView(context,null);
    }

    public TypeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TypeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TypeItemView);
        View view = LayoutInflater.from(context).inflate(R.layout.type_item, this);
        ImageView mIvIcon = (ImageView) view.findViewById(R.id.mIvIcon);
        TextView mTvName = (TextView) view.findViewById(R.id.mTvName);
        mTvValue = (TextView) view.findViewById(R.id.mTvValue);
        mTvUnit = (TextView) view.findViewById(R.id.mTvUnit);

        CommonUtil.setCustomTypeFace(mTvValue);

        Drawable icon = typedArray.getDrawable(R.styleable.TypeItemView_type_icon);
        String name = typedArray.getString(R.styleable.TypeItemView_name);
        String value = typedArray.getString(R.styleable.TypeItemView_value);
        String unit = typedArray.getString(R.styleable.TypeItemView_unit);

        typedArray.recycle();

        mIvIcon.setImageDrawable(icon);
        mTvName.setText(name);
        mTvValue.setText(value);
        mTvUnit.setText(unit);
    }

    public void setData(String value){
        mTvValue.setText(value);
    }
    public void setUnit(String unit){
        mTvUnit.setText(unit);
    }
}
