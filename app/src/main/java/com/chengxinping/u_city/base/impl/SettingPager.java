package com.chengxinping.u_city.base.impl;

import android.app.Activity;
import android.view.View;

import com.chengxinping.u_city.R;
import com.chengxinping.u_city.base.BasePager;

import org.xutils.x;

/**
 * 设置
 * Created by 平瓶平瓶子 on 2016/10/26.
 */

public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //修改标题
        tvTitle.setText("设置");
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_setting, null);
        x.view().inject(this,view);
        return view;
    }
}
