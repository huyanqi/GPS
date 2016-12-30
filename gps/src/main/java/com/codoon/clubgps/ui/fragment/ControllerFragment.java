package com.codoon.clubgps.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codoon.clubgps.R;
import com.codoon.clubgps.bean.GPSPoint;
import com.codoon.clubgps.ui.GPSControllerActivity;
import com.codoon.clubgps.util.AnimUtil;
import com.codoon.clubgps.util.CommonUtil;
import com.codoon.clubgps.util.GPSSignal;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Frankie on 2016/12/27.
 */

public class ControllerFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private GPSControllerActivity mControllerActivity;
    private ImageView openMapIv;
    private ImageView gpsSignalIv;
    private TextView gpsSignalTv;
    private Timer mRunTimeTimer;

    private ImageView pauseIv;
    private ImageView resumeIv;
    private ImageView stopIv;

    private TextView distanceTv;
    private TextView durationTv;
    private TextView avgPaceTv;

    private int runTime = 0;//跑步耗时
    private double runDistance = 0;//跑步距离
    private long avgPace = 0;//当前平均配速

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_controller, container, false);
        init();
        sportResume();
        return rootView;
    }

    private void init(){
        mControllerActivity = (GPSControllerActivity) getActivity();
        openMapIv = (ImageView) rootView.findViewById(R.id.open_map_iv);
        gpsSignalIv = (ImageView) rootView.findViewById(R.id.gps_sinal_iv);
        gpsSignalTv = (TextView) rootView.findViewById(R.id.gps_sinal_tv);

        pauseIv = (ImageView) rootView.findViewById(R.id.pause_iv);
        resumeIv = (ImageView) rootView.findViewById(R.id.resume_iv);
        stopIv = (ImageView) rootView.findViewById(R.id.stop_iv);

        distanceTv = (TextView) rootView.findViewById(R.id.distance_tv);
        durationTv = (TextView) rootView.findViewById(R.id.duration_tv);
        avgPaceTv = (TextView) rootView.findViewById(R.id.avg_pace_tv);

        openMapIv.setOnClickListener(this);
        pauseIv.setOnClickListener(this);
        resumeIv.setOnClickListener(this);
        stopIv.setOnClickListener(this);
    }

    /**
     * 更新运动数据
     */
    public void updateSportData(GPSPoint gpsPoint){
        runDistance += gpsPoint.getDistance();
        distanceTv.setText(CommonUtil.format2(runDistance / 1000));
        avgPaceTv.setText(CommonUtil.getPaceTimeStr( (avgPace += gpsPoint.getPace()) / 2 ));//(当前平均配速+本次配速) /2
    }

    /**
     * 第一次定位成功，开始跑步
     */
    public void onFirstLocationSuccess() {
        initTimer();
    }

    private void initTimer(){
        mRunTimeTimer = new Timer();
        mRunTimeTimer.schedule(mRunTimeTask, 1000, 1000);
    }

    private TimerTask mRunTimeTask = new TimerTask() {
        @Override
        public void run() {
            if(mControllerActivity.isRunning()){
                rootView.post(new Runnable() {
                    @Override
                    public void run() {
                        runTime ++;
                        durationTv.setText(CommonUtil.getPeriodTime(runTime));
                    }
                });
            }
        }
    };

    @Override
    public void onClick(View v) {
        if(v.getId() == openMapIv.getId()){
            openMap();
        }else if(v.getId() == pauseIv.getId()){
            sportPause();
        }else if(v.getId() == resumeIv.getId()){
            sportResume();
        }else if(v.getId() == stopIv.getId()){
            sportStop();
        }
    }

    private void sportPause(){
        AnimUtil.alpha(pauseIv, true);
        AnimUtil.alpha(resumeIv, false);
        AnimUtil.alpha(stopIv, false);
        mControllerActivity.sportPause();
    }

    private void sportResume() {
        AnimUtil.alpha(pauseIv, false);
        AnimUtil.alpha(resumeIv, true);
        AnimUtil.alpha(stopIv, true);
        mControllerActivity.sportResume();
    }

    private void sportStop(){
        mControllerActivity.sportStop();
    }

    private void openMap(){
        if(mControllerActivity.mMapFragment.centerX == -1){
            //第一次切换过去
            int location[] = new int[2];
            openMapIv.getLocationOnScreen(location);
            mControllerActivity.mMapFragment.setCenter(location, openMapIv.getMeasuredWidth(), openMapIv.getMeasuredHeight());
        }
        mControllerActivity.switchFragment();
    }

    /**
     * 信号强度更新
     */
    public void onGPSSignalChanged(GPSSignal gpsSignal){
        if(gpsSignal.getSignal() == GPSSignal.Signal.STRONG) {
            //信号强
            gpsSignalIv.setImageResource(R.mipmap.ic_gps_strong);
            gpsSignalTv.setVisibility(View.INVISIBLE);
        }else if(gpsSignal.getSignal() == GPSSignal.Signal.NORMAL) {
            //信号一般
            gpsSignalIv.setImageResource(R.mipmap.ic_gps_normal);
            gpsSignalTv.setVisibility(View.VISIBLE);
            gpsSignalTv.setTextColor(0xFFffb53f);
            gpsSignalTv.setText(R.string.gps_sinal_normal);
        }else if(gpsSignal.getSignal() == GPSSignal.Signal.WEAK) {
            //信号弱
            gpsSignalIv.setImageResource(R.mipmap.ic_gps_weak);
            gpsSignalTv.setVisibility(View.VISIBLE);
            gpsSignalTv.setTextColor(0xFFff4141);
            gpsSignalTv.setText(R.string.gps_sinal_weak);
        }else if(gpsSignal.getSignal() == GPSSignal.Signal.NONE) {
            //无信号
            gpsSignalIv.setImageResource(R.mipmap.ic_gps_lost);
            gpsSignalTv.setVisibility(View.VISIBLE);
            gpsSignalTv.setTextColor(0xFFb1b1b1);
            gpsSignalTv.setText(R.string.gps_sinal_none);
        }else if(gpsSignal.getSignal() == GPSSignal.Signal.SEARCHING) {
            //信号搜索中
            gpsSignalIv.setImageResource(R.mipmap.ic_gps_lost);
            gpsSignalTv.setVisibility(View.VISIBLE);
            gpsSignalTv.setTextColor(0xFFb1b1b1);
            gpsSignalTv.setText(R.string.gps_sinal_searching);
        }
    }

    @Override
    public void onDestroy() {
        if(mRunTimeTimer != null) {
            mRunTimeTimer.cancel();
            mRunTimeTimer = null;
        }
        if(mRunTimeTask != null){
            mRunTimeTask.cancel();
            mRunTimeTask = null;
        }
        super.onDestroy();
    }

}
