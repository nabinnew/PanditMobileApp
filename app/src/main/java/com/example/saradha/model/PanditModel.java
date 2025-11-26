package com.example.saradha.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PanditModel {

    @SerializedName("_id")

    public String userid;
    public String name;
    public int price;

    public String photo;      // FIRST object uses "photo"
    public String profile;    // second & third use "profile"

    public List<String> services;

    public String experience;
    public String location;
    public boolean verified;
    public String phone;

    public List<Boolean> booking;

    public String status;
    public String address;
    public String description;
    public boolean bookNow;

}
