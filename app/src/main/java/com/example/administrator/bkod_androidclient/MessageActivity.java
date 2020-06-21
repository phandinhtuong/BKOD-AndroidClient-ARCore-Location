package com.example.administrator.bkod_androidclient;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.administrator.bkod_androidclient.adapter.MessageAdapter;
import com.example.administrator.bkod_androidclient.model.Conversation;

import org.json.JSONException;
import org.json.JSONObject;

import Manager.ActivityManager;
import Manager.OnlineManager;

public class MessageActivity extends BaseActivity {
    // So thu tu hoi thoai
    private int conversationOrder;
    // Message adapter
    private MessageAdapter messageAdapter;
    // Doan hoi thoai hien tai
    private Conversation currentConversation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lien ket layout voi activity
        ActivityManager.getInstance().activityMessageBinding = DataBindingUtil.setContentView(this, R.layout.activity_message);
        // Lay so thu tu hoi thoai
        Intent i = getIntent();
        conversationOrder = i.getIntExtra("ConversationOrder", 0);
        // Gan doan hoi thoai hien tai
        currentConversation = OnlineManager.getInstance().mConversationList.get(conversationOrder);
        // Thay doi tieu de
        ActivityManager.getInstance().activityMessageBinding.actionBar.actionBarTitle.setText(currentConversation.getPartnerName());
        // Gan message adapter
        messageAdapter = new MessageAdapter(this, conversationOrder);
        // Gan su kien cho nut dang nhap
        ActivityManager.getInstance().activityMessageBinding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Neu bam nut gui tin nhan thi gui tin nhan len server
                sendMessage();
            }
        });
        // Set danh sach tin nhan
        setMessagesListView();
        // Tat progress
        messageProgressOff ();
    }

    // TODO: Set danh sach tin nhan
    public void setMessagesListView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Gan message adapter
                ActivityManager.getInstance().activityMessageBinding.listview.setAdapter(messageAdapter);
                // Cuon xuong duoi cung
                ActivityManager.getInstance().activityMessageBinding.listview.post(new Runnable(){
                    public void run() {
                        ActivityManager.getInstance().activityMessageBinding.listview.setSelection(messageAdapter.getCount());
                    }});
            }
        });
    }

    // TODO: Refresh danh sach tin nhan
    public void refreshMessagesListView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Gan message adapter
                messageAdapter.notifyDataSetChanged();
                // Cuon xuong duoi cung
                ActivityManager.getInstance().activityMessageBinding.listview.post(new Runnable(){
                    public void run() {
                        ActivityManager.getInstance().activityMessageBinding.listview.smoothScrollToPosition(messageAdapter.getCount());
                    }});
            }
        });
    }

    // Gui tin nhan
    private void sendMessage() {
        // Kiem tra tin nhan co hop le khong
        String content = ActivityManager.getInstance().activityMessageBinding.inputMessage.getText().toString();
        if (content.indexOf("'") != -1) {
            // Thay the ky tu ' bang ky tu "
            content = content.replaceAll("'", "\"");
        }
        // Neu khong nhap gi thi thoat khoi ham
        if (content.matches("\\s*")) {
            return;
        }
        // Soan message tin nhan gui len server
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("COMMAND", "MEMBER_MESSAGE");
            jsonObject.put("SENDER_NAME", OnlineManager.getInstance().userInfo.getFullName());
            jsonObject.put("RECIEVER_ID", currentConversation.getPartnerId());
            jsonObject.put("RECIEVER_NAME", currentConversation.getPartnerName());
            jsonObject.put("CONTENT", content);
            // Gui message len server
            OnlineManager.getInstance().sendMessage(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Xoa het text trong khung nhap tin nhan di
        ActivityManager.getInstance().activityMessageBinding.inputMessage.setText("");
    }

    //TODO: Bat tour progress
    public void messageProgressOn (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ActivityManager.getInstance().activityMessageBinding.messageProgress.getVisibility() != View.VISIBLE) {
                    ActivityManager.getInstance().activityMessageBinding.messageProgress.setVisibility(View.VISIBLE);
                }
                if (ActivityManager.getInstance().activityMessageBinding.messageLayout.getVisibility() != View.GONE) {
                    ActivityManager.getInstance().activityMessageBinding.messageLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    //TODO: Tat tour progress
    public void messageProgressOff (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ActivityManager.getInstance().activityMessageBinding.messageProgress.getVisibility() != View.GONE) {
                    ActivityManager.getInstance().activityMessageBinding.messageProgress.setVisibility(View.GONE);
                }
                if (ActivityManager.getInstance().activityMessageBinding.messageLayout.getVisibility() != View.VISIBLE) {
                    ActivityManager.getInstance().activityMessageBinding.messageLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public int getConversationOrder() {
        return conversationOrder;
    }
}
