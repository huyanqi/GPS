package com.codoon.clubgps.ui;

import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.codoon.clubgps.R;
import com.codoon.clubgps.bean.GPSPoint;
import com.codoon.clubgps.bean.HistoryCount;
import com.codoon.clubgps.bean.HistoryDetail;
import com.codoon.clubgps.service.GPSService;
import com.codoon.clubgps.ui.fragment.controller.ControllerFragment;
import com.codoon.clubgps.ui.fragment.controller.MapFragment;
import com.codoon.clubgps.util.CommonUtil;
import com.codoon.clubgps.util.Constant;
import com.codoon.clubgps.util.GPSSignal;
import com.codoon.clubgps.util.PermissionUtils;
import com.codoon.clubgps.util.ToastUtil;
import com.codoon.clubgps.util.VoicePlayer;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Frankie on 2016/12/27.
 */

public class GPSControllerActivity extends AppCompatActivity implements GPSService.OnGPSLocationChangedListener {

    private final String TAG = "GPSControllerActivity";

    public MapFragment mMapFragment;
    public ControllerFragment mControllerFragment;
    private boolean mapIsShowing = true;

    private VoicePlayer mVoicePlayer;

    /**
     * 路线相关
     */
    private List<GPSPoint> mGPSPoints;//已经绘制过的gps坐标点
    private int savePointPer = 10;//每几个点保存一次数据库

    /**
     * 跑步时间相关
     */
    private Timer mRunTimeTimer;
    private TimerTask mRunTimeTask;
    private int runTime;//跑步耗时

    /**
     * 跑步距离相关
     */
    private double runDistance = 0;//跑步距离

    /**
     * 配速相关
     */
    private long avgPace = 0;//当前平均配速

    /**
     * 权限相关
     */
    private PermissionUtils permissionUtils;

    private GPSService mGPSService;

    @Override
    public void onGPSLocationChanged(GPSPoint newGpsPoint) {
        //1.添加新的点到集合里，准备下次保存
        mGPSPoints.add(newGpsPoint);
        //2.检查本次是否需要保存数据到数据库
        checkSave();
        //3.更新地图
        mMapFragment.onNewPoint(newGpsPoint);
        //4.更新controller
        runningDuration(newGpsPoint);

        mVoicePlayer.updateLength(runDistance);
    }

    @Override
    public void onFirstGPSLocation(GPSPoint newGPSPoint) {
        mGPSPoints.add(newGPSPoint);
        mMapFragment.onFirstLocationSuccess(newGPSPoint);
        mControllerFragment.onFirstLocationSuccess();
    }

    /**
     * 跑步正在持续
     */
    private void runningDuration(GPSPoint newGPSPoint) {
        runDistance += newGPSPoint.getDistance();
        mControllerFragment.updateRunData(CommonUtil.format2(runDistance / 1000),
                CommonUtil.getPaceTimeStr(
                        (avgPace = (avgPace + newGPSPoint.getPace()) / 2)
                ));
    }

    /**
     * 运动暂停
     */
    public void sportPause() {
        if (mGPSService == null) return;
        mVoicePlayer.runPause();
        mGPSService.sportPause();
    }

    /**
     * 运动继续
     * @param isFirst true:开始运动 false:继续运动
     */
    public void sportResume(boolean isFirst) {
        if (mGPSService == null) return;//有可能service还没绑定成功，MapFragment就已经开始加载完成了
        if(isFirst){
            mVoicePlayer.runStart();
        }else{
            mVoicePlayer.runResume();
        }
        mGPSService.sportResume();
        startRunTimer();
    }

    /**
     * 运动结束
     */
    public void sportStop() {
        //先做跳转判断,如果运动距离太短，直接退出当前界面，否则跳转进运动预览界面
        int count = DataSupport.count(GPSPoint.class);
        if (runDistance >= 10) {
            //已有运动记录产生
            //先保存还未保存的点
            mVoicePlayer.runFinish();
            savePoints2DB(mGPSPoints);
            mGPSPoints.clear();
            //跳转
            save(runTime, avgPace);
            //GPSPreviewActivity.start(GPSControllerActivity.this, 1024, runDistance, runTime, avgPace);
        } else {
            //没有足够的运动记录
            ToastUtil.showToast(R.string.toast_distance_too_short);
            finish();
        }
    }

    public void writePoints(){
        mGPSService.writePoints();
    }

    private void bindGPSService() {
        Intent gpsServiceIntent = new Intent(this, GPSService.class);
        bindService(gpsServiceIntent, gpsServiceConnection, BIND_AUTO_CREATE);
    }

    private void init() {
        mGPSPoints = new ArrayList<GPSPoint>();
        initFragment();
    }

    /**
     * 地图已经加载后，再绑定service
     * 原因是service绑定成功以后，会检查当前是否是继续完成未完成运动
     * 如果检查出来需要继续完成上次运动，则马上要在地图上绘制之前路线，避免地图未加载，就开始画线
     */
    public void onMapLoaded() {
        bindGPSService();
    }

    @Override
    public void onAccuracyChanged(GPSSignal gpsSignal) {
        mMapFragment.onGPSSignalChanged(gpsSignal);
        mControllerFragment.onGPSSignalChanged(gpsSignal);
    }

    @Override
    public void onlyShowNoLogic(LatLng latLng) {
        mMapFragment.onlyShowNoLogic(latLng);
    }

    private ServiceConnection gpsServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGPSService = ((GPSService.GPSBinder) service).getGPSService();
            mGPSService.setOnGPSLocationChangedListener(GPSControllerActivity.this);
            mGPSService.initGPSLocation();//初始化GPS定位

            mVoicePlayer = VoicePlayer.getInstance();

