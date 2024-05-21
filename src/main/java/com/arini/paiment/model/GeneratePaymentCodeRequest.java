package com.arini.paiment.model;


import java.util.UUID;

public class GeneratePaymentCodeRequest {
    private float amount;

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
