package com.wify.smart.home.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MotionObject implements Serializable, Cloneable {

    @SerializedName("0")
    private String filename = "";

    @SerializedName("1")
    private String mac;

    @SerializedName("2")
    private String active;

    @SerializedName("3")
    private String active_time;

    @SerializedName("4")
    private String start_time;

    @SerializedName("5")
    private String end_time;

    @SerializedName("6")
    private String period;

    @SerializedName("7")
    private String name;

    @SerializedName("8")
    private String scene_ids;

    @SerializedName("9")
    private String room_data;

    @SerializedName("A")
    private String fav;

    @SerializedName("B")
    private String controller_data = "";

    public MotionObject() {

    }

    public MotionObject(String filename, String mac, String active, String active_time, String start_time, String end_time, String period, String name, String scene_ids, String room_data, String fav) {
        this.filename = filename;
        this.mac = mac;
        this.active = active;
        this.active_time = active_time;
        this.start_time = start_time;
        this.end_time = end_time;
        this.period = period;
        this.name = name;
        this.scene_ids = scene_ids;
        this.room_data = room_data;
        this.fav = fav;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getController_data() {
        return controller_data;
    }

    public void setController_data(String controller_data) {
        this.controller_data = controller_data;
    }

    public String getAutomationData() {

        //mac@active@period

        return new StringBuilder(this.mac).append("@").append(this.active).append("@").append(this.period).toString();

    }

    public void setAutomationData(String data) {

        //mac@active@period

        String dataSplit[] = data.split("@");

        this.mac = dataSplit[0];
        this.active = dataSplit[1];
        this.period = dataSplit[2];

    }

    @Override
    public String toString() {
        return "MotionObject{" +
                "filename='" + filename + '\'' +
                ", mac='" + mac + '\'' +
                ", active='" + active + '\'' +
                ", active_time='" + active_time + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", period='" + period + '\'' +
                ", name='" + name + '\'' +
                ", scene_ids='" + scene_ids + '\'' +
                ", room_data='" + room_data + '\'' +
                ", fav='" + fav + '\'' +
                ", controller_data='" + controller_data + '\'' +
                '}';
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getScene_ids() {
        return scene_ids;
    }

    public void setScene_ids(String scene_ids) {
        this.scene_ids = scene_ids;
    }

    public String getRoom_data() {
        return room_data;
    }

    public void setRoom_data(String room_data) {
        this.room_data = room_data;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getActive_time() {
        return active_time;
    }

    public void setActive_time(String active_time) {
        this.active_time = active_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}