package edu.neu.recipehub.objects;

public class ChatMessage {
    private String mMessage;
    private String mUserName;

    public ChatMessage() {
    }

    public ChatMessage(String mMessage, String mUserName) {
        this.mMessage = mMessage;
        this.mUserName = mUserName;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }
}
