package com.example.administrator.bkod_androidclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.model.News;
import com.squareup.picasso.Picasso;

import Manager.OnlineManager;

public class NewsAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public NewsAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.mLayoutInflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        if (OnlineManager.getInstance().mNewsList == null) {
            return 0;
        }
        return OnlineManager.getInstance().mNewsList.size();
    }

    @Override
    public Object getItem(int position) {
        return OnlineManager.getInstance().mNewsList.get(position);
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
            convertView = mLayoutInflater.inflate(R.layout.news_item_layout, null);
            holder.mLogo = (ImageView) convertView.findViewById(R.id.img_news_logo);
            holder.mTitle = (TextView) convertView.findViewById(R.id.txt_news_title);
            holder.mSummary = (TextView) convertView.findViewById(R.id.txt_news_summary);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        News event = (News) getItem(position);
        Picasso.get().load(event.getmUrlImage()).resize(256, 256).centerInside().into(holder.mLogo);
        holder.mSummary.setText(event.getmSummary());
        holder.mTitle.setText(event.getmTitle());
        return convertView;
    }

    public class ViewHolder {
        private ImageView mLogo;
        private TextView mTitle;
        private TextView mSummary;
    }
}