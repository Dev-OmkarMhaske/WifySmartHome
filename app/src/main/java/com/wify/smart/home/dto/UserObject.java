package com.wify.smart.home.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserObject implements Serializable {

    @SerializedName("0")
    private String file = "";

    @SerializedName("1")
    private String phn = "";

    @SerializedName("2")
    private String name = "";

    @SerializedName("3")
    private String phn_mac = "";

    @SerializedName("4")
    private String last = "";

    @SerializedName("5")
    private String mac = "";

    @SerializedName("6")
    private String type = "";

    public UserObject() {
    }

    public UserObject(String file, String phn, String name, String phn_mac, String last, String mac, String type) {
        this.file = file;
        this.phn = phn;
        this.name = name;
        this.phn_mac = phn_mac;
        this.last = last;
        this.mac = mac;
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserObject{" +
                "file='" + file + '\'' +
                ", phn='" + phn + '\'' +
                ", name='" + name + '\'' +
                ", phn_mac='" + phn_mac + '\'' +
                ", last='" + last + '\'' +
                ", mac='" + mac + '\'' +
                ", type='" + type + '\'' +
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

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhn_mac() {
        return phn_mac;
    }

    public void setPhn_mac(String phn_mac) {
        this.phn_mac = phn_mac;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
