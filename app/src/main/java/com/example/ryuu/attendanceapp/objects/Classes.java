package com.example.ryuu.attendanceapp.objects;
import java.util.HashMap;
import java.util.Map;

//This is the real object for classes
public class Classes {
    private String className, classID;
    private String attend_list;
    private String qrUrl;
    private boolean status, open;
    private String date;
    private String location;
    private String startTime,endTime;

    public Classes(String classID,String className, String date,String startTime, String location) {
        this.classID = classID;
        this.className = className;
        this.attend_list = "";
        this.qrUrl = classID;
        this.status = false;
        this.open = false;
        this.date = date;
        this.location = location;
        this.startTime = startTime;
        this.endTime = "";
    }
    public Classes(){

    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getAttend_list() {
        return attend_list;
    }

    public void setAttend_list(String attend_list) {
        this.attend_list = attend_list;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("className", className);
//        result.put("date", date);
//        result.put("start_time", startTime);
//        result.put("end_time", endTime);
//        result.put("location", location);
//        result.put("status", status);
//        result.put("open", open);
//        result.put("QR", qrUrl);
//
//        return result;
//    }
}
