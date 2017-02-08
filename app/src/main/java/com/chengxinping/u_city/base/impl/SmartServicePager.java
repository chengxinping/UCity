package com.chengxinping.u_city.base.impl;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.chengxinping.u_city.R;
import com.chengxinping.u_city.base.BasePager;
import com.chengxinping.u_city.bean.ChatMessage;
import com.chengxinping.u_city.bean.ChatMessageAdapter;
import com.chengxinping.u_city.utils.HttpUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 智慧服务
 * Created by 平瓶平瓶子 on 2016/10/26.
 */

public class SmartServicePager extends BasePager {
    @ViewInject(R.id.id_listview_msgs)
    ListView mMsgs;

    @ViewInject(R.id.id_send_msg)
    Button mSendMsg;

    @ViewInject(R.id.id_input_msg)
    EditText mInputMsg;

    private ChatMessageAdapter mAdappter;
    private List<ChatMessage> mDatas;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //等待接收,子线程完成数据返回
            ChatMessage fromMessage = (ChatMessage) msg.obj;
            mDatas.add(fromMessage);
            mAdappter.notifyDataSetChanged();
        }
    };

    public SmartServicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //修改标题
        tvTitle.setText("生活");
        initDates();
        //初始化事件
        initListener();
        mMsgs.setSelection(mMsgs.getCount() - 1);
        initListListener();//长按监听
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_smart_service, null);
        x.view().inject(this, view);
        return view;
    }

    private void initDates() {
        mDatas = new ArrayList<ChatMessage>();
        ChatMessage initMessage = new ChatMessage(new Date(), ChatMessage.Type.INCOMING, "你好，我是大格");
        mDatas.add(initMessage);
        mAdappter = new ChatMessageAdapter(mActivity, mDatas);
        mMsgs.setAdapter(mAdappter);
    }

    private void initListener() {
        mSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String toMsg = mInputMsg.getText().toString();
                if (TextUtils.isEmpty(toMsg)) {
                    Toast.makeText(mActivity, "发送消息不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                ChatMessage toMessage = new ChatMessage();
                toMessage.setDate(new Date());
                toMessage.setMsg(toMsg);
                toMessage.setType(ChatMessage.Type.OUTCOMING);
                mDatas.add(toMessage);
                mAdappter.notifyDataSetChanged();
                mInputMsg.setText("");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        ChatMessage fromMessage = HttpUtils.doPost(toMsg);
                        ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
                        Message m = Message.obtain();
                        m.obj = fromMessage;
                        mHandler.sendMessage(m);
                    }
                }).start();
            }
        });
    }

    private void initListListener() {
        mMsgs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setPositiveButton("复制", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager cpm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                        ChatMessage message = mDatas.get(position);
                        cpm.setText(message.getMsg());
                        Toast.makeText(mActivity, "文本已负责到粘贴版", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("删除聊天记录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatas.removeAll(mDatas);
                        mAdappter.notifyDataSetChanged();
                    }
                });
                builder.show();
                return false;
            }
        });
    }
}
