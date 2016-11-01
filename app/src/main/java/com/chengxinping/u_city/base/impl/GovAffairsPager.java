package com.chengxinping.u_city.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chengxinping.u_city.base.BasePager;

/**
 * 政务
 * Created by 平瓶平瓶子 on 2016/10/26.
 */

public class GovAffairsPager extends BasePager {
    public GovAffairsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //要给帧布局填充对象
        TextView view = new TextView(mActivity);
        view.setText("政务");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        //把对象填充给布局
        flContent.addView(view);

        //修改标题
        tvTitle.setText("人口管理");

        //显示菜单按钮
        btnMenu.setVisibility(View.VISIBLE);
    }
}
