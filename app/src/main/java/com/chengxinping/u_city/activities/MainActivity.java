package com.chengxinping.u_city.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.chengxinping.u_city.R;
import com.chengxinping.u_city.fragment.ContentFragment;
import com.chengxinping.u_city.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    private final static String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private final static String TAG_CONTENT = "TAG_CONTENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); // 全屏触摸
        slidingMenu.setBehindOffset(600);  //屏幕预留600

        initFragment();
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//开启事物
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
        transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);
        transaction.commit(); //提交事物
    }

    /**
     * 获取侧边栏fragment对象
     *
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {

        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);//根据标记找到侧边栏
        return fragment;
    }

    /**
     * 获取侧边栏fragment对象
     *
     * @return
     */
    public ContentFragment getContentFragment() {

        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);//根据标记找到侧边栏
        return fragment;
    }
}
