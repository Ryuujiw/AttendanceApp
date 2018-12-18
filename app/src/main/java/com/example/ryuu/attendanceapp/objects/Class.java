package com.example.ryuu.attendanceapp.objects;

import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;
//this is course
public class Class {
    String coursecode;
    String name;
    String description;
    int image;

    public Class(){

    }

    public Class(String coursecode, String name, String description, int image) {
        this.coursecode = coursecode;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getCoursecode() {
        return coursecode;
    }

    public void setCoursecode(String coursecode) {
        this.coursecode = coursecode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClasscode() {
        return coursecode;
    }

    public void setClasscode(String classcode) {
        this.coursecode = classcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> course = new HashMap<>();
        course.put("classcode", coursecode);
        course.put("name", name);
        course.put("description", description);
        course.put("image", image);

        return course;
    }
}
