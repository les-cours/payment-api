package com.arini.paiment.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class ChargeAccountRequest {
    @JsonProperty("student_id")
    private UUID studentID;
    private String code;

    // Getters and Setters


    public UUID getStudentID() {
        return studentID;
    }

    public void setStudentID(UUID studentID) {
        this.studentID = studentID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
