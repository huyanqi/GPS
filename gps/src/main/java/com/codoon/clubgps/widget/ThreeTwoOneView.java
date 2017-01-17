package com.codoon.clubgps.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codoon.clubgps.R;
import com.codoon.clubgps.util.CommonUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Frankie on 2017/1/16.
 *
 * 321倒计时
 */

public class ThreeTwoOneView extends RelativeLayout {

    private View bgView;
    private Queue<View> animQueue;
    private LayoutParams rlp;
    private OnAnimFinishListener mOnAnimFinishListener;

    public ThreeTwoOneView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //添加4个控件，321数字+扩散的背景

        rlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.CENTER_IN_PARENT);

        bgView = new View(context);
        bgView.setBackgroundResource(R.drawable.bg_threetwoone);
        int width = CommonUtil.dip2px(60);
        LayoutParams bgLp = new LayoutParams(width, width);
        bgLp.addRule(RelativeLayout.CENTER_IN_PARENT);
        bgView.setAlpha(0);

        addView(bgView, bgLp);

        numbers = new ArrayList<TextView>();
        animQueue = new LinkedList<View>();

        addNumber(3);
        addNumber(2);
        addNumber(1);
    }

    /**
     * 开始倒计时
     */
    public void start(){
        animQueue.addAll(numbers);
        animNext();
    }

    public void setOnAnimFinishListener(OnAnimFinishListener onAnimFinishListener) {
        mOnAnimFinishListener = onAnimFinishListener;
    }

    private List<TextView> numbers;
    private void addNumber(int number){
        TextView textView = new TextView(getContext());
        textView.setText(number+"");
        textView.setTextSize(30);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
        textView.setGravity(Gravity.CENTER);
        textView.setAlpha(0);
        textView.setLayoutParams(rlp);
        CommonUtil.setCustomTypeFace(textView);
        addView(textView);
        numbers.add(textView);
    }

    private void animNext(){
        View view = animQueue.poll();
        if(view == null){
            mOnAnimFinishListener.onAnimFinished();
            return;
        }

        AnimatorSet animationSet = new AnimatorSet();

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1, 1, 1, 1, 0);
        alphaAnimator.setDuration(1000);

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0,10,10,10,10,0);
        scaleXAnimator.setDuration(1000);

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0,10,10,10,10,0);
        scaleYAnimator.setDuration(1000);

        ObjectAnimator bgalphaAnimator = ObjectAnimator.ofFloat(bgView, "alpha", 1, 0);
        bgalphaAnimator.setDuration(500);

        ObjectAnimator bgscaleXAnimator = ObjectAnimator.ofFloat(bgView, "scaleX", 0, 5);
        bgscaleXAnimator.setDuration(500);

        ObjectAnimator bgscaleYAnimator = ObjectAnimator.ofFloat(bgView, "scaleY", 0, 5);
        bgscaleYAnimator.setDuration(500);

        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                animNext();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        animationSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator, bgalphaAnimator, bgscaleXAnimator, bgscaleYAnimator);
        animationSet.start();

    }

    public interface OnAnimFinishListener {
        void onAnimFinished();
    }

}
