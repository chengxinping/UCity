package com.chengxinping.u_city.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chengxinping.u_city.activities.MainActivity;
import com.chengxinping.u_city.base.BaseMenuDetailPager;
import com.chengxinping.u_city.base.BasePager;
import com.chengxinping.u_city.base.impl.menu.InteractMenuDetailPager;
import com.chengxinping.u_city.base.impl.menu.NewsMenuDetailPager;
import com.chengxinping.u_city.base.impl.menu.PhotosMenuDetailPager;
import com.chengxinping.u_city.base.impl.menu.TopicMenuDetailPager;
import com.chengxinping.u_city.domain.NewsMenu;
import com.chengxinping.u_city.fragment.LeftMenuFragment;
import com.chengxinping.u_city.global.GlobakConstats;
import com.chengxinping.u_city.utils.CacheUtils;
import com.chengxinping.u_city.utils.PrefUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 新闻中心
 * Created by 平瓶平瓶子 on 2016/10/26.
 */

public class NewsCenterPager extends BasePager {

    public ArrayList<BaseMenuDetailPager> mMenuDetailPagers; //菜单详情集合
    private NewsMenu mNewsData;  //分类信息网络数据

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {

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
        mNewsData = gson.fromJson(json, NewsMenu.class);

        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment fragment = mainUI.getLeftMenuFragment();
        //给侧边栏设置数据
        fragment.setMenuData(mNewsData.data);

        //初始化四个菜单详情页
        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));

        //切换到新闻菜单详情页
        setCurrentDetailPager(0);

    }

    /**
     * 设置菜单详情页
     *
     * @param position
     */
    public void setCurrentDetailPager(int position) {
        //重新给FrameLayout添加内容
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);//获取当前应该显示的页面
        View view = pager.mRootView; //当前页面布局
        //清除之前旧的布局
        flContent.removeAllViews();

        flContent.addView(view); //给帧布局添加新的布局

        //初始化页面数据
        pager.initData();

        //更新标题
        tvTitle.setText(mNewsData.data.get(position).title);

    }
}
