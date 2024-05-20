package com.arini.paiment.model;


import java.util.UUID;

public class PayRequest {
    private UUID classRoomID;
    private UUID studentID;
    private String month;

    // Getters and Setters
    public UUID getClassRoomID() {
        return classRoomID;
    }

    public void setClassRoomID(UUID classRoomID) {
        this.classRoomID = classRoomID;
    }

    public UUID getStudentID() {
        return studentID;
    }

    public void setStudentID(UUID studentID) {
        this.studentID = studentID;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
