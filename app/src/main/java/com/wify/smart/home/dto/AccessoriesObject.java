package com.wify.smart.home.dto;

import com.google.firebase.database.PropertyName;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AccessoriesObject implements Serializable {

    @SerializedName("0")
    private String accessory;

    @SerializedName("1")
    private String date;

    @SerializedName("2")
    private String mac;

    @SerializedName("3")
    private String esp_now;

    @SerializedName("4")
    private String real_mac;

    @SerializedName("5")
    private String state;

    @SerializedName("6")
    private String level;

    @SerializedName("7")
    private boolean isWrite = false;

    @SerializedName("8")
    private String points = "";

    @SerializedName("9")
    private String parentMAC = "";

    @SerializedName("10")
    private String childMAC = "";

    @SerializedName("11")
    private String ip = "";

    @PropertyName("isWrite")
    public boolean isWrite() {
        return isWrite;
    }

    @PropertyName("isWrite")
    public void setWrite(boolean write) {
        this.isWrite = write;
    }

    public String getPoints() {
        return points;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getReal_mac() {
        return real_mac;
    }

    public void setReal_mac(String real_mac) {
        this.real_mac = real_mac;
    }

    public String getEsp_now() {
        return esp_now;
    }

    public void setEsp_now(String esp_now) {
        this.esp_now = esp_now;
    }

    public String getParentMAC() {
        return parentMAC;
    }

    public void setParentMAC(String parentMAC) {
        this.parentMAC = parentMAC;
    }

    public String getChildMAC() {
        return childMAC;
    }

    public void setChildMAC(String childMAC) {
        this.childMAC = childMAC;
    }

    @Override
    public String toString() {
        return "AccessoriesObject{" +
                "accessory='" + accessory + '\'' +
                ", date='" + date + '\'' +
                ", mac='" + mac + '\'' +
                ", esp_now='" + esp_now + '\'' +
                ", real_mac='" + real_mac + '\'' +
                ", state='" + state + '\'' +
                ", level='" + level + '\'' +
                ", isWrite=" + isWrite +
                ", points='" + points + '\'' +
                ", parentMAC='" + parentMAC + '\'' +
                ", childMAC='" + childMAC + '\'' +
                '}';
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
