package com.wify.smart.home.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class DBObject implements Serializable {

    private String name;
    private String date;
    private ArrayList<String> host;
    private ArrayList<String> shared;

    public DBObject(){

    }

    public DBObject(String name, String date, ArrayList<String> host, ArrayList<String> shared) {
        this.name = name;
        this.date = date;
        this.host = host;
        this.shared = shared;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getHost() {
        return host;
    }

    public void setHost(ArrayList<String> host) {
        this.host = host;
    }

    public ArrayList<String> getShared() {
        return shared;
    }

    public void setShared(ArrayList<String> shared) {
        this.shared = shared;
    }
}
