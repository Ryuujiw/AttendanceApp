package com.example.ryuu.attendanceapp.objects;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;
@IgnoreExtraProperties
public class Student {

    private String name;
    private String email;
    private String gender;
    private String major;
    private HashMap<String,Boolean> courses = new HashMap<String, Boolean>();

    public Student(){
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }

    public Student(String name, String email,String gender, String major) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.major = major;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMajor() { return major; }

    public void setMajor(String major) { this.major = major; }

    public HashMap<String, Boolean> getCourses() {
        return courses;
    }

    public void setCourses(HashMap<String, Boolean> classes) {
        this.courses = classes;
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("gender", gender);
        result.put("major", major);

        return result;
    }


}
