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

    private VoicePlayer (){
        mContext = GPSApplication.getContext();
        soundPath = getGpsSounudPath();
        if (!(new File(soundPath).exists())) {
            new File(soundPath).mkdir();
        }
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

    private void playSounds(int... sounds){
        List<Integer> sourdsList = new ArrayList<>();
        for(int souds : sounds){
            sourdsList.add(souds);
        }
        playSounds(sourdsList);
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
            v.add(mContext.getResources().openRawResource(source));
        }
        Enumeration<InputStream> en = v.elements();
        // 将多个流变为一个流
        SequenceInputStream seq = new SequenceInputStream(en);
        FileOutputStream fos = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;
        while ((len = seq.read(buf)) != -1) {
            fos.write(buf);
        }
        fos.close();
        seq.close();

    }

}
