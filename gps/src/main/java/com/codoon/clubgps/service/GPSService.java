package com.codoon.clubgps.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.codoon.clubgps.application.GPSApplication;
import com.codoon.clubgps.bean.GPSPoint;
import com.codoon.clubgps.util.GPSSignal;
import com.codoon.clubgps.util.LogUtil;
import com.orhanobut.logger.Logger;

/**
 * Created by Frankie on 2016/12/28.
 *
 * GPS服务
 *
 * 所有的GPS位置获取都从这里
 */

public class GPSService extends Service implements AMapLocationListener {

    private final String TAG = "GPSService";

    private GPSBinder mGPSBinder;
    private AMapLocationClient mMapLocationClient;
    private OnGPSLocationChangedListener mOnGPSLocationChangedListener;

    private boolean isRunning = true;//是否正在跑步中
    private boolean isSearchingGPS = true;

    private GPSPoint lastGPSPoint;//记录最后一个GPS坐标点

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) return;
        if (mOnGPSLocationChangedListener == null) return;
        if (aMapLocation.getErrorCode() == 0) {
            //定位成功
            LogUtil.i(TAG, "获取到新的GPS坐标点:" + aMapLocation.getLatitude() + "," + aMapLocation.getLongitude() + ",精度:" + aMapLocation.getAccuracy());
            mOnGPSLocationChangedListener.onAccuracyChanged(new GPSSignal(aMapLocation.getAccuracy()));
            GPSPoint gpsPoint = isValid(aMapLocation);

            if (gpsPoint == null) return;//无效的点

            LogUtil.i(TAG, "有效的点:" + gpsPoint + ",lastgpspoint:" + lastGPSPoint);

            if(!isRunning){
                LogUtil.i(TAG, "运动暂停中.不记录,但是只在界面中显示");
                mOnGPSLocationChangedListener.onlyShowNoLogic(gpsPoint);
                return;
            }

            if (lastGPSPoint == null) {
                mOnGPSLocationChangedListener.onFirstGPSLocation(gpsPoint);
            } else {
                mOnGPSLocationChangedListener.onGPSLocationChanged(gpsPoint);
            }
            lastGPSPoint = gpsPoint;
        } else {
            LogUtil.i(TAG, "定位失败,错误码" + aMapLocation.getErrorCode() + ":" + aMapLocation.getErrorInfo());
        }

    }

    public void sportPause(){
        isRunning = false;

    }

    public void sportResume(){
        isRunning = true;

    }

    private GPSPoint isValid(AMapLocation aMapLocation) {
        GPSPoint gpsPoint = new GPSPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude(), lastGPSPoint, isRunning);

        float accuracy = aMapLocation.getAccuracy();//获取定位精度，单位:m
        if (accuracy > 200) {
            LogUtil.i(TAG, "放弃本次坐标点,精度太低,精度:" + accuracy + "m");
            isSearchingGPS = true;
            if(mOnGPSLocationChangedListener != null){
                mOnGPSLocationChangedListener.onlyShowNoLogic(gpsPoint);
            }
            return null;
        }

        isSearchingGPS = false;

        if (lastGPSPoint != null && gpsPoint.getDistance() < 1) {
            LogUtil.i(TAG, "放弃本次坐标点,距离太短,距离:" + gpsPoint.getDistance() + "m");
            return null;
        }

        if (lastGPSPoint != null && (gpsPoint.getLatitude() == lastGPSPoint.getLatitude() && gpsPoint.getLongitude() == lastGPSPoint.getLongitude())) {
            LogUtil.i(TAG, "放弃本次坐标点,重复点");
            return null;
        }

        gpsPoint.setIndex(gpsPoint.getIndex()+1);//更新点的索引
        return gpsPoint;
    }

    public class GPSBinder extends Binder {
        public GPSService getGPSService() {
            return GPSService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mGPSBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGPSBinder = new GPSBinder();
        startGPSLocation();
    }

    /**
     * 启动定位
     */
    private void startGPSLocation() {

        /*启动高德定位*/
        mMapLocationClient = new AMapLocationClient(GPSApplication.getAppContext());
        mMapLocationClient.setLocationListener(this);
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        /*高精度定位,会同时使用网络定位和GPS定位，优先返回最高精度的定位结果*/
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setInterval(5000);//每2s定位一次，系统默认就是2s
        locationOption.setNeedAddress(false);//不需要返回地址信息
        locationOption.setMockEnable(true);//允许模拟位置，可以调用第三方工具模拟GPS
        mMapLocationClient.setLocationOption(locationOption);
        mMapLocationClient.startLocation();

        //startFake();//开启模拟定位
    }

    @Override
    public void onDestroy() {
        Logger.i("销毁GPS定位服务");
        mMapLocationClient.stopLocation();
        mMapLocationClient.unRegisterLocationListener(this);
        mMapLocationClient.onDestroy();
        if(fakeThread != null && fakeThread.isAlive()){
            fakeThread.interrupt();
        }
        super.onDestroy();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isSearchingGPS() {
        return isSearchingGPS;
    }

    public interface OnGPSLocationChangedListener {
        void onGPSLocationChanged(GPSPoint newGpsPoint);
        void onFirstGPSLocation(GPSPoint newGPSPoint);//第一次定位成功
        void onAccuracyChanged(GPSSignal gpsSignal);//当gps信号发生变化
        void onlyShowNoLogic(GPSPoint gpsPoint);//显示我的低精度位置
    }

    public void setOnGPSLocationChangedListener(OnGPSLocationChangedListener onGPSLocationChangedListener) {
        mOnGPSLocationChangedListener = onGPSLocationChangedListener;
    }

    //假点
    private Thread fakeThread;
    private boolean run;
    private int i = 0;
    private Handler fakeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null && msg.obj instanceof Location) {
                onLocationChanged((AMapLocation) msg.obj);
            }
        }
    };

    /**
     * 模拟gps数据
     */
    private void startFake() {
        fakeThread = new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    double la = 0;
                    double ln = 0;
                    Message a;
                    AMapLocation location;
                    Thread.sleep(1000);
                    run = true;
                    while (run) {
                        //Logger.d("假点 "+i);
                        if(i%30 == 0 && i > 0){
                            Thread.sleep(30 * 1000);
                        }
                        la = i * 0.00020;
                        ln = i * 0.00022;
                        a = Message.obtain();
                        location = new AMapLocation(new Location(LocationManager.GPS_PROVIDER));
                        location.setLatitude(30.5272112352 + la);
                        if((i/5) % 2 == 0){
                            location.setLongitude(104.0687161241 + ln);
                        }else{
                            location.setLongitude(104.0687161241 - ln);
                        }
                        a.obj = location;
                        Thread.sleep(2000);
                        fakeHandler.sendMessage(a);
                        i++;
                    }
                } catch (Exception e) {
                    run = false;
                }

            }
        };
        fakeThread.start();
    }

}
