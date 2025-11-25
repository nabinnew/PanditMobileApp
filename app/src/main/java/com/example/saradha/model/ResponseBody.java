package com.example.saradha.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseBody {
    @SerializedName("success")
    private boolean success;


    private UserModel user;



    public UserModel getUser() {
        return user;
    }



    PanditModel pandit;
    private List<Pandit> data;

    public List<Pandit> getData() { return data; }

    @SerializedName("message")
    private String message;
    private  String id;


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getId() {
        return id;
    }
}
