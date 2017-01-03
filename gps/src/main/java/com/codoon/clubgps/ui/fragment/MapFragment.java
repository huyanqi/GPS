package com.codoon.clubgps.ui.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.codoon.clubgps.R;
import com.codoon.clubgps.application.GPSApplication;
import com.codoon.clubgps.bean.GPSPoint;
import com.codoon.clubgps.ui.GPSControllerActivity;
import com.codoon.clubgps.util.AnimUtil;
import com.codoon.clubgps.util.CacheUtil;
import com.codoon.clubgps.util.CommonUtil;
import com.codoon.clubgps.util.Constant;
import com.codoon.clubgps.util.GPSSignal;
import com.codoon.clubgps.util.LogUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Frankie on 2016/12/27.
 */

public class MapFragment extends com.amap.api.maps.MapFragment implements View.OnClickListener, AMap.OnMapLoadedListener {

    private final String TAG = "MapFragment";

    private View rootView;
    private GPSControllerActivity mControllerActivity;
    private AMap mAMap;
    private TextView gpsNoSignalTipsTv;
    private List<GPSPoint> mGPSPoints;//已经绘制过的gps坐标点
    private double mCurrentLat, mCurrentLng;
    private CacheUtil mCacheUtil;
    private Marker myPosintMarker;//我的坐标标记
    private int lineColor;
    private Timer mMapAutoMoveTimer;

    private long lastMoveCenterTime = System.currentTimeMillis();
    private LatLng lastGPSPoint;//最后一次打的点

    /**
     * 打点
     * @param newGPSPoint
     */
    public void onNewPoint(GPSPoint newGPSPoint){
        addNewGPSPoint(newGPSPoint);
        //画路线
        drawLine(newGPSPoint.getLatLng());

        //更新我的位置marker
        markMyPoint(newGPSPoint.getLatLng());

        if(mGPSPoints.size() == 1) {
            //如果是第一次定位成功，移动过去
            moveToMyLocation();
        }
    }

    /**
     * 第一次定位成功，开始跑步
     */
    public void onFirstLocationSuccess(GPSPoint newGPSPoint){
        addNewGPSPoint(newGPSPoint);

        //标记开始位置
        markStartPoint(newGPSPoint.getLatLng());

        //移动地图到我的开始位置
        moveToMyLocation();

        lastGPSPoint = newGPSPoint.getLatLng();

    }

    public void onGPSSignalChanged(GPSSignal gpsSignal){
        gpsNoSignalTipsTv.setVisibility(View.INVISIBLE);
        if(gpsSignal.getSignal() == GPSSignal.Signal.STRONG) {
            //信号强

        }else if(gpsSignal.getSignal() == GPSSignal.Signal.NORMAL) {
            //信号一般

        }else if(gpsSignal.getSignal() == GPSSignal.Signal.WEAK) {
            //信号弱
            gpsNoSignalTipsTv.setVisibility(View.VISIBLE);
            gpsNoSignalTipsTv.setText(R.string.gps_weak_signal_tips);
        }else if(gpsSignal.getSignal() == GPSSignal.Signal.NONE) {
            //无信号
            gpsNoSignalTipsTv.setVisibility(View.VISIBLE);
            gpsNoSignalTipsTv.setText(R.string.gps_no_signal_tips);
        }else if(gpsSignal.getSignal() == GPSSignal.Signal.SEARCHING) {
            //信号搜索中
            gpsNoSignalTipsTv.setVisibility(View.VISIBLE);
            gpsNoSignalTipsTv.setText(R.string.gps_searching_signal_tips);
        }
    }

    /**
     * 更新本地的GPS位置变量
     * @param gpsPoint
     */
    private void addNewGPSPoint(GPSPoint gpsPoint){
        mGPSPoints.add(gpsPoint);
        builder.include(gpsPoint.getLatLng());
        this.mCurrentLat = gpsPoint.getLatitude();
        this.mCurrentLng = gpsPoint.getLongitude();
        mCacheUtil.updateMyCacheLocation(mCurrentLat, mCurrentLng);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mapView = super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ((FrameLayout)rootView.findViewById(R.id.map_fragment_ly)).addView(mapView);
        init();
        return rootView;
    }

    private void init() {
        initMap();
        mControllerActivity = (GPSControllerActivity) getActivity();
        mGPSPoints = new ArrayList<GPSPoint>();
        mCacheUtil = CacheUtil.getInstance();
        screenWidth = CommonUtil.getScreenWidth(GPSApplication.getAppContext());
        screenHeight = CommonUtil.getScreenHeight(GPSApplication.getAppContext());

        gpsNoSignalTipsTv = (TextView) rootView.findViewById(R.id.gps_no_signal_tips);

        rootView.findViewById(R.id.close_btn).setOnClickListener(this);
        rootView.findViewById(R.id.fake_signal_weak).setOnClickListener(this);
        rootView.findViewById(R.id.fake_signal_strong).setOnClickListener(this);
    }

