package com.chengxinping.u_city.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chengxinping.u_city.base.BasePager;
import com.chengxinping.u_city.domain.NewsMenu;
import com.chengxinping.u_city.global.GlobakConstats;
import com.chengxinping.u_city.utils.CacheUtils;
import com.chengxinping.u_city.utils.PrefUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 新闻中心
 * Created by 平瓶平瓶子 on 2016/10/26.
 */

public class NewsCenterPager extends BasePager {
    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //要给帧布局填充对象
        TextView view = new TextView(mActivity);
        view.setText("新闻中心");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        //把对象填充给布局
        flContent.addView(view);

        //修改标题
        tvTitle.setText("新闻中心");

        //显示菜单按钮
        btnMenu.setVisibility(View.VISIBLE);

        //先判断有没有缓存，如果有的话就先加载缓存
        String cache = CacheUtils.getCache(GlobakConstats.CATEGORY_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        //请求服务器获取数据  开源框架XUtils
        getDataFromServer();

    }

    /**
     * 从服务器获取数据
     */

    public void getDataFromServer() {
        RequestParams params = new RequestParams(GlobakConstats.CATEGORY_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                processData(result);

                //缓存
                CacheUtils.setCache(GlobakConstats.CATEGORY_URL, result, mActivity);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mActivity, "网络请求错误", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 解析数据
     */
    private void processData(String json) {
        Gson gson = new Gson();
        NewsMenu data = gson.fromJson(json, NewsMenu.class);
    }
}
