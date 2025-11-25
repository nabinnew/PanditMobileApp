package com.example.saradha.model;
public class NotificationModel {
    private String title;
    private String body;
    private String created_at;

    public NotificationModel(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
    public String getCreated_at() {
        return created_at;
    }

}
