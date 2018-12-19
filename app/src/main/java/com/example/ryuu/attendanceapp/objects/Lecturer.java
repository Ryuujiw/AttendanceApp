package com.example.ryuu.attendanceapp.objects;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Lecturer{

    private String name;
    private String email;
    private String matric;
    private String gender;
    private HashMap<String,Boolean> courses = new HashMap<String, Boolean>();

    public Lecturer(String name, String email, String matric, String gender) {
        this.name = name;
        this.email = email;
        this.matric = matric;
        this.gender = gender;
    }

    public Lecturer() {
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatric() {
        return matric;
    }

    public void setMatric(String matric) {
        this.matric = matric;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("matric", matric);
        result.put("gender", gender);

        return result;
    }
}
