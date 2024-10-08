package com.example.aeroalert;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {

    Context context;
    private String email;
    private String password;
    private int customerId;
    private int subscriptionId;
    private int membershipId;
    private int walletId;
    private String homeDelivery;
    private String firstName;
    private String lastName;
    private String city;
    private int locationId;
    private String society;
    private String address;
    private String tempOrderAddress;
    private String mobile;
    private String image;
    private String temp;
    private SharedPreferences sharedPreferences;


    public UserSession(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }


    // Log Out function
    public void removeUser() {
        sharedPreferences.edit().clear().commit();
    }


    // Getter Setter


    public String getTemp() {
        temp = sharedPreferences.getString("temp", "");
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
        sharedPreferences.edit().putString("temp", temp).commit();
    }


    public String getEmail() {
        email = sharedPreferences.getString("email", "");
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit().putString("email", email).commit();
    }

    public String getPassword() {
        email = sharedPreferences.getString("password", "");
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        sharedPreferences.edit().putString("password", password).commit();
    }

    public int getCustomerId() {
        customerId = sharedPreferences.getInt("customerId", 0);
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
        sharedPreferences.edit().putInt("customerId", customerId).commit();
    }

    public String getFirstName() {
        firstName = sharedPreferences.getString("firstName", "");
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        sharedPreferences.edit().putString("firstName", firstName).commit();
    }

    public String getLastName() {
        lastName = sharedPreferences.getString("lastName", "");
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        sharedPreferences.edit().putString("lastName", lastName).commit();
    }


    public String getCity() {
        city = sharedPreferences.getString("city", "");
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        sharedPreferences.edit().putString("city", city).commit();
    }


    public String getSociety() {
        society = sharedPreferences.getString("society", "");
        return society;
    }


    public void setSociety(String society) {
        this.society = society;
        sharedPreferences.edit().putString("society", society).commit();
    }


    public String getAddress() {
        address = sharedPreferences.getString("address", "");
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        sharedPreferences.edit().putString("address", address).commit();
    }


    public String getTempOrderAddress() {
        tempOrderAddress = sharedPreferences.getString("tempOrderAddress", "");
        return tempOrderAddress;
    }

    public void setTempOrderAddress(String tempOrderAddress) {
        this.tempOrderAddress = tempOrderAddress;
        sharedPreferences.edit().putString("tempOrderAddress", tempOrderAddress).commit();
    }


    public String getMobile() {
        mobile = sharedPreferences.getString("mobile", "");
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
        sharedPreferences.edit().putString("mobile", mobile).commit();
    }

    public String getImage() {
        image = sharedPreferences.getString("image", "");
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        sharedPreferences.edit().putString("image", image).commit();
    }

    public int getMembershipId() {
        membershipId = sharedPreferences.getInt("membershipId", 0);
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
        sharedPreferences.edit().putInt("membershipId", membershipId).commit();
    }


    public int getWalletId() {
        walletId = sharedPreferences.getInt("walletId", 0);
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
        sharedPreferences.edit().putInt("walletId", walletId).commit();
    }

    public String getHomeDelivery() {
        homeDelivery = sharedPreferences.getString("homeDelivery", "");
        return homeDelivery;
    }

    public void setHomeDelivery(String homeDelivery) {
        this.homeDelivery = homeDelivery;
        sharedPreferences.edit().putString("homeDelivery", homeDelivery).commit();
    }


    public int getLocationId() {
        locationId = sharedPreferences.getInt("locationId", 0);
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
        sharedPreferences.edit().putInt("locationId", locationId).commit();
    }


    public int getSubscriptionId() {
        subscriptionId = sharedPreferences.getInt("subscriptionId", 0);
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
        sharedPreferences.edit().putInt("subscriptionId", subscriptionId).commit();
    }


    @Override
    public String toString() {
        return "UserSession{" +
                "context=" + context +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", customerId=" + customerId +
                ", subscriptionId=" + subscriptionId +
                ", membershipId=" + membershipId +
                ", walletId=" + walletId +
                ", homeDelivery='" + homeDelivery + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", locationId=" + locationId +
                ", society='" + society + '\'' +
                ", address='" + address + '\'' +
                ", mobile='" + mobile + '\'' +
                ", image='" + image + '\'' +
                ", sharedPreferences=" + sharedPreferences +
                '}';
    }
}