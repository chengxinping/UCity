package com.chengxinping.u_city.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.chengxinping.u_city.R;
import com.chengxinping.u_city.base.BaseMenuDetailPager;
import com.chengxinping.u_city.bean.NewsMenu;
import com.chengxinping.u_city.bean.NewsTabBean;
import com.chengxinping.u_city.global.GlobakConstats;
import com.chengxinping.u_city.utils.CacheUtils;
import com.chengxinping.u_city.view.TopNewsViewPager;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 页签详情页
 * Created by 平瓶平瓶子 on 2016/11/3.
 */

public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenu.NewsTabData mTabData; //单个页签的网络数据
    private TopNewsViewPager mViewPager;
    private String mUrl;
    private ArrayList<NewsTabBean.TopNews> mTopNews;
    private ImageOptions options;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobakConstats.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        mViewPager = (TopNewsViewPager) view.findViewById(R.id.vp_top_news);
        return view;
    }

    @Override
    public void initData() {
        //缓存
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        //请求服务器
        getDataFromServer();
    }

    public void getDataFromServer() {
        RequestParams params = new RequestParams(mUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result);

                CacheUtils.setCache(mUrl, result, mActivity);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mActivity, "网络请求错误" + mUrl, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);
        //头条新闻填充数据
        mTopNews = newsTabBean.data.topnews;
        if (mTabData != null) {
            mViewPager.setAdapter(new TopNewsAdapter());
        }

    }

    /**
     * 头条新闻适配器
     */
    class TopNewsAdapter extends PagerAdapter {
        public TopNewsAdapter() {
            options = new ImageOptions.Builder().build();
        }

        @Override
        public int getCount() {
            return mTopNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView view = new ImageView(mActivity);
            String url = mTopNews.get(position).topimage; //图片下载连接
            view.setImageResource(R.drawable.topnews_item_default);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            //下载图片，将它设置给ImageView
            //避免oom
            //缓存 XUtils3
            System.out.println(url);
            x.image().bind(view, url);
            container.addView(view);
            return view;
        }
    }
}
