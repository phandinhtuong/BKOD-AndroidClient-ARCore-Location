package com.example.administrator.bkod_androidclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.model.Conversation;
import com.example.administrator.bkod_androidclient.model.Counselor;

import Manager.ConversationManager;
import Manager.OnlineManager;

public class CounselorsAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public CounselorsAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (OnlineManager.getInstance().mCounselorsList == null) {
            return 0;
        }
        return OnlineManager.getInstance().mCounselorsList.size();
    }

    @Override
    public Object getItem(int position) {
        return OnlineManager.getInstance().mCounselorsList.get(position);
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
            convertView = mLayoutInflater.inflate(R.layout.counselor_item_layout, null);
            holder.mCounselorFullname = (TextView) convertView.findViewById(R.id.txt_counselor_fullname);
            holder.mLastMessage = (TextView) convertView.findViewById(R.id.txt_last_message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Lay tu van vien o vi tri hien tai
        Counselor counselor = (Counselor) getItem(position);
        // Tim doan hoi thoai cua tu van vien nay
        int eventOrder = ConversationManager.findConversationOrderByMemberIdAndName(counselor.getUserId(), counselor.getFullname());
        Conversation event = OnlineManager.getInstance().mConversationList.get(eventOrder);
        // Set ten tu van vien
        holder.mCounselorFullname.setText(event.getPartnerName());
        // Set tin nhan cuoi
        if (event.getMemberMessages() != null && event.getMemberMessages().size() > 0) {
            holder.mLastMessage.setText(event.getMemberMessages().get(event.getMemberMessages().size() - 1).getContent());
        } else {
            holder.mLastMessage.setText("");
        }
        return convertView;
    }

    public class ViewHolder {
        private TextView mCounselorFullname;
        private TextView mLastMessage;
    }
}