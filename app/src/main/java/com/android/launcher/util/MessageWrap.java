package com.android.launcher.util;

public class MessageWrap {

    public   String messageType ;

    public   String message;


    public MessageWrap(String messageType, String message){
        this.messageType =messageType ;
        this.message = message ;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageWrap{" +
                "messageType='" + messageType + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}