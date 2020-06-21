package com.example.administrator.bkod_androidclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.model.Conversation;

import Manager.OnlineManager;

public class ConversationsAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public ConversationsAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (OnlineManager.getInstance().mConversationList == null) {
            return 0;
        }
        return OnlineManager.getInstance().mConversationList.size();
    }

    @Override
    public Object getItem(int position) {
        return OnlineManager.getInstance().mConversationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.conversation_item_layout, null);
            holder.mMemberFullname = (TextView) convertView.findViewById(R.id.txt_member_fullname);
            holder.mLastMessage = (TextView) convertView.findViewById(R.id.txt_last_message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Conversation event = (Conversation) getItem(position);
        // Set ten nguoi nhan tin
        holder.mMemberFullname.setText(event.getPartnerName());
        // Set tin nhan cuoi
        if (event.getMemberMessages() != null && event.getMemberMessages().size() > 0) {
            holder.mLastMessage.setText(event.getMemberMessages().get(event.getMemberMessages().size() - 1).getContent());
        } else {
            holder.mLastMessage.setText("");
        }
        return convertView;
    }

    public class ViewHolder {
        private TextView mMemberFullname;
        private TextView mLastMessage;
    }
}