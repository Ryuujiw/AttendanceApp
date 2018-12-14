package com.example.ryuu.attendanceapp.object;

public class user {

    private String name;
    private String email;
    private String phone_num;
    private String gender;
    private String profile_image;

    public user(String name, String email, String phone_num, String gender, String profile_image) {
        this.name = name;
        this.email = email;
        this.phone_num = phone_num;
        this.gender = gender;
        this.profile_image = profile_image;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public String getGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
