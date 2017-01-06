package com.codoon.clubgps.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.codoon.clubgps.R;
import com.codoon.clubgps.application.GPSApplication;
import com.codoon.clubgps.bean.GPSPoint;
import com.codoon.clubgps.bean.RecordDetail;
import com.codoon.clubgps.util.CommonUtil;
import com.codoon.clubgps.util.DialogUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frankie on 2017/1/4.
 *
 * 运动预览，此时运动记录还没停止
 */

public class GPSPreviewActivity extends FragmentActivity implements View.OnClickListener, AMap.OnMapLoadedListener {

    public static void start(Activity activity, int requestCode, double distance, int runTime, long avgPace){
        Intent intent = new Intent(activity, GPSPreviewActivity.class);
        intent.putExtra("distance", distance);
        intent.putExtra("runTime", runTime);
        intent.putExtra("avgPace", avgPace);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 初始化地图
     */
    private void initMap(){
        mBuilder = new LatLngBounds.Builder();

        UiSettings uiSettings = mAMap.getUiSettings();
        //1.禁止所有触摸操作
        uiSettings.setAllGesturesEnabled(false);
        //2.关闭缩放控件
        uiSettings.setZoomControlsEnabled(false);
        //3.隐藏logo
        uiSettings.setLogoBottomMargin(-50);
        //4.获取本地跑步所有路线点
        gpsPointsList = GPSPoint.getValidDatas();
        List<LatLng> latLngs = new ArrayList<LatLng>();
        LatLng latLng;
        for(GPSPoint gpsPoint : gpsPointsList){
            latLng = new LatLng(gpsPoint.getLatitude(), gpsPoint.getLongitude());
            mBuilder.include(latLng);
            latLngs.add(latLng);
        }

        //画起点
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLngs.get(0));
        markerOption.anchor(0.5f, 0.5f);
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_start));
        mAMap.addMarker(markerOption);

        //将路线画到地图上
        mAMap.addPolyline(new PolylineOptions().addAll(latLngs).geodesic(true).width(16).color(ContextCompat.getColor(GPSApplication.getAppContext(), R.color.gps_line)));
        //4.移动地图
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(mBuilder.build(),
                CommonUtil.getScreenWidth(GPSApplication.getAppContext()),
                CommonUtil.dip2px(this, 180),//CommonUtil.getScreenHeight(GPSApplication.getAppContext()
                CommonUtil.dip2px(this, 15));
        mAMap.animateCamera(cameraUpdate, 500, null);
    }

    private MapView mMapView;
    private AMap mAMap;
    private LatLngBounds.Builder mBuilder;
    private List<GPSPoint> gpsPointsList;
    private TextView distanceTv;
    private TextView durationTv;
    private TextView avgPaceTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpspreview);
        mMapView = ((MapView) findViewById(R.id.map_view));
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mAMap.setOnMapLoadedListener(this);
        init();
        showData();
    }

    private void init(){
        distanceTv = (TextView) findViewById(R.id.distance_tv);
        durationTv = (TextView) findViewById(R.id.duration_tv);
        avgPaceTv = (TextView) findViewById(R.id.avg_pace_tv);

        findViewById(R.id.resume_view).setOnClickListener(this);
        findViewById(R.id.show_hide_iv).setOnClickListener(this);
        findViewById(R.id.delete_view).setOnClickListener(this);
        findViewById(R.id.complete_btn).setOnClickListener(this);
    }

    private void showData(){
        Intent intent = getIntent();
        distanceTv.setText(CommonUtil.format2(intent.getDoubleExtra("distance", 0) / 1000));
        durationTv.setText(CommonUtil.getPeriodTime(intent.getIntExtra("runTime", 0)));
        avgPaceTv.setText(CommonUtil.getPaceTimeStr(intent.getLongExtra("avgPace", 0)));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.resume_view){//恢复运动
            setResult(RESULT_CANCELED);
            finish();
        } else if(v.getId() == R.id.show_hide_iv){//隐藏/显示地图
            mAMap.setMapType( mAMap.getMapType() == AMap.MAP_TYPE_NIGHT ? AMap.MAP_TYPE_NORMAL : AMap.MAP_TYPE_NIGHT);
        } else if(v.getId() == R.id.delete_view){//删除运动记录
            deleteRun();
        } else if(v.getId() == R.id.complete_btn){//运动完成
            complete();
        }

    }

    private void deleteRun(){
        new DialogUtil(this).createAlertDialog(getString(R.string.alert_run_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //放弃记录
                //1.从数据库清空数据
                DataSupport.deleteAll(GPSPoint.class);
                //2.通知上一个界面退出,并退出当前界面
                setResult(RESULT_OK);
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        }).show();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onMapLoaded() {
        initMap();
    }

    private void complete(){
        if(gpsPointsList == null) return;//如果地图还没加载完就点击了按钮，则无效

        //生成路线缩略图
        mAMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {

            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int i) {
                RecordDetail recordDetail = new RecordDetail().build(bitmap, gpsPointsList);
                //保存运动记录
                recordDetail.save();

                //保存路线点
                DataSupport.saveAll(recordDetail.getRecordGPSPointList());

                //清空数据库
                DataSupport.deleteAll(GPSPoint.class);
                //通知上一个界面退出,并退出当前界面
                setResult(RESULT_OK);
                finish();
            }
        });


    }

}
