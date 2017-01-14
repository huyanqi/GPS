package com.codoon.clubgps.ui.fragment.history;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.codoon.clubgps.R;
import com.codoon.clubgps.application.GPSApplication;
import com.codoon.clubgps.bean.HistoryDetail;
import com.codoon.clubgps.bean.HistoryGPSPoint;
import com.codoon.clubgps.ui.HistoryDetailActivity;
import com.codoon.clubgps.util.CommonUtil;
import com.codoon.clubgps.util.PaceLevelPicker;

import java.util.ArrayList;
import java.util.List;

public class HistoryDetailFragment extends Fragment implements AMap.OnMapLoadedListener, View.OnClickListener {

    private View rootView;
    private HistoryDetail mHistoryDetail;
    private MapView mMapView;
    private AMap mAMap;
    private LatLngBounds.Builder mBuilder;
    private ViewGroup contentLy;
    private HistoryDetailActivity mActivity;
    private List<HistoryGPSPoint> mHistoryGPSPointList;
    private boolean contentIsShown;

    public HistoryDetailFragment() {
        // Required empty public constructor
    }

    public void setActivity(HistoryDetailActivity activity){
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history_detail, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        init();
        return rootView;
    }

    private void init(){
        contentLy = (ViewGroup) rootView.findViewById(R.id.content_ly);

        mAMap = mMapView.getMap();
        mAMap.setOnMapLoadedListener(this);

        rootView.findViewById(R.id.close_view).setOnClickListener(this);
    }

    private void initMap(){
        mBuilder = new LatLngBounds.Builder();

        UiSettings uiSettings = mAMap.getUiSettings();
        //关闭缩放控件
        uiSettings.setZoomControlsEnabled(false);
        //隐藏logo
        uiSettings.setLogoBottomMargin(-50);
        //4.获取本地跑步所有路线点

        mHistoryGPSPointList = mHistoryDetail.findAllGPSPoints();
        int count = mHistoryGPSPointList.size();
        PaceLevelPicker picker = new PaceLevelPicker(mHistoryDetail.getMax_pace(), count);
        List<Integer> mLineColors = new ArrayList<>();

        List<LatLng> latLngList = new ArrayList<>();
        HistoryGPSPoint gpsPoint;
        for(int i=0;i<count;i++){
            gpsPoint = mHistoryGPSPointList.get(i);
            latLngList.add(new LatLng(gpsPoint.getLatitude(), gpsPoint.getLongitude()));
            mLineColors.add(picker.getColor(gpsPoint.getPace()));
        }

        CommonUtil.drawLineOnMap(latLngList, mAMap, CommonUtil.getScreenWidth(GPSApplication.getAppContext()), CommonUtil.dip2px(180), CommonUtil.dip2px(30), 0, mBuilder, mLineColors);

    }

    public void setData(HistoryDetail historyDetail){
        this.mHistoryDetail = historyDetail;
    }

    @Override
    public void onDestroyView() {
        mMapView.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }
    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        mMapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMapLoaded() {
        initMap();
    }

    public void switchContent(){
        if(!contentLy.isShown()){
            //需要显示
            contentLy.setVisibility(View.VISIBLE);
            //地图模式调成黑色
            mAMap.setMapType(AMap.MAP_TYPE_NIGHT);
            //有一个放大效果
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(mBuilder.build(),
                    CommonUtil.getScreenWidth(GPSApplication.getAppContext()),
                    CommonUtil.dip2px(180), CommonUtil.dip2px(10));
            mAMap.animateCamera(cameraUpdate, 500, null);
            //显示公里标记
            showKmPosition();

        }else{
            //隐藏
            contentLy.setVisibility(View.INVISIBLE);
            //还原地图模式
            mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
            //还原缩放
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(mBuilder.build(),
                    CommonUtil.getScreenWidth(GPSApplication.getAppContext()),
                    CommonUtil.dip2px(180), CommonUtil.dip2px(30));
            mAMap.moveCamera(cameraUpdate);
            //隐藏公里标记
            hideKmPosition();
        }
        contentIsShown = contentLy.isShown();
    }

    public void switchKmPosition(){
        if(markerShown) {
            hideKmPosition();
        }else{
            showKmPosition();
        }
    }

    public void switchMapType(){
        mAMap.setMapType(mAMap.getMapType() == AMap.MAP_TYPE_NIGHT ? AMap.MAP_TYPE_NORMAL : AMap.MAP_TYPE_NIGHT);
    }

    public boolean isContentIsShown() {
        return contentIsShown;
    }

    /**
     * 在地图上显示公里标记
     */
    private List<Marker> kmMarkers;
    private boolean markerShown = false;
    private void showKmPosition() {
        if(markerShown) {
            hideKmPosition();
            return;
        }
        if(kmMarkers == null){
            //没有显示过的数据
            int currentKm = 1;
            kmMarkers = new ArrayList<Marker>();
            Bitmap resIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_milestone).getBitmap();
            for(HistoryGPSPoint historyGPSPoint : mHistoryGPSPointList){
                if(historyGPSPoint.getTotal_length() / 1000 >= currentKm){
                    //新的marker
                    MarkerOptions kmMarkerOption = new MarkerOptions();
                    kmMarkerOption.position(historyGPSPoint.getLatlng());
                    kmMarkerOption.anchor(0.5f, 0.5f);
                    int kmNum = CommonUtil.getKmNumber(historyGPSPoint.getTotal_length() / 1000);
                    kmMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(com.codoon.clubgps.util.BitmapUtil.generatorCountIcon(resIcon, kmNum)));
                    currentKm = kmNum + 1;
                    kmMarkers.add(mAMap.addMarker(kmMarkerOption));
                }
            }
        }else{
            List<Marker> tmpMarkers = new ArrayList<>();
            for(Marker marker : kmMarkers){
                tmpMarkers.add(mAMap.addMarker(marker.getOptions()));
            }
            kmMarkers.clear();
            kmMarkers.addAll(tmpMarkers);
        }
        markerShown = true;
    }

    /**
     * 隐藏公里标记
     */
    private void hideKmPosition(){
        if(!markerShown) return;
        if(kmMarkers == null) return;
        for(Marker marker : kmMarkers){
            marker.remove();
        }
        markerShown = false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.close_view){
            mActivity.switchViewPager();
        }

    }

}
