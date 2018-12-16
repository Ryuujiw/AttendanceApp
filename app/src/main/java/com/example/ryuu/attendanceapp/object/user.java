package com.example.ryuu.attendanceapp.object;

public class user {

    private String name;
    private String email;
    private String gender;
    private String profile_image;

    public user(String name, String email, String gender) {
        this.name = name;
        this.email = email;
        this.gender = gender;
    }
    public String getProfile_image() {
        return profile_image;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