    private void initMap(){
        lineColor = ContextCompat.getColor(GPSApplication.getAppContext(), R.color.gps_line);

        mMapAutoMoveTimer = new Timer();
        mMapAutoMoveTimer.schedule(mMapAutoMoveTask, Constant.MAP_AUTOMOVE_TIME, 1 * 1000);

        mAMap = getMap();
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
        mAMap.setOnMapLoadedListener(this);
        mAMap.getUiSettings().setZoomControlsEnabled(false);//关闭缩放控件
    }

    private TimerTask mMapAutoMoveTask = new TimerTask() {
        @Override
        public void run() {
            if(rootView.getVisibility() == View.VISIBLE){
                if(System.currentTimeMillis() - lastMoveCenterTime > Constant.MAP_AUTOMOVE_TIME){
                    if(!mControllerActivity.isRunning() || mControllerActivity.isSearchingGPS()) return;
                    rootView.post(new Runnable() {
                        @Override
                        public void run() {
                            mapMoveToMyLocation();
                        }
                    });
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close_btn) {
            mControllerActivity.switchFragment();
        }else if(v.getId() == R.id.fake_signal_weak){
            mControllerActivity.fakeSignal(500);
        }else if(v.getId() == R.id.fake_signal_strong){
            mControllerActivity.fakeSignal(10);
        }
    }

    /**
     * 动画效果相关
     */
    public int centerX = -1;
    public int centerY = 0;
    private int screenWidth;
    private int screenHeight;
    public void setCenter(int[] center, int widgetWidth, int widgetHeight) {
        this.centerX = center[0] + widgetWidth / 2;
        this.centerY = center[1];
    }

    public void showOrHide(final boolean isShow) {
        int finalRadius = Math.max(rootView.getWidth(), rootView.getHeight());
        if (rootView.getWidth() == 0) {
            finalRadius = Math.max(screenWidth, screenHeight);
        }
        finalRadius *= 1.2;
        AnimUtil.reveal(isShow, rootView, finalRadius, centerX, centerY);
    }

    @Override
    public void onMapLoaded() {
        mapMoveToMyLocation();
    }

    /**
     * 标记我的当前位置
     */
    private void markMyPoint(LatLng myLatLng){
        if(myPosintMarker == null){
            MarkerOptions markerOption = new MarkerOptions();
            //markerOption.position(myLatLng);
            markerOption.anchor(0.5f, 0.5f);
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_curposition));
            myPosintMarker = mAMap.addMarker(markerOption);
        }
        myPosintMarker.setPosition(myLatLng);
    }

    /**
     * 标记开始位置
     */
    private void markStartPoint(LatLng latlng){
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latlng);
        markerOption.anchor(0.5f, 0.5f);
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_start));
        mAMap.addMarker(markerOption);
    }

    private void drawLine(LatLng endPoint){
        LogUtil.i(TAG, "画线:"+lastGPSPoint+"-"+endPoint);
        Polyline polyline = mAMap.addPolyline(new PolylineOptions().add(lastGPSPoint).add(endPoint).geodesic(true).width(16).color(lineColor));
        //如果打点异常，放弃本次打点
        if(polyline == null) return;
        //mAMap.addCircle(new CircleOptions().center(endPoint).radius(mAMap.getScalePerPixel() * 16).fillColor(lineColor).strokeColor(Color.argb(1,0,0,0)));
        lastGPSPoint = endPoint;
    }

    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

    /**
     * 将地图移动到我的位置
     */
    private void mapMoveToMyLocation(){
        boolean isDefaultMove;//是否为默认的移动(第一次进入地图，需要加载中心点为上次缓存的位置)
        if(mCurrentLat == 0 || mCurrentLng == 0){
            LatLng myCachePoint = mCacheUtil.getMyCacheLocation();
            mCurrentLat = myCachePoint.latitude;
            mCurrentLng = myCachePoint.longitude;
            isDefaultMove = true;
        }else{
            isDefaultMove = false;
        }

        if(isDefaultMove){
            //第一次进入地图，需要移动地图到上次缓存位置
            moveToMyLocation();
        }else{
            lastMoveCenterTime = System.currentTimeMillis();
            moveToCenter();
        }

    }

    /**
     * 将我的位置移动到地图中央
     */
    private void moveToMyLocation(){
        LatLng myLatLng = new LatLng(mCurrentLat, mCurrentLng);
        CameraPosition myPosition = new CameraPosition(myLatLng, 17, 0, 0);
        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition), 500, null);
        markMyPoint(myLatLng);
    }

    /**
     * 显示我的高精度值的位置，不做任何逻辑操作
     */
    public void onlyShowNoLogic(LatLng myLatLng){
        CameraPosition myPosition = new CameraPosition(myLatLng, 17, 0, 0);
        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition), 500, null);
        markMyPoint(myLatLng);
    }

    /**
     * 将我的路线移动到地图中央
     */
    private void moveToCenter(){
        if(mGPSPoints.size() < 2) return;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), screenWidth, screenHeight,CommonUtil.dip2px(this.getActivity(),60));
        mAMap.animateCamera(cameraUpdate, 500, null);
    }

    @Override
    public void onDestroy() {

        mMapAutoMoveTask.cancel();
        mMapAutoMoveTimer.cancel();

        Logger.i("退出地图，一共打点 "+ mGPSPoints.size()+" 个");
        super.onDestroy();
    }

}
