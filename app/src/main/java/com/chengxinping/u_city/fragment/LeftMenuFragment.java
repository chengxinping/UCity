package com.chengxinping.u_city.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chengxinping.u_city.R;
import com.chengxinping.u_city.activities.MainActivity;
import com.chengxinping.u_city.base.impl.NewsCenterPager;
import com.chengxinping.u_city.bean.NewsMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * Created by 平瓶平瓶子 on 2016/10/26.
 */

public class LeftMenuFragment extends BaseFragment {

    private ListView lvList;
    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuData;  //侧边栏网络数据对象

    private int mCurrentPos = 0; // 当前被选中的item位置
    private LeftMenuAdapter mAdapter;

    @Override
    public void initData() {

    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        lvList = (ListView) view.findViewById(R.id.lv_list);
        return view;
    }

    /**
     * 给侧边栏设置数据
     *
     * @param data
     */
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data) {
        mCurrentPos = 0; //重新更新数据时将listView的的item选择位置更新为0
        mNewsMenuData = data;
        //更新页面
        mAdapter = new LeftMenuAdapter();
        lvList.setAdapter(mAdapter);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                mAdapter.notifyDataSetChanged();

                //收起侧边栏
                toggle();

                //侧边栏点击后要修改新闻中心FrameLayout中的内容
                setCurrentDetailPager(position);
            }

        });

    }

    /**
     * 设置当前菜单详情页
     *
     * @param position
     */
    private void setCurrentDetailPager(int position) {
        //获取新闻中心的对象
        MainActivity mainUi = (MainActivity) mActivity;
        ContentFragment fragment = mainUi.getContentFragment();
        NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();
        //修改新闻中心的FrameLayout的布局
        newsCenterPager.setCurrentDetailPager(position);

    }

    /**
     * 打开或关闭侧边栏
     */
    private void toggle() {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle(); //如果当前状态是开，调用之后就关 反之亦然；

    }

    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return mNewsMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(400, 300);
            view.setLayoutParams(params);
            TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
            NewsMenu.NewsMenuData item = getItem(position);
            tvMenu.setText(item.title);
            if (mCurrentPos == position) {
                //被选中的布局
                tvMenu.setEnabled(true);
            } else {
                tvMenu.setEnabled(false);
            }

            return view;
        }
    }
}
