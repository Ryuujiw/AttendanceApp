package com.example.ryuu.attendanceapp.objects;

import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;
//this is course
public class Class {
    String course_code;
    String course_name;
    String description;
    String date_created;
    private HashMap<String,Boolean> roster = new HashMap<String, Boolean>();

    public Class(){

    }

    public Class(String coursecode, String name, String description, String date_created, HashMap<String, Boolean> roster) {
        this.course_code = coursecode;
        this.course_name = name;
        this.description = description;
        this.date_created = date_created;
        this.roster = roster;
    }

    public Class(String coursecode, String name, String description, String date_created) {
        this.course_code = coursecode;
        this.course_name = name;
        this.description = description;
        this.date_created = date_created;
    }

    public HashMap<String, Boolean> getRoster() {
        return roster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoursecode() {
        return course_code;
    }

    public void setCoursecode(String coursecode) {
        this.course_code = coursecode;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public void setRoster(HashMap<String, Boolean> roster) {
        this.roster = roster;
    }

    public String getName() {
        return course_name;
    }

    public void setName(String name) {
        this.course_name = name;
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
