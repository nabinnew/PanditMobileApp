package com.example.saradha.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user;

    UserProfileResponse profile;

    @SerializedName("token")
    private String token;

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    // Inner User class
    public static class User {
        @SerializedName("_id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("email")
        private String email;

        @SerializedName("role")
        private String role;

        @SerializedName("phone")
        private String phone;

        @SerializedName("address")
        private String address;

        // Getters
        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }
    }
}
