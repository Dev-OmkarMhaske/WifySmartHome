package com.wify.smart.home.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CurtainObject implements Serializable, Cloneable {

    @SerializedName("0")
    private String filename = "";

    @SerializedName("1")
    private String mac;

    @SerializedName("2")
    private String name;

    @SerializedName("3")
    private String state;

    @SerializedName("4")
    private String fav;

    @SerializedName("5")
    private String enableAlexa;

    public CurtainObject(String filename, String mac, String name, String state, String fav, String enableAlexa) {
        this.filename = filename;
        this.mac = mac;
        this.name = name;
        this.state = state;
        this.fav = fav;
        this.enableAlexa = enableAlexa;
    }

    public String getAutomationData() {

        //mac@state@CM
        return new StringBuilder(this.mac).append("@").append(this.state).append("@CM").toString();

    }

    public void setAutomationData(String data) {

        //mac@state@CM

        String dataSplit[] = data.split("@");

        this.mac = dataSplit[0];
        this.state = dataSplit[1];

    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getEnableAlexa() {
        return enableAlexa;
    }

    public void setEnableAlexa(String enableAlexa) {
        this.enableAlexa = enableAlexa;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "CurtainObject{" +
                "filename='" + filename + '\'' +
                ", mac='" + mac + '\'' +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", fav='" + fav + '\'' +
                '}';
    }
}
