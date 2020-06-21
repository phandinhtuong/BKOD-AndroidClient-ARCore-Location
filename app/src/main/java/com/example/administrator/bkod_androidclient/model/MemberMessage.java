package com.example.administrator.bkod_androidclient.model;

import java.util.Date;

public class MemberMessage {
    // Ngay gui
    private Date dateSent;
    // Danh dau co phai nguoi gui khong
    private boolean isSender;
    // Noi dung tin nhan
    private String content;

    public MemberMessage(boolean mIsSender, String cContent, Date mDateSent) {
        this.dateSent = mDateSent;
        this.isSender = mIsSender;
        this.content = cContent;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public boolean isSender() {
        return isSender;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
