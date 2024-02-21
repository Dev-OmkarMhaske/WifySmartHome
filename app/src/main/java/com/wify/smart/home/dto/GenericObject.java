package com.wify.smart.home.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GenericObject implements Serializable, Cloneable {

    @SerializedName("0")
    private String filename = "";

    @SerializedName("1")
    private String mac = "";

    @SerializedName("2")
    private String point = "";

    @SerializedName("3")
    private String logo = "";

    @SerializedName("4")
    private String name = "";

    @SerializedName("5")
    private String freeze = "";

    @SerializedName("6")
    private String state = "";

    @SerializedName("7")
    private String used = "";

    @SerializedName("8")
    private String fav = "";

    @SerializedName("9")
    private String dmData = "";

    @SerializedName("A")
    private String enableAlexa;

    @SerializedName("B")
    private String type = "";

    public GenericObject() {
    }

    public GenericObject(String filename, String mac, String point, String logo, String name, String freeze, String state, String used, String fav, String enableAlexa, String type) {
        this.filename = filename;
        this.mac = mac;
        this.point = point;
        this.logo = logo;
        this.name = name;
        this.freeze = freeze;
        this.state = state;
        this.used = used;
        this.fav = fav;
        this.enableAlexa = enableAlexa;
        this.type = type;
    }

    public String getAutomationData() {

        //mac@point@freeze@state@GM
        return new StringBuilder(this.mac).append("@").append(this.point).append("@").append(this.freeze).append("@").append(this.state).append("@").append(this.type).toString();

    }

    public void setAutomationData(String data) {

        //mac@point@freeze@state@GM

        String dataSplit[] = data.split("@");

        this.mac = dataSplit[0];
        this.point = dataSplit[1];
        this.freeze = dataSplit[2];
        this.state = dataSplit[3];

    }

    @Override
    public String toString() {
        return "GenericObject{" +
                "filename='" + filename + '\'' +
                ", mac='" + mac + '\'' +
                ", point='" + point + '\'' +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                ", freeze='" + freeze + '\'' +
                ", state='" + state + '\'' +
                ", used='" + used + '\'' +
                ", fav='" + fav + '\'' +
                ", dmData='" + dmData + '\'' +
                '}';
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDmData() {
        return dmData;
    }

    public void setDmData(String dmData) {
        this.dmData = dmData;
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

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }
}
