package com.example.ryuu.attendanceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Class_list{
    String className;
    String classDate;
    String classStartTime;
    String classID;

    public Class_list(String className, String classDate, String classStartTime) {
        this.className = className;
        this.classDate = classDate;
        this.classStartTime = classStartTime;
        this.classID="";
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDate() {
        return classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public String getClassStartTime() {
        return classStartTime;
    }

    public void setClassStartTime(String classStartTime) {
        this.classStartTime = classStartTime;
    }
}
