package com.codoon.clubgps.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Frankie on 2016/12/28.
 *
 * 动画效果
 */

public class AnimUtil {

    /**
     * 执行5.0开始有的reveal效果
     * @param isShow
     * @param view
     */
    public static void reveal(final boolean isShow, final View view, int finalRadius, int centerX, int centerY){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator animator;
            if (isShow) {
                view.setVisibility(View.VISIBLE);
                animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, finalRadius);
            } else {
                animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, finalRadius, 0);
            }
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(350);
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(isShow ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }else{
            view.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 执行透明动画
     * @param view
     * @param isHide 是否是从显示到隐藏
     */
    public static void alpha(final View view, final boolean isHide){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", isHide ? 1 : 0 , isHide ? 0 : 1).setDuration(300);
        objectAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.setClickable(!isHide);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        objectAnimator.start();
    }

}
