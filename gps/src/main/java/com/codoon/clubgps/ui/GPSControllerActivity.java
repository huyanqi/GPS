package com.codoon.clubgps.ui;

import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.maps.model.LatLng;
import com.codoon.clubgps.R;
import com.codoon.clubgps.bean.GPSPoint;
import com.codoon.clubgps.service.GPSService;
import com.codoon.clubgps.ui.fragment.ControllerFragment;
import com.codoon.clubgps.ui.fragment.MapFragment;
import com.codoon.clubgps.util.Constant;
import com.codoon.clubgps.util.DialogUtil;
import com.codoon.clubgps.util.GPSSignal;
import com.codoon.clubgps.util.PermissionUtils;

/**
 * Created by Frankie on 2016/12/27.
 */

public class GPSControllerActivity extends AppCompatActivity implements GPSService.OnGPSLocationChangedListener {

    private final String TAG = "GPSControllerActivity";

    public MapFragment mMapFragment;
    public ControllerFragment mControllerFragment;
    private boolean mapIsShowing = true;

    /**
     * 权限相关
     */
    private PermissionUtils permissionUtils;

    private GPSService mGPSService;

    private void bindGPSService(){
        Intent gpsServiceIntent = new Intent(this, GPSService.class);
        bindService(gpsServiceIntent, gpsServiceConnection, BIND_AUTO_CREATE);
    }

    private void init(Bundle savedInstanceState) {
        initFragment(savedInstanceState);
        bindGPSService();
    }

    /**
     * 运动暂停
     */
    public void sportPause(){
        if(mGPSService == null) return;
        mGPSService.sportPause();
    }

    /**
     * 运动继续
     */
    public void sportResume(){
        if(mGPSService == null) return;//有可能service还没绑定成功，MapFragment就已经开始加载完成了
        mGPSService.sportResume();
    }

    /**
     * 运动结束
     */
    public void sportStop(){
        new DialogUtil(this).createAlertDialog(getString(R.string.alert_run_finish), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        }).show();
    }

    @Override
    public void onGPSLocationChanged(GPSPoint newGpsPoint) {
        mMapFragment.onNewPoint(newGpsPoint);
        mControllerFragment.updateSportData(newGpsPoint);
    }

    @Override
    public void onFirstGPSLocation(GPSPoint newGPSPoint) {
        mMapFragment.onFirstLocationSuccess(newGPSPoint);
        mControllerFragment.onFirstLocationSuccess();
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
            mGPSService = ((GPSService.GPSBinder)service).getGPSService();
            mGPSService.setOnGPSLocationChangedListener(GPSControllerActivity.this);
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
        if(!permissionUtils.checkPermissions(Constant.mustPermissions)){
            //没有授权
            permissionUtils.requestPermissions(Constant.mustPermissions, 1024);
        }else{
            //已授权
            init(savedInstanceState);
        }
    }

    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction = fragmentTransaction.add(R.id.controller_fragment, mControllerFragment = new ControllerFragment());
        fragmentTransaction = fragmentTransaction.add(R.id.map_fragment, mMapFragment = new MapFragment());
        fragmentTransaction.commit();
    }

    /**
     * 切换布局
     */
    public void switchFragment() {
        mMapFragment.showOrHide(!mapIsShowing);
        mapIsShowing = !mapIsShowing;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024) {
            permissionResult();
        }
    }

    private void permissionResult(){
        if(permissionUtils.checkPermissions(Constant.mustPermissions)){
            // Permission Granted
            init(null);
        }else{
            permissionUtils.showTips(1025);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1025){
            permissionResult();
        }
    }

    public boolean isRunning(){
        if(mGPSService == null) return false;
        return mGPSService.isRunning();
    }

    public boolean isSearchingGPS(){
        if(mGPSService == null) return false;
        return mGPSService.isSearchingGPS();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(gpsServiceConnection);
        mGPSService.stopSelf();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        switchFragment();
    }

    public void fakeSignal(int accuracy) {
        mGPSService.fakeSignal(accuracy);
    }

}
