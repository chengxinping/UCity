package com.chengxinping.u_city.base;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页基类
 * Created by 平瓶平瓶子 on 2016/11/2.
 */

public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;  //菜单详情页根布局

    public BaseMenuDetailPager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    public void initData() {
    }

    public abstract View initView();


}
