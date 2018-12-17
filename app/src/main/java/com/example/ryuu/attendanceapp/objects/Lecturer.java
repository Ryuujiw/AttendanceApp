package com.example.ryuu.attendanceapp.objects;

import com.example.ryuu.attendanceapp.objects.Class;

import java.util.List;

public class Lecturer{

    private String matric;
    private String name;
    private String email;
    private String role;
    private String gender;
    private List<Class> listOfCourse;

    public Lecturer(String matric, String name, String email, String role, String gender) {
        this.matric = matric;
        this.name = name;
        this.email = email;
        this.role = role;
        this.gender = gender;
    }
    public Lecturer() {
        super();
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }
    public List<Class> getListOfCourse() {
        return listOfCourse;

    }
    public void addCourse(Class name) {

        listOfCourse.add(name);
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
}
