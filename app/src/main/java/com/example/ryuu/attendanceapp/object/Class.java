package com.example.ryuu.attendanceapp.object;

import java.util.concurrent.atomic.AtomicInteger;

public class Class {
    private String classTitle, classDate, classStartTime, classEndTime, classVenue, qrurl;
    private boolean started, status;
    private int attendanceNumber, classID;
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1000);

    public Class(String classTitle, String classDate, String classStartTime, String classVenue, String qrurl){
        this.classTitle = classTitle;
        this.classDate = classDate;
        this.classStartTime = classStartTime;
        this.classVenue = classVenue;
        this.qrurl = qrurl;
        this.classID = ID_GENERATOR.getAndIncrement();
        this.started = false;
        this.status = false;
        this.classEndTime = "";
        this.attendanceNumber = 0;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
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

    public String getClassEndTime() {
        return classEndTime;
    }

    public void setClassEndTime(String classEndTime) {
        this.classEndTime = classEndTime;
    }

    public String getClassVenue() {
        return classVenue;
    }

    public void setClassVenue(String classVenue) {
        this.classVenue = classVenue;
    }

    public String getQrurl() {
        return qrurl;
    }

    public void setQrurl(String qrurl) {
        this.qrurl = qrurl;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getAttendanceNumber() {
        return attendanceNumber;
    }

    public void setAttendanceNumber(int attendanceNumber) {
        this.attendanceNumber = attendanceNumber;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public static AtomicInteger getIdGenerator() {
        return ID_GENERATOR;
    }

    public static void setIdGenerator(AtomicInteger idGenerator) {
        ID_GENERATOR = idGenerator;
    }
}
