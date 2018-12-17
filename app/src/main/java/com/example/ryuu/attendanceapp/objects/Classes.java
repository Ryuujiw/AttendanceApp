package com.example.ryuu.attendanceapp.objects;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Classes {
    private String className;
    private int attendanceNo;
    private String qrUrl;
    private boolean status;
    private String date;
    private String location;


    private String startTime;
    private String endTime;



    public Classes(String className, String qrUrl, String location) {
        this.startTime = "";
        this.endTime = "";
        this.className = className;
        this.attendanceNo = 0;
        this.qrUrl = qrUrl;
        this.status = false;
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        date = String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
        this.location = location;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getAttendanceNo() {
        return attendanceNo;
    }

    public void setAttendanceNo(int attendanceNo) {
        this.attendanceNo = attendanceNo;
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
}
