package com.example.ryuu.attendanceapp;

public class ClassList {
    String class_name;
    String class_date;
    String class_start_time;

    public ClassList(String className, String classDate, String classStartTime){
        className = class_name;
        classDate = class_date;
        classStartTime = class_start_time;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getClass_date() {
        return class_date;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public void setClass_date(String class_date) {
        this.class_date = class_date;
    }

    public void setClass_start_time(String class_start_time) {
        this.class_start_time = class_start_time;
    }

    public String getClass_start_time() {

        return class_start_time;
    }
}
