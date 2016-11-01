package com.chengxinping.u_city;

import android.app.Application;

import org.xutils.x;

/**
 * Created by 平瓶平瓶子 on 2016/11/1.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
