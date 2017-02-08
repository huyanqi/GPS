package com.codoon.clubgps.ui.fragment.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codoon.clubgps.R;
import com.codoon.clubgps.ui.GPSControllerActivity;
import com.codoon.clubgps.util.AnimUtil;
import com.codoon.clubgps.util.GPSSignal;

/**
 * Created by Frankie on 2016/12/27.
 */

public class ControllerFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private GPSControllerActivity mControllerActivity;
    private ImageView openMapIv;
    private ImageView gpsSignalIv;
    private TextView gpsSignalTv;

    private ImageView pauseIv;
    private ImageView resumeIv;
    private ImageView stopIv;

    private TextView distanceTv;
    private TextView durationTv;
    private TextView avgPaceTv;

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
    public void updateRunData(String distance, String avgPace){
        distanceTv.setText(distance);
        avgPaceTv.setText(avgPace);
    }

    /**
     * 更新运动持续时间
     */
    public void updateRunDuration(String runTime){
        durationTv.setText(runTime);
    }

    /**
     * 第一次定位成功，开始跑步
     */
    public void onFirstLocationSuccess() {

    }

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

    public void sportPause(){
        AnimUtil.alpha(pauseIv, true);
        AnimUtil.alpha(resumeIv, false);
        AnimUtil.alpha(stopIv, false);
        mControllerActivity.sportPause();
    }

    public void sportResume() {
        AnimUtil.alpha(pauseIv, false);
        AnimUtil.alpha(resumeIv, true);
        AnimUtil.alpha(stopIv, true);
        mControllerActivity.sportResume(false);
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
        super.onDestroy();
    }

}
