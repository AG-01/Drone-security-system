package com.example.aeroalert;


public class UserRequests {

    public String id;
    public String request_title;
    public String user_name;
    public String request_type;
    public String request_details;
    public String request_date;

    public UserRequests() {
        // Default constructor required for calls to DataSnapshot.getValue(UserRequests.class)
    }

    public UserRequests(String request_tittle, String user_name, String request_type, String request_detils, String request_date) {
        this.request_title = request_tittle;
        this.user_name = user_name;
        this.request_type = request_type;
        this.request_details = request_detils;
        this.request_date = request_date;
    }
}