            boolean isContinue = checkIsContinue();
            if(isContinue){
                //如果是继续运动，需要先暂停，让用户自己判断继续还是结束
                mControllerFragment.sportPause();
                switchFragment();
            }else{
                //重新开始的运动，需要在MapFragment显示倒计时，然后切换回ControllerFragment
                mGPSService.startLocation();
                mMapFragment.startCountDown();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpscontroller);
        permissionUtils = new PermissionUtils(this);
        if (!permissionUtils.checkPermissions(Constant.mustPermissions)) {
            //没有授权
            permissionUtils.requestPermissions(Constant.mustPermissions, 1024);
        } else {
            //已授权
            init();
        }
    }

    private void initFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction = fragmentTransaction.replace(R.id.controller_fragment, mControllerFragment = new ControllerFragment());
        fragmentTransaction = fragmentTransaction.replace(R.id.map_fragment, mMapFragment = new MapFragment());
        fragmentTransaction.commit();
    }

    /**
     * 切换布局
     */
    public void switchFragment() {
        mapIsShowing = !mapIsShowing;
        mMapFragment.showOrHide(mapIsShowing);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024) {
            permissionResult();
        }
    }

    private void permissionResult() {
        if (permissionUtils.checkPermissions(Constant.mustPermissions)) {
            // Permission Granted
            init();
        } else {
            permissionUtils.showTips(1025);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1025) {
            permissionResult();
        } else if (requestCode == 1024) {
            //从预览界面回来的
            if (resultCode == RESULT_OK) {
                finish();
            } else if(resultCode == RESULT_CANCELED) {
                //继续运动
                mControllerFragment.sportResume();
            }
        }

    }

    public boolean isRunning() {
        if (mGPSService == null) return false;
        return mGPSService.isRunning();
    }

    public boolean isSearchingGPS() {
        if (mGPSService == null) return false;
        return mGPSService.isSearchingGPS();
    }

    @Override
    protected void onDestroy() {
        if(mGPSService != null){
            unbindService(gpsServiceConnection);
            mGPSService.stopSelf();
        }
        closeTimers();
        super.onDestroy();
    }

    private void closeTimers() {
        if (mRunTimeTimer != null) {
            mRunTimeTimer.cancel();
        }
        if (mRunTimeTask != null) {
            mRunTimeTask.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        switchFragment();
    }

    /**
     * 检查是否需要保存到数据库
     */
    private void checkSave() {
        if (mGPSPoints.size() % savePointPer != 0) return;
        List<GPSPoint> savePoints = new ArrayList<>(mGPSPoints);
        mGPSPoints.clear();
        savePoints2DB(savePoints);
    }

    /**
     * 保存列表对象到数据库
     */
    private void savePoints2DB(List<GPSPoint> savePoints) {
        DataSupport.saveAll(savePoints);
    }

    /**
     * 检查是否是继续跑步
     */
    private boolean checkIsContinue() {
        if (DataSupport.count(GPSPoint.class) == 0) return false;
        //是未完成的跑步

        //1.取出上次有效的路线
        List<GPSPoint> gpsPointsList = GPSPoint.getValidDatas();
        int size = gpsPointsList.size();
        //2.初始化跑步时间、距离、配速
        for (GPSPoint gpsPoint : gpsPointsList) {
            runDistance += gpsPoint.getDistance();
            avgPace += gpsPoint.getPace();
        }
        //3.批量打点
        GPSPoint firstPoint = gpsPointsList.get(0);
        firstPoint.setLatLng(new LatLng(firstPoint.getLatitude(), firstPoint.getLongitude()));
        onFirstGPSLocation(firstPoint);
        mMapFragment.drawAllLines(gpsPointsList);

        //4.设置最后一个点到service
        mGPSService.setLastGPSPoint(gpsPointsList.get(size - 1));

        //5.恢复上次运动数据
        runTime = (int) ((gpsPointsList.get(size - 1).getTimestamp() - gpsPointsList.get(0).getTimestamp()) / 1000);
        mControllerFragment.updateRunDuration(CommonUtil.getPeriodTime(runTime));

        avgPace = avgPace / size;
        mControllerFragment.updateRunData(CommonUtil.format2(runDistance / 1000), CommonUtil.getPaceTimeStr(avgPace));

        return true;
    }

    /**
     * 开始记录跑步时间
     */
    private void startRunTimer() {
        closeTimers();

        mRunTimeTask = new TimerTask() {
            @Override
            public void run() {
                if (isRunning()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            runTime++;
                            mVoicePlayer.updateDuration(runTime);
                            mControllerFragment.updateRunDuration(CommonUtil.getPeriodTime(runTime));
                        }
                    });
                }
            }
        };

        mRunTimeTimer = new Timer();
        mRunTimeTimer.schedule(mRunTimeTask, 1000, 1000);
    }

    public void fakeSignal(int accuracy) {
        mGPSService.fakeSignal(accuracy);
    }

    private void save(final int runTime, final long avgPace){
        //1.获取地图截图
        mMapFragment.getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {

                HistoryDetail recordDetail = new HistoryDetail().build(bitmap, runTime, avgPace, GPSPoint.getValidDatas());
                //保存运动记录
                recordDetail.save();

                //保存路线点
                DataSupport.saveAll(recordDetail.getRecordGPSPointList());

                //添加到统计
                HistoryCount.add2Count(recordDetail);

                //清空临时存点的数据库
                DataSupport.deleteAll(GPSPoint.class);

                //跳转到历史记录列表
                Intent intent = new Intent(GPSControllerActivity.this, HistoryListActivity.class);
                startActivity(intent);

                finish();

            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int i) {

            }
        });

    }

}
