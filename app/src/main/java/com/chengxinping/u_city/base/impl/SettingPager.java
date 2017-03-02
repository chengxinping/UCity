package com.chengxinping.u_city.base.impl;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;

import com.chengxinping.u_city.R;
import com.chengxinping.u_city.base.BasePager;
import com.chengxinping.u_city.utils.DataCleanManager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 设置
 * Created by 平瓶平瓶子 on 2016/10/26.
 */

public class SettingPager extends BasePager implements View.OnClickListener {
    @ViewInject(R.id.cv_about)
    CardView about;
    @ViewInject(R.id.cv_clear)
    CardView clear;
    @ViewInject(R.id.cv_share)
    CardView share;
    @ViewInject(R.id.cv_check)
    CardView check;

    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //修改标题
        tvTitle.setText("设置");
        initListener();
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_setting, null);
        x.view().inject(this, view);
        return view;
    }

    private void initListener() {
        about.setOnClickListener(this);
        check.setOnClickListener(this);
        clear.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder;
        switch (v.getId()) {
            case R.id.cv_clear:
                builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("清除缓存")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataCleanManager.cleanApplicationData(mActivity);
                                DataCleanManager.cleanSharedPreference(mActivity);
                                // DataCleanManager.cleanInternalCache(mActivity);
                                mActivity.finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setMessage("是否清楚应用缓存并退出程序？")
                        .show();
                break;
            case R.id.cv_check:
                builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("检查更新")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setMessage("已经是最新版本了！")
                        .show();
                break;
            case R.id.cv_share:
                ShareSDK.initSDK(mActivity);
                OnekeyShare oks = new OnekeyShare();
                oks.disableSSOWhenAuthorize();
                oks.setTitle("智慧城市");
                oks.setTitleUrl("http://119.29.180.219/U-City.apk");
                oks.setSite(mActivity.getString(R.string.app_name));
                oks.setSiteUrl("http://119.29.180.219/U-City.apk");
                oks.show(mActivity);
                break;
            case R.id.cv_about:
                builder = new AlertDialog.Builder(mActivity);
                builder.setMessage("本软件遵从Apache开源协议：Apache Licence 2.0" +
                        "Github地址：https://github.com/chengxinping")
                        .show();
                break;
            default:
                break;
        }
    }
}
