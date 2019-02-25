package com.example.ryuu.attendanceapp.objects;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Class_list{
    String className;
    String classDate;
    String classType;
    String classID;


    public Class_list(String className, String classDate, String classType) {
        this.className = className;
        this.classDate = classDate;
        this.classType = classType;
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

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
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

}
