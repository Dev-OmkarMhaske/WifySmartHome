package com.wify.smart.home.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PowerObject implements Serializable, Cloneable {

    @SerializedName("0")
    private String filename = "";

    @SerializedName("1")
    private String mac;

    @SerializedName("2")
    private String state;

    @SerializedName("3")
    private String freeze;

    @SerializedName("4")
    private String name;

    @SerializedName("5")
    private String logo;

    @SerializedName("6")
    private String fav;

    @SerializedName("7")
    private String enableAlexa;

    public PowerObject(String filename, String mac, String state, String freeze, String name, String logo, String fav,String enableAlexa) {
        this.filename = filename;
        this.mac = mac;
        this.state = state;
        this.freeze = freeze;
        this.name = name;
        this.logo = logo;
        this.fav = fav;
        this.enableAlexa = enableAlexa;
    }

    public String getAutomationData() {

        //mac@freeze@state@PM

        return new StringBuilder(this.mac).append("@").append(this.freeze).append("@").append(this.state).append("@PM").toString();

    }

    public void setAutomationData(String data) {

        //mac@freeze@state@PM

        String dataSplit[] = data.split("@");

        this.mac = dataSplit[0];
        this.freeze = dataSplit[1];
        this.state = dataSplit[2];

    }

    public String getEnableAlexa() {
        return enableAlexa;
    }

    public void setEnableAlexa(String enableAlexa) {
        this.enableAlexa = enableAlexa;
    }

    @Override
    public String toString() {
        return "PowerObject{" +
                "filename='" + filename + '\'' +
                ", mac='" + mac + '\'' +
                ", state='" + state + '\'' +
                ", freeze='" + freeze + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", fav='" + fav + '\'' +
                '}';
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
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
}
