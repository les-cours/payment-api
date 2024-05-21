package com.arini.paiment.model;

public class AppResponse {
    private boolean success;
    private String message;
    private Object data;


    public AppResponse() {
    }


    public AppResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AppResponse(boolean success, String message,Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AppResponse [success=" + success + ", message=" + message + "]";
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

