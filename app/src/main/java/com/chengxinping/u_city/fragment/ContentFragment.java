package com.chengxinping.u_city.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.chengxinping.u_city.R;
import com.chengxinping.u_city.base.BasePager;
import com.chengxinping.u_city.base.impl.HomePager;
import com.chengxinping.u_city.base.impl.NewsCenterPager;
import com.chengxinping.u_city.base.impl.SettingPager;
import com.chengxinping.u_city.base.impl.SmartServicePager;
import com.chengxinping.u_city.view.NoScrollViewPager;

import java.util.ArrayList;

/**
 * 主页面Fragment
 * Created by 平瓶平瓶子 on 2016/10/26.
 */

public class ContentFragment extends BaseFragment {
    private NoScrollViewPager mViewPager;
    private ArrayList<BasePager> mPagers; //五个标签页的集合
    private RadioGroup rgGroup;

    @Override
    public void initData() {
        mPagers = new ArrayList<BasePager>();

        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new ContentAdapter());

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //底栏标签切换监听
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        mViewPager.setCurrentItem(0, false);   //false表示不具有滑动动画
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(3, false);
                        break;
                    default:
                        break;
                }
            }
        });
/**
 * 在这里调用initData节省流量和性能
 */
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager pager = mPagers.get(position);
                pager.initData();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //手动加载第一页数据
        mPagers.get(0).initData();
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }


    class ContentAdapter extends PagerAdapter {
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
            BasePager pager = mPagers.get(position);
            View view = pager.mRootView; // 获取当前页面对象的布局

            //ViewPager默认会加载下一个页面，会消耗流量和性能
            //pager.initData(); //初始化数据
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取新闻中心的页面
     */
    public NewsCenterPager getNewsCenterPager() {
        NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);
        return pager;
    }
}
