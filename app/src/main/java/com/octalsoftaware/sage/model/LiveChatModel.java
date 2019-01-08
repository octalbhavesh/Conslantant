package com.octalsoftaware.sage.model;

public class LiveChatModel {
    String sender_id, receiver_id, sender_name,receiver_name,
            sender_message,receiver_message,sender_date,receiver_date,messageType,img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getSender_message() {
        return sender_message;
    }

    public void setSender_message(String sender_message) {
        this.sender_message = sender_message;
    }

    public String getReceiver_message() {
        return receiver_message;
    }

    public void setReceiver_message(String receiver_message) {
        this.receiver_message = receiver_message;
    }

    public String getSender_date() {
        return sender_date;
    }

    public void setSender_date(String sender_date) {
        this.sender_date = sender_date;
    }

    public String getReceiver_date() {
        return receiver_date;
    }

    public void setReceiver_date(String receiver_date) {
        this.receiver_date = receiver_date;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
