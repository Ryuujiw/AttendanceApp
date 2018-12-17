package com.example.ryuu.attendanceapp.objects;

import java.util.HashMap;
import java.util.Map;

public class Student {


    private String name;
    private String email;
    private String role;
    private String gender;
    private String major;
    private String matric;

//    public Map<String, Boolean> course = new HashMap<>();

    public Student(){
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }

    public Student(String name, String email, String role,String gender, String major, String matric) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.gender = gender;
        this.major = major;
        this.matric = matric;

    }

    public String getMatric() {
        return matric;
    }

    public void setMatric(String matric) {
        this.matric = matric;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMajor() {

        return major;
    }

    public void setMajor(String major) {

        this.major = major;
    }


}
