package com.codoon.clubgps.util;

import android.content.Context;
import android.media.MediaPlayer;

import com.codoon.clubgps.R;
import com.codoon.clubgps.application.GPSApplication;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * Created by Frankie on 2017/2/8.
 *
 * 语音播报器
 */

public class VoicePlayer {

    private static VoicePlayer sVoicePlayer;

    private Context mContext;
    private String soundPath;//临时保存被合成后提示音的文件
    private MediaPlayer mPlayer;
    private VoiceUtil mVoiceUtil;//数字转播报工具

    private int duration;//已耗时
    private double total_length;//跑步总距离,单位:米

    private int lastDuration;//最后一公里耗时
    private int lastDistanceKm;//最后播报的公里数,默认0，等跑到整数公里数时候再播报


    private VoicePlayer (){
        mContext = GPSApplication.getContext();
        soundPath = getGpsSounudPath();
        initVoiceUtil();
        if (!(new File(soundPath).exists())) {
            new File(soundPath).mkdir();
        }
    }

    private void initVoiceUtil(){
        int[] units = new int[]{0, SHI, BAI, QIAN, WAN};
        int[] nText = new int[]{LING, YI, ER, SAN, SI, WU, LIU, QI, BA, JIU};
        mVoiceUtil = new VoiceUtil(units, nText, DIAN, XIAOSHI, FENZHONG, MIAO);
    }

    public static VoicePlayer getInstance(){
        if(sVoicePlayer == null) {
            synchronized (VoicePlayer.class){
                if(sVoicePlayer == null)
                    sVoicePlayer = new VoicePlayer();
            }
        }
        return sVoicePlayer;
    }

    public void runStart(){
        playSounds(KAISHIPAOBU);
    }

    public void runPause(){
        playSounds(YUNDONGYIZANTING);
    }

    public void runResume(){
        playSounds(YUNDONGYIHUIFU);
    }

    public void runFinish(){
        playSounds(FANGSONGYIXIABA);
    }

    public void updateDuration(int duration){
        this.duration = duration;
        System.out.println("updateDuratin:"+duration);
        if(duration % (30) == 0){//60  * 5
            //每5分钟播放一次
            playDurationVoice(duration);
        }
    }

    public void updateLength(double total_length){
        this.total_length = total_length;
        System.out.println("updateLength:"+total_length);
        int kmNum = CommonUtil.getKmNumber(total_length / 1000);
        if(kmNum > lastDistanceKm){
            playKmVoice(kmNum, duration);
            lastDistanceKm = kmNum;
            lastDuration = duration;
        }

    }

    /**
     * 播报公里数
     * @param length 公里数,单位:km
     * @duration 耗时,单位:s
     */
    private void playKmVoice(double length, int duration){
        playKmSound(mVoiceUtil.numToList(length), mVoiceUtil.timeToList(duration));
    }

    /**
     * 播报用时
     * @param duration 耗时,单位:s
     */
    private void playDurationVoice(int duration){
        playDurationSound(mVoiceUtil.timeToList(duration), mVoiceUtil.timeToList(lastDuration));
    }

    private void playDurationSound(List<Integer> durationVoice, List<Integer> lastKmDurationVoice) {
        List<Integer> res = new ArrayList<>();
        res.add(DINGDONG);
        res.add(NIYIJING);
        res.add(YONGSHI);
        for(Integer lres : durationVoice){
            res.add(lres);
        }
        if(lastKmDurationVoice.size() > 0){//有最近一公里播报记录
            res.add(ZUIJINYIGONGLI);
            res.add(YONGSHI);
            for(Integer lres : lastKmDurationVoice){
                res.add(lres);
            }
        }

        playSounds(res);
    }

    private void playSounds(int... sounds){
        List<Integer> sourdsList = new ArrayList<>();
        for(int souds : sounds){
            sourdsList.add(souds);
        }
        playSounds(sourdsList);
    }

