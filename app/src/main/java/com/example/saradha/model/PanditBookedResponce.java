package com.example.saradha.model;

import java.util.List;

public class PanditBookedResponce {
    private boolean success;
    private String message;
    private List<PanditBooked> data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<PanditBooked> getData() {
        return data;
    }
}
