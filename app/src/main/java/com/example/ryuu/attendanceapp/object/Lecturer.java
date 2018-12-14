package com.example.ryuu.attendanceapp.object;

import java.util.List;

public class Lecturer extends user {

    private String lecturer_id;
    private List<Class> listOfCourse;

    public Lecturer(String name, String email, String phone_num, String gender, String profile_image,String lecturer_id ,List<Class> listOfCourse) {
        super(name, email, phone_num, gender, profile_image);
        this.listOfCourse = listOfCourse;
        this.lecturer_id = lecturer_id;
    }

    public String getLecturer_id() {
        return lecturer_id;
    }

    public void setLecturer_id(String lecturer_id) {
        this.lecturer_id = lecturer_id;
    }

    public List<Class> getListOfCourse() {
        return listOfCourse;

    }

    public void addCourse(Class name) {
        listOfCourse.add(name);
    }
}
