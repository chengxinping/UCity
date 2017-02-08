package com.chengxinping.u_city.base.impl;

import android.app.Activity;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.chengxinping.u_city.R;
import com.chengxinping.u_city.base.BasePager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 首页
 * Created by 平瓶平瓶子 on 2016/10/26.
 */

public class CityMapPager extends BasePager {
    public LocationClient mLocationClient;
    private boolean isFirstLocate = true;
    public BaiduMap mBaiduMap;

    @ViewInject(R.id.baidu_map_view)
    public TextureMapView mMapView;

    public CityMapPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        SDKInitializer.initialize(mActivity.getApplicationContext());
        mLocationClient = new LocationClient(mActivity.getApplicationContext());
        View view = View.inflate(mActivity, R.layout.fragment_city_map, null);
        x.view().inject(this, view);
        mLocationClient.registerLocationListener(new MyLocationListener());
        mLocationClient.start();
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        return view;
    }

    @Override
    public void initData() {

    }

    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            mBaiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder builder = new MyLocationData.Builder();
        MyLocationData myLocationData = builder.latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
        mBaiduMap.setMyLocationData(myLocationData);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(bdLocation);
            }
        }
    }
}
