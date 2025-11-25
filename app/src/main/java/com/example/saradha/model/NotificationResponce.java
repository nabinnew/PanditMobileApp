package com.example.saradha.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationResponce {
    @SerializedName("success")
    private boolean success;

    private List<NotificationModel> data;




    @SerializedName("message")
    private String message;
    private  String id;


    public String getMessage() {
        return message;
    }
    public List<NotificationModel> getData()
    { return data; }


    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
