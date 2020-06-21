package com.example.administrator.bkod_androidclient.model;

/**
 * Created by thuynguyen on 1/26/16.
 */
public class News {
    // Duong dan den trang tin tuc
    private String mUrl;
    // Tieu de tin tuc
    private String mTitle;
    // Tom tat tin tuc
    private String mSummary;
    // Duong dan den hinh anh tin tuc
    private String mUrlImage;
    // TODO: Constructor
    public News(String url, String title, String summary, String urlImage) {
        this.mUrl = url;
        this.mTitle = title;
        this.mSummary = summary;
        this.mUrlImage = urlImage;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSummary() {
        return mSummary;
    }

    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public String getmUrlImage() {
        return mUrlImage;
    }
    public void setmUrlImage(String urlImage) {
        this.mUrlImage = urlImage;
    }
}
