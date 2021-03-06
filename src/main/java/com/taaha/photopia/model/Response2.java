package com.taaha.photopia.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Response2 {
    private Date timestamp;
    private int status;
    private String message;
    private Map<String, ArrayList<String>> payload;

    public Response2(Date timestamp, int status, String message, Map<String,ArrayList<String>> payload) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.payload = payload;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, ArrayList<String>> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, ArrayList<String>> payload) {
        this.payload = payload;
    }
}
