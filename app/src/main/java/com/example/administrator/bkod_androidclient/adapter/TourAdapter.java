package com.example.administrator.bkod_androidclient.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.model.Tour;
import com.squareup.picasso.Picasso;

import Manager.ActivityManager;
import Manager.OnlineManager;

public class TourAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public TourAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (OnlineManager.getInstance().mTourList == null) {
            return 0;
        }
        return OnlineManager.getInstance().mTourList.size();
    }

    @Override
    public Object getItem(int position) {
        return OnlineManager.getInstance().mTourList.get(position);
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
            convertView = mLayoutInflater.inflate(R.layout.tour_item_layout, null);
            holder.mLogo = (ImageView) convertView.findViewById(R.id.img_tour_logo);
            holder.mName = (TextView) convertView.findViewById(R.id.txt_tour_title);
            holder.mInfo = (TextView) convertView.findViewById(R.id.txt_tour_info);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Tour event = (Tour) getItem(position);
        // Set anh minh hoa cho tour
        Picasso.get().load(event.getmImageUrl()).resize(256, 256).centerInside().into(holder.mLogo);
        // Set ten tour
        holder.mName.setText(event.getmName());
        // Gan ngay dien ra tour
        String tourInfo = String.format("%02d/%02d/%d - ", event.getmDate().getDate(), event.getmDate().getMonth() + 1, (1900 + event.getmDate().getYear()));
        // Gan vai tro trong tour
        switch (event.getmFunction()) {
            case 0:
                // Thanh vien
                tourInfo += ActivityManager.getInstance().getCurrentActivity().getString(R.string.tour_function_member);
                break;
            case 1:
                // Truong doan
                tourInfo += ActivityManager.getInstance().getCurrentActivity().getString(R.string.tour_function_leader);
                break;
            case 2:
                // Pho doan
                tourInfo += ActivityManager.getInstance().getCurrentActivity().getString(R.string.tour_function_vice_leader);
                break;
            case 3:
                // Phu huynh
                tourInfo += ActivityManager.getInstance().getCurrentActivity().getString(R.string.tour_function_parent);
                break;
            default:
                tourInfo += ActivityManager.getInstance().getCurrentActivity().getString(R.string.tour_function_member);
                break;
        }
        tourInfo += " - ";
        // Gan trang thai tour
        int afterToday = Tour.afterToday(event.getmDate());
        if (afterToday == -1) {
            // Tour da dien ra mau do
            tourInfo += "<font color='#" + Integer.toHexString(ContextCompat.getColor(ActivityManager.getInstance().getCurrentActivity(), R.color.colorTourStateInactive) & 0x00ffffff) + "'>" + ActivityManager.getInstance().getCurrentActivity().getString(R.string.tour_state_inactive) + "</font>";
        } else if (afterToday == 1){
            // Tour chua dien ra mau vang
            tourInfo += "<font color='#" + Integer.toHexString(ContextCompat.getColor(ActivityManager.getInstance().getCurrentActivity(), R.color.colorTourStateWaiting) & 0x00ffffff) + "'>" + ActivityManager.getInstance().getCurrentActivity().getString(R.string.tour_state_waiting) + "</font>";
        } else {
            // Tour dang dien ra mau xanh
            tourInfo += "<font color='#" + Integer.toHexString(ContextCompat.getColor(ActivityManager.getInstance().getCurrentActivity(), R.color.colorTourStateActive) & 0x00ffffff) + "'>" + ActivityManager.getInstance().getCurrentActivity().getString(R.string.tour_state_active) + "</font>";
        }
        // Set thong tin tour
        holder.mInfo.setText(Html.fromHtml(tourInfo));
        return convertView;
    }

    public class ViewHolder {
        private ImageView mLogo;
        private TextView mName;
        private TextView mInfo;
    }
}