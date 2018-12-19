package com.example.ryuu.attendanceapp.objects;

import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;

//This is the object for Course

public class Class {
    private HashMap<String, Boolean> student = new HashMap<String, Boolean>();
    String date_created;
    String description;
    String course_code;
    String course_name;
    private HashMap<String,Boolean> lecturer = new HashMap<String, Boolean>();

    public Class(){

    }

    public Class(String coursecode, String name, String description, String date_created, HashMap<String, Boolean> lecturer, HashMap<String, Boolean> student) {
        this.student = student;
        this.date_created = date_created;
        this.description = description;
        this.course_code = coursecode;
        this.course_name = name;
        this.lecturer = lecturer;
    }

    public Class(String coursecode, String name, String description, String date_created) {
        this.course_code = coursecode;
        this.course_name = name;
        this.description = description;
        this.date_created = date_created;
    }

    public HashMap<String, Boolean> getStudent() {
        return student;
    }

    public void setStudent(HashMap<String, Boolean> student) {
        this.student = student;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public HashMap<String, Boolean> getLecturer() {
        return lecturer;
    }

    public void setLecturer(HashMap<String, Boolean> lecturer) {
        this.lecturer = lecturer;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> course = new HashMap<>();
        course.put("course_code", course_code);
        course.put("course_name", course_name);
        course.put("description", description);
        course.put("date_created", date_created);

        return course;
    }
}