    /**
     * 合成公里播报
     * @param lengthVoice 距离语音
     * @param durationVoice 耗时语音
     */
    private void playKmSound(List<Integer> lengthVoice, List<Integer> durationVoice){
        List<Integer> res = new ArrayList<>();
        res.add(DINGDONG);
        res.add(NIYIJING);
        res.add(PAOBU);
        for(Integer lres : lengthVoice){
            res.add(lres);
        }
        res.add(GONGLI);
        res.add(YONGSHI);
        for(Integer dres : durationVoice){
            res.add(dres);
        }
        playSounds(res);
    }

    /**
     * 开始播放语音
     * @param sounds 语音的集合
     */
    private void playSounds(List<Integer> sounds){
        try {
            //1.先合成文件
            mergeSoundFile(sounds, soundPath);
            //2.重置播放器
            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.release();
                mPlayer = null;
            }
            //3.重置播放器
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(soundPath);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer arg0) {
                    Logger.e("tag", "player.setOnCompletionListener");
                    if (mPlayer != null) {
                        mPlayer.release();
                        mPlayer = null;
                    }
                }
            });
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 命名规则：
     *  key:语音文本内容
     *  value:资源ID
     */
    int KAISHIPAOBU = R.raw.run_start;
    int YUNDONGYIZANTING = R.raw.sport_pause;
    int YUNDONGYIHUIFU = R.raw.sport_continue;
    int FANGSONGYIXIABA = R.raw.exercise_to_relax;
    int LING = R.raw.number_0;
    int YI = R.raw.number_1;
    int ER = R.raw.number_2;
    int SAN = R.raw.number_3;
    int SI = R.raw.number_4;
    int WU = R.raw.number_5;
    int LIU = R.raw.number_6;
    int QI = R.raw.number_7;
    int BA = R.raw.number_8;
    int JIU = R.raw.number_9;
    int SHI = R.raw.number_10;
    int BAI = R.raw.number_100;
    int QIAN = R.raw.number_1000;
    int WAN = R.raw.number_10000;
    int GONGLI = R.raw.kilometer;
    int MI = R.raw.meter;
    int XIAOSHI = R.raw.hour;
    int FENZHONG = R.raw.minute;
    int MIAO = R.raw.second;
    int PAOBU = R.raw.run;
    int NIYIJING = R.raw.you_have_already;
    int YONGSHI = R.raw.spend_time;
    int ZUIJINYIGONGLI = R.raw.nearbyonemile;
    int DIAN = R.raw.dot;
    int YUNDONG = R.raw.sports;
    int GONGLIMEIXIAOSHI = R.raw.km_hour;
    int PINGJUNSUDU = R.raw.averagespeed;
    int DINGDONG = R.raw.dingdong;

    private String getGpsSounudPath() {
        File sharePath;
        String path = mContext.getFilesDir() + File.separator + "sound";
        sharePath = new File(path);
        if (!sharePath.exists()) {
            sharePath.mkdirs();
        }
        return sharePath.getAbsolutePath();
    }

    /**
     * 合成文件
     * @param preFactories 合成的资源
     * @param dst 目标文件地址
     * @throws IOException
     */
    private void mergeSoundFile(List<Integer> preFactories, String dst)
            throws IOException {
        File file = new File(dst);
        if (file.exists()) {
            new File(dst).delete();
        }

        Vector<InputStream> v = new Vector<InputStream>();

        for (Integer source : preFactories) {
            if(source.equals(0)) continue;
            v.add(mContext.getResources().openRawResource(source));
        }
        Enumeration<InputStream> en = v.elements();
        // 将多个流变为一个流
        SequenceInputStream seq = new SequenceInputStream(en);
        FileOutputStream fos = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;
        while ((len = seq.read(buf)) != -1) {
            fos.write(buf, 0, len);
        }
        fos.close();
        seq.close();

    }

}
