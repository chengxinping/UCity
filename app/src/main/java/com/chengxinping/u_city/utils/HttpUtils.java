package com.chengxinping.u_city.utils;

import com.chengxinping.u_city.bean.ChatMessage;
import com.chengxinping.u_city.bean.Result;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by 平瓶平瓶子 on 2016/5/26.
 */
public class HttpUtils {
    private static final String URL = "http://www.tuling123.com/openapi/api";
    private static final String APIKEY = "ca6b8a84bd0780d59174b18adafa78f0";


//    public static ChatMessage doPost(String msg) {
//        final ChatMessage chatMessage = new ChatMessage();
//        String url = setParmas(msg);
//        x.http().get(new RequestParams(url), new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Gson gson = new Gson();
//                Result r = gson.fromJson(result, Result.class);
//                if (r.getCode() == 100000)
//                    chatMessage.setMsg(r.getText());
//                if (r.getCode() == 200000) {
//                    String string = r.getUrl().toString();
//                    chatMessage.setMsg(r.getText() + "\r\n" + string);
//                }
//                if (r.getCode() == 302000) {
//                    String s = "你想看新闻了吗？反正就是不告你今天的新闻！";
//                    chatMessage.setMsg(s);
//                }
//                if (r.getCode() == 308000) {
//                    chatMessage.setMsg("你要学做菜啊？好厉害啊！");
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                chatMessage.setMsg("你烦死了，我不想理你了");
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//        chatMessage.setDate(new Date());
//        chatMessage.setType(ChatMessage.Type.INCOMING);
//        return chatMessage;
//    }
//
//    private static String setParmas(String msg) {
//        String url = null;
//        try {
//            url = URL + "?key=" + APIKEY + "&info=" + URLEncoder.encode(msg, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return url;
//    }

    public static ChatMessage sendMessage(String msg) {
        ChatMessage chatMessage = new ChatMessage();
        String jsonRes = doPost(msg);
        Gson gson = new Gson();
        try {
            Result result = gson.fromJson(jsonRes, Result.class);
            if (result.getCode() == 100000) {
                chatMessage.setMsg(result.getText());
            }
            if (result.getCode() == 200000) {
                String string = result.getUrl().toString();
                chatMessage.setMsg(result.getText() + "\r\n" + string);
            }
            if (result.getCode() == 302000) {
                String s = "你想看新闻了吗？反正就是不告你今天的新闻！";
                chatMessage.setMsg(s);
            }
            if (result.getCode() == 308000) {
                chatMessage.setMsg("你要学做菜啊？好厉害啊！");
            }
        } catch (Exception e) {
            chatMessage.setMsg("你烦死了，我不想理你了");
        }
        chatMessage.setDate(new Date());
        chatMessage.setType(ChatMessage.Type.INCOMING);
        return chatMessage;
    }

    public static String doPost(String msg) {
        String result = "";
        String url = setParmas(msg);
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            java.net.URL urlNet = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlNet.openConnection();
            connection.setReadTimeout(5 * 1000);
            connection.setConnectTimeout(5 * 1000);
            connection.setRequestMethod("POST");
            is = connection.getInputStream();
            int len = -1;
            byte[] buf = new byte[128];
            baos = new ByteArrayOutputStream();
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            baos.flush();
            result = new String(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    private static String setParmas(String msg) {
        String url = null;
        try {
            url = URL + "?key=" + APIKEY + "&info=" + URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
