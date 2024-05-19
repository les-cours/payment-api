package com.arini.paiment.model;

import java.util.UUID;

public class Student {
    private UUID studentID;
    private String firstName;
    private float amount;


    public Student(UUID studentID, String firstName, float amount) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.amount = amount;
    }

    public Student() {}

    public UUID getStudentID() {
        return studentID;
    }

    public void setStudentID(UUID studentID) {
        this.studentID = studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
