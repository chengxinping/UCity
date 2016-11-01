package com.chengxinping.u_city.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不允许滑动的ViewPager
 * Created by 平瓶平瓶子 on 2016/10/26.
 */

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        //重写此方法，触摸时什么都不做，从而实现滑动事件的禁用
        return true;
    }
}
