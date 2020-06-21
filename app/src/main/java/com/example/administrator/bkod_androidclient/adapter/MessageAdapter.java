package com.example.administrator.bkod_androidclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.model.MemberMessage;
import android.widget.LinearLayout.LayoutParams;

import Manager.OnlineManager;

public class MessageAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    // So thu tu hoi thoai
    private int mConversationOrder;
    public MessageAdapter(Context context, int conversationOrder) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mConversationOrder = conversationOrder;
    }

    @Override
    public int getCount() {
        if (OnlineManager.getInstance().mConversationList.get(mConversationOrder).getMemberMessages() == null) {
            return 0;
        }
        return OnlineManager.getInstance().mConversationList.get(mConversationOrder).getMemberMessages().size();
    }

    @Override
    public Object getItem(int position) {
        return OnlineManager.getInstance().mConversationList.get(mConversationOrder).getMemberMessages().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new MessageAdapter.ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.message_item_layout, null);
            holder.mContent = (TextView) convertView.findViewById(R.id.txt_message_content);
            holder.mDate = (TextView) convertView.findViewById(R.id.txt_message_date);
            holder.mItemMessageLayout = (LinearLayout) convertView.findViewById(R.id.item_message_layout);
            convertView.setTag(holder);
        } else {
            holder = (MessageAdapter.ViewHolder) convertView.getTag();
        }
        MemberMessage event = (MemberMessage) getItem(position);
        // Set noi dung tin nhan
        holder.mContent.setText(event.getContent());
        // Set ngay gui tin nhan
        holder.mDate.setText(String.format("%02d:%02d %02d-%02d-%d", event.getDateSent().getHours(), event.getDateSent().getMinutes(), event.getDateSent().getDate(), event.getDateSent().getMonth() + 1, (1900 + event.getDateSent().getYear())));
            // Set margin tuy thuoc vao tin nhan gui hay nhan
            if (event.isSender() == true) {
                // La nguoi gui thi margin left
                setMargins(holder.mItemMessageLayout, 100, 0);
            } else {
                // La nguoi nhan thi margin right
                setMargins(holder.mItemMessageLayout, 0, 100);
            }
        return convertView;
    }

    private void setMargins (LinearLayout view, int left, int right) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        p.setMargins(left, 10, right, 10);
        view.setLayoutParams(p);
    }

    public class ViewHolder {
        private TextView mContent;
        private TextView mDate;
        private LinearLayout mItemMessageLayout;
    }
}