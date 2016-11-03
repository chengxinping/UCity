package com.chengxinping.u_city.base.impl.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chengxinping.u_city.base.BaseMenuDetailPager;
import com.chengxinping.u_city.domain.NewsMenu;

/**
 * 页签详情页
 * Created by 平瓶平瓶子 on 2016/11/3.
 */

public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenu.NewsTabData mTabData; //单个页签的网络数据
    private TextView view;


    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
    }

    @Override
    public View initView() {
        view = new TextView(mActivity);

        //view.setText(mTabData.title);  //此处会空指针异常

        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    public void initData() {
        view.setText(mTabData.title);
    }
}
