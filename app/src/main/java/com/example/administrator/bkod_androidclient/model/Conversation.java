package com.example.administrator.bkod_androidclient.model;

import java.util.ArrayList;

public class Conversation {
    // Id cua nguoi nhan tin voi minh
    private int partnerId;
    // Ten cua nguoi nhan tin voi minh
    private String partnerName;
    // Danh sach tin nhan
    private ArrayList<MemberMessage> memberMessages;

    public Conversation(int mRecieverId, String mRecieverName, ArrayList<MemberMessage> mMemberMessages) {
        this.partnerId = mRecieverId;
        this.partnerName = mRecieverName;
        this.memberMessages = mMemberMessages;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public ArrayList<MemberMessage> getMemberMessages() {
        return memberMessages;
    }

    public void setMemberMessages(ArrayList<MemberMessage> memberMessages) {
        this.memberMessages = memberMessages;
    }
}