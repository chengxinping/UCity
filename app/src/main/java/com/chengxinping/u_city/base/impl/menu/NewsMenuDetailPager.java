package com.chengxinping.u_city.base.impl.menu;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.chengxinping.u_city.R;
import com.chengxinping.u_city.activities.MainActivity;
import com.chengxinping.u_city.base.BaseMenuDetailPager;
import com.chengxinping.u_city.bean.NewsMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

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

    @ViewInject(R.id.indicator)
    private TabLayout mIndicator;

    @ViewInject(R.id.btn_next)
    private ImageButton btnNext;

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
        mIndicator.setupWithViewPager(mViewPager);
        mIndicator.setTabMode(TabLayout.MODE_SCROLLABLE);

        //设置页面滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    //开启侧边栏
                    setSlidingMenuEnable(true);
                } else {
                    //关闭侧边栏
                    setSlidingMenuEnable(false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mViewPager.getCurrentItem();
                currentItem++;
                mViewPager.setCurrentItem(currentItem);
            }
        });

    }

    /**
     * 开启或禁用侧边栏
     *
     * @param enable
     */
    public void setSlidingMenuEnable(boolean enable) {
        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
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

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabData.get(position).title;
        }
    }
}
