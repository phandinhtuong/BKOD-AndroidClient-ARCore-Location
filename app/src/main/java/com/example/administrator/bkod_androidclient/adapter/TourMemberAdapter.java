package com.example.administrator.bkod_androidclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.model.TourMember;

import Manager.OnlineManager;

public class TourMemberAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int tourOrder;
    public TourMemberAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return OnlineManager.getInstance().mTourList.get(tourOrder).getmTourMember().size();
    }

    @Override
    public Object getItem(int position) {
        return OnlineManager.getInstance().mTourList.get(tourOrder).getmTourMember().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getTourOrder() {
        return tourOrder;
    }

    public void setTourOrder(int tourOrder) {
        this.tourOrder = tourOrder;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.tour_member_item_layout, null);
            holder.mName = (TextView) convertView.findViewById(R.id.txt_member_name);
            holder.mFunction = (TextView) convertView.findViewById(R.id.txt_member_function);
            holder.mState = (ImageView) convertView.findViewById(R.id.img_member_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TourMember member = (TourMember) getItem(position);
        // Set ten thanh vien
        holder.mName.setText(member.getUserInfo().getFullName());
        // Set vai tro trong tour
        switch (member.getmFunction()) {
            case 0:
                // Thanh vien
                holder.mFunction.setText(R.string.tour_function_member);
                break;
            case 1:
                // Truong doan
                holder.mFunction.setText(R.string.tour_function_leader);
                break;
            case 2:
                // Pho doan
                holder.mFunction.setText(R.string.tour_function_vice_leader);
                break;
            case 3:
                // Phu huynh
                holder.mFunction.setText(R.string.tour_function_parent);
                break;
            default:
                // Thanh vien
                holder.mFunction.setText(R.string.tour_function_member);
                break;
        }
        // Set trang thai online
        if (member.getUserInfo().getState() == 0) {
            // Offline
            holder.mState.setImageResource(R.drawable.ico_offline_2);
        } else if (member.getUserInfo().getState() == 1){
            // Online
            holder.mState.setImageResource(R.drawable.ico_online_2);
        } else {
            // Khong lam phien
            holder.mState.setImageResource(R.drawable.ico_idle_2);
        }
        return convertView;
    }

    public class ViewHolder {
        private TextView mName;
        private TextView mFunction;
        private ImageView mState;
    }
}