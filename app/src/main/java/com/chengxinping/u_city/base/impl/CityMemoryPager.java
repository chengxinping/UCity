package com.chengxinping.u_city.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.chengxinping.u_city.base.BasePager;

/**
 * 首页
 * Created by 平瓶平瓶子 on 2016/10/26.
 */

public class CityMemoryPager extends BasePager {

    public CityMemoryPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //要给帧布局填充对象
        TextView view = new TextView(mActivity);
        view.setText("城市记忆");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        //把对象填充给布局
        flContent.addView(view);

        //修改标题
        tvTitle.setText("城市记忆");

    }
}
