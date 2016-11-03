package com.chengxinping.u_city.base.impl.menu;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chengxinping.u_city.R;
import com.chengxinping.u_city.base.BaseMenuDetailPager;
import com.chengxinping.u_city.domain.NewsMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 新闻菜单详情页
 * Created by 平瓶平瓶子 on 2016/11/2.
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager {

    @ViewInject(R.id.vp_news_menu_detail)
    private ViewPager mViewPager;
    private ArrayList<NewsMenu.NewsTabData> mTabData;  //页签网络数据
    private ArrayList<TabDetailPager> mPagers; //页签集合


    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        mTabData = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        x.view().inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        //初始化页签
        mPagers = new ArrayList<TabDetailPager>();
        for (int i = 0; i < mTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, mTabData.get(i));
            mPagers.add(pager);
        }

        mViewPager.setAdapter(new NewsMenuDetailAdapter());
    }

    class NewsMenuDetailAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            TabDetailPager pager = mPagers.get(position);
            View view = pager.mRootView;
            container.addView(view);
            pager.initData();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}