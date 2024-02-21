package com.wify.smart.home.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RoomObject implements Serializable {

    @SerializedName("0")
    private String file;

    @SerializedName("1")
    private String name;

    @SerializedName("2")
    private String logo;

    @SerializedName("3")
    private String mac;

    @SerializedName("4")
    private String last;

    @Override
    public String toString() {
        return "RoomObject{" +
                "file='" + file + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", mac='" + mac + '\'' +
                ", last='" + last + '\'' +
                '}';
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMac() {

        if (mac == null) {

            mac = "";
        }

        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
