package com.wify.smart.home.dto;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class DimmerObject {

    @SerializedName("0")
    private String mac;

    @SerializedName("1")
    private String intensity;

    @SerializedName("2")
    private String parent_point;

    @SerializedName("3")
    private String parent_mac;

    @SerializedName("4")
    private String color;

    public DimmerObject() {
    }

    public DimmerObject(HashMap<String, String> dataMap) {

//      mac$intensity$parent_mac$parent_point$color
        this.mac = dataMap.get("0");
        this.intensity = dataMap.get("1");
        this.parent_mac = dataMap.get("2");
        this.parent_point = dataMap.get("3");
        this.color = dataMap.get("4");
    }

    public String getParent_point() {
        return parent_point;
    }

    public void setParent_point(String parent_point) {
        this.parent_point = parent_point;
    }

    public String getParent_mac() {
        return parent_mac;
    }

    public void setParent_mac(String parent_mac) {
        this.parent_mac = parent_mac;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    @Override
    public String toString() {
        return "DimmerObject{" +
                "mac='" + mac + '\'' +
                ", intensity='" + intensity + '\'' +
                ", parent_point='" + parent_point + '\'' +
                ", parent_mac='" + parent_mac + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
