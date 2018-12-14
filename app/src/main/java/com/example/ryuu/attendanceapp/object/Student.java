package com.example.ryuu.attendanceapp.object;

import java.util.List;

public class Student extends user {

    private String matric_num;
    private String course;
    private List<Class> listofClass;

    public Student(String name, String email, String phone_num, String gender, String profile_image, String matric_num ,String course) {
        super(name, email, phone_num, gender, profile_image);
        this.matric_num = matric_num;
        this.course = course;
    }

    public String getCourse() {
        return course;
    }

    public String getMatric_num(){
        return matric_num;
    }


    public List<Class> getListofClass() {
        return listofClass;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setMatric_num(String matric_num){
        this.matric_num = matric_num;
    }
    public void addClass(Class name){
        listofClass.add(name);
    }
}
