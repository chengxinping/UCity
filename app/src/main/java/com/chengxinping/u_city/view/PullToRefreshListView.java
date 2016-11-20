package com.chengxinping.u_city.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chengxinping.u_city.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新的自定义ListView
 * Created by 平瓶平瓶子 on 2016/11/16.
 */

public class PullToRefreshListView extends ListView {

    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;

    private int mCurrentState = 1; //当前刷新状态


    private View mHeaderView;
    private int mHeaderViewHeight;
    private int startY = -1;
    private int endY;
    private int dy;

    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;

    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private ProgressBar pbProgress;

    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
    }

    /**
     * 初始化一个头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh, null);
        this.addHeaderView(mHeaderView);
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);

        //隐藏头布局
        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

        initAnim();
        setCurrentTime();

    }

    /**
     * 设置刷新时间
     */
    private void setCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        tvTime.setText(time);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                startY = (int) ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                //避免被其他布局拦截(当在ViewPager里滑动，会被消费掉，导致startY没有赋值)
                if (startY == -1) {
                    startY = (int) ev.getY();
                }

                //如果是正在刷新，跳出
                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }

                endY = (int) ev.getY();
                dy = endY - startY;
                int firstVisiblePosition = getFirstVisiblePosition(); // 第一个item位置

                if (dy > 0 && firstVisiblePosition == 0) {
                    int padding = dy - mHeaderViewHeight;//计算当前控件下拉空间的布局
                    mHeaderView.setPadding(0, padding, 0, 0);

                    if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
                        //改为松开刷新的状态
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
                        //改为下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }

                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:

                startY = -1;

                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    refreshState();

                    //完整展示头布局
                    mHeaderView.setPadding(0, 0, 0, 0);

                    //4.进行回调
                    if (mListener != null) {
                        mListener.OnRefresh();
                    }

                } else if (mCurrentState == STATE_PULL_TO_REFRESH) {
                    //隐藏头布局
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                }

                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 初始化箭头的动画
     */
    private void initAnim() {
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

    }

    /**
     * 根据当前状态刷新页面
     */
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                tvTitle.setText("下拉刷新");
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                ivArrow.startAnimation(animDown);

                break;
            case STATE_RELEASE_TO_REFRESH:
                tvTitle.setText("松开刷新");
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                ivArrow.startAnimation(animUp);

                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");
                ivArrow.clearAnimation();//清楚动画，否则无法隐藏
                pbProgress.setVisibility(View.VISIBLE);
                ivArrow.setVisibility(View.INVISIBLE);

                break;
            default:
                break;
        }
    }

    /**
     * 刷新结束，收起控件
     */
    public void OnRefreshComplete(boolean success) {
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
        mCurrentState = STATE_PULL_TO_REFRESH;
        tvTitle.setText("下拉刷新");
        pbProgress.setVisibility(View.INVISIBLE);
        ivArrow.setVisibility(View.VISIBLE);
        //只有刷新成功才刷新时间
        if (success) {
            setCurrentTime();
        }
    }

    //3.定义成员变量，接收监听对象
    private OnRefreshListener mListener;

    /**
     * 2.暴露接口，设置监听
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;

    }

    /**
     * 1.下拉刷新回调接口
     */
    public interface OnRefreshListener {
        public void OnRefresh();
    }
}
