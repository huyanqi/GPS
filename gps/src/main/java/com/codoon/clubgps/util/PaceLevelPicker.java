package com.codoon.clubgps.util;

/**
 * Created by Frankie on 2017/1/14.
 * <p>
 * 计算配速等级、颜色
 */

public class PaceLevelPicker {

    private float[] levels;
    private int[] colors = new int[]{0xffF85445, 0xffd8d552, 0xff79E05C};
    private long max_pace;
    private int count;

    private PaceLevelPicker() {
    }

    public PaceLevelPicker(long max_pace, int count) {
        this.max_pace = max_pace;
        this.count = count;
        init();
    }

    private void init() {
        float offset = max_pace / colors.length;
        levels = new float[colors.length];
        for (int i = 1; i <= levels.length; i++) {
            levels[i - 1] = i * offset;
        }
    }

    public int getColor(float value){
        int level = getLevel(value);
        return colors[level];
    }

    /**
     * 根据value获取等级
     * @param value
     * @return
     */
    private int getLevel(float value){
        int level = levels.length - 1;
        float startV = 0;
        for(int i=0;i<levels.length;i++){
            if(startV <= value && value <= levels[i]){
                level = i;
                break;
            }
            startV = levels[i];
        }

        return level;
    }

}
