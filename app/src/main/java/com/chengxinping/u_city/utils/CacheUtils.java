package com.chengxinping.u_city.utils;

import android.content.Context;

/**
 * 缓存工具类
 * 已URL为key json为value 保存在本地
 * Created by 平瓶平瓶子 on 2016/11/1.
 */

public class CacheUtils {
    /**
     * 缓存
     *
     * @param url
     * @param json
     * @param ctx
     */
    public static void setCache(String url, String json, Context ctx) {
        PrefUtils.setString(ctx, url, json);

    }

    /**
     * 获取缓存
     *
     * @param url
     * @param ctx
     */
    public static String getCache(String url, Context ctx) {
        return PrefUtils.getString(ctx, url, null);
    }
}
