package com.chengxinping.u_city.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chengxinping.u_city.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 五个标签页的基类
 * Created by 平瓶平瓶子 on 2016/10/26.
 */

public class BasePager {

    public Activity mActivity;
    @ViewInject(R.id.tv_title)
    public TextView tvTitle;
    @ViewInject(R.id.fl_content)
    public FrameLayout flContent;

    public View mRootView; //当前页面的布局对象

    public BasePager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    //初始化布局
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        x.view().inject(this, view);
        return view;
    }


    //初始化数据
    public void initData() {

    }

}
