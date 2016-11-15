package com.chengxinping.u_city.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chengxinping.u_city.R;
import com.chengxinping.u_city.base.BaseMenuDetailPager;
import com.chengxinping.u_city.bean.NewsMenu;
import com.chengxinping.u_city.bean.NewsTabBean;
import com.chengxinping.u_city.global.GlobakConstats;
import com.chengxinping.u_city.utils.CacheUtils;
import com.chengxinping.u_city.view.CirclePageIndicator;
import com.chengxinping.u_city.view.TopNewsViewPager;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 页签详情页
 * Created by 平瓶平瓶子 on 2016/11/3.
 */

public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenu.NewsTabData mTabData; //单个页签的网络数据
    private String mUrl;
    private ArrayList<NewsTabBean.TopNews> mTopNews;

    @ViewInject(R.id.vp_top_news)
    private TopNewsViewPager mViewPager;

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;

    @ViewInject(R.id.lv_list)
    private ListView lvList;

    private ArrayList<NewsTabBean.NewsData> mNewsList;
    private NewsAdapter mNewsAdapter;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobakConstats.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        x.view().inject(this, view);

        //给ListView添加头布局
        View mHeaderView = View.inflate(mActivity, R.layout.list_item_header, null);
        x.view().inject(this, mHeaderView);//此处必须头布局注入
        lvList.addHeaderView(mHeaderView);
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

            mIndicator.setViewPager(mViewPager);
            mIndicator.setSnap(true); //快照方式展示

            //事件要设置给Indicator
            mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == 0) {
                        mIndicator.setCurrentItem(0);
                    }
                    //更新头条新闻标题
                    NewsTabBean.TopNews topNews = mTopNews.get(position);
                    tvTitle.setText(topNews.title);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //手动设置第一个头条新闻标题栏（其他在ViewPager切换时才能更新）
            tvTitle.setText(mTopNews.get(0).title);

            mIndicator.onPageSelected(0); // 默认让第一个选中，解决页面销毁后重新初始化时Indicator仍然停留在上次界面的bug
        }

        //设置列表项
        mNewsList = newsTabBean.data.news;
        if (mNewsList != null) {
            mNewsAdapter = new NewsAdapter();
            lvList.setAdapter(mNewsAdapter);
        }


    }

    /**
     * 头条新闻适配器
     */
    class TopNewsAdapter extends PagerAdapter {
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
            ImageOptions options = new ImageOptions.Builder().setLoadingDrawableId(R.drawable.topnews_item_default).build();
            ImageView view = new ImageView(mActivity);
            String url = mTopNews.get(position).topimage; //图片下载连接
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            //下载图片，将它设置给ImageView
            //避免oom
            //缓存 XUtils3
            x.image().bind(view, url, options);
            container.addView(view);
            return view;
        }
    }

    class NewsAdapter extends BaseAdapter {

        private ImageOptions options;

        public NewsAdapter() {
            options = new ImageOptions.Builder().setLoadingDrawableId(R.drawable.news_pic_default).build();
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public NewsTabBean.NewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            NewsTabBean.NewsData news = getItem(position);
            holder.tvTitle.setText(news.title);
            holder.tvDate.setText(news.pubdate);

            x.image().bind(holder.ivIcon, news.listimage, options);

            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvDate;
    }
}
