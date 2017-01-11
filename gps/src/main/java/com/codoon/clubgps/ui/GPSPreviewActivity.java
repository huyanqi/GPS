package com.codoon.clubgps.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.codoon.clubgps.R;
import com.codoon.clubgps.application.GPSApplication;
import com.codoon.clubgps.bean.GPSPoint;
import com.codoon.clubgps.bean.HistoryCount;
import com.codoon.clubgps.bean.HistoryDetail;
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
        List<LatLng> latLngList = new ArrayList<>();
        for(GPSPoint gpsPoint : gpsPointsList){
            latLngList.add(new LatLng(gpsPoint.getLatitude(), gpsPoint.getLongitude()));
        }

        CommonUtil.drawLineOnMap(latLngList, mAMap, CommonUtil.getScreenWidth(GPSApplication.getAppContext()), CommonUtil.dip2px(180), CommonUtil.dip2px(30), 500, mBuilder);
    }

    private MapView mMapView;
    private AMap mAMap;
    private LatLngBounds.Builder mBuilder;
    private List<GPSPoint> gpsPointsList;
    private TextView distanceTv;
    private TextView durationTv;
    private TextView avgPaceTv;

    private Intent intent;

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
        intent = getIntent();
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
                HistoryDetail recordDetail = new HistoryDetail().build(bitmap, intent.getIntExtra("runTime", 0), intent.getLongExtra("avgPace", 0), gpsPointsList);
                //保存运动记录
                recordDetail.save();

                //保存路线点
                DataSupport.saveAll(recordDetail.getRecordGPSPointList());

                //添加到统计
                HistoryCount.add2Count(recordDetail);

                //清空临时存点的数据库
                DataSupport.deleteAll(GPSPoint.class);
                //通知上一个界面退出,并退出当前界面
                setResult(RESULT_OK);
                finish();
            }
        });


    }

}
