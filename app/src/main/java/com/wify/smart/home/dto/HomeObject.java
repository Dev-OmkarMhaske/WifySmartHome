package com.wify.smart.home.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class HomeObject implements Serializable {

    private String home;
    private String date;
    private String mqtt;
    private String note;
    private HashMap<String, AccessoriesObject> accessories;
    private List<String> users;
    private String home_uid;

    public String getHome_uid() {
        return home_uid;
    }

    public void setHome_uid(String home_uid) {
        this.home_uid = home_uid;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMqtt() {
        return mqtt;
    }

    public void setMqtt(String mqtt) {
        this.mqtt = mqtt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public HashMap<String, AccessoriesObject> getAccessories() {

        if (accessories == null) {

            accessories = new HashMap<>();
        }
        return accessories;
    }

    public void setAccessories(HashMap<String, AccessoriesObject> accessories) {
        this.accessories = accessories;
    }
}
