package com.wify.smart.home.dto;

import com.google.gson.annotations.SerializedName;
import com.wify.smart.home.utils.UtilityConstants;

import java.io.Serializable;

public class FanObject implements Serializable, Cloneable {

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
    private String speed;

    @SerializedName("6")
    private String fav;

    @SerializedName("7")
    private String enableAlexa;

    @SerializedName("8")
    private String point;

    @SerializedName("9")
    private String type;

    public FanObject(String filename, String mac, String state, String freeze, String name, String speed, String fav, String enableAlexa, String point, String type) {
        this.filename = filename;
        this.mac = mac;
        this.state = state;
        this.freeze = freeze;
        this.name = name;
        this.speed = speed;
        this.fav = fav;
        this.enableAlexa = enableAlexa;
        this.point = point;
        this.type = type;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getAutomationData() {

        //mac@point@freeze@state@speed@FM

        if (this.type.equalsIgnoreCase(UtilityConstants.CFM)) {

            return new StringBuilder(this.mac.replace("F1", "")).append("@").append(this.point).append("@").append(this.freeze).append("@").append(this.state).append("@").append(this.speed).append("@").append(this.type).toString();

        }
        return new StringBuilder(this.mac).append("@").append(this.point).append("@").append(this.freeze).append("@").append(this.state).append("@").append(this.speed).append("@").append(this.type).toString();

    }

    public void setAutomationData(String data) {
        //mac@point@freeze@state@speed@FM

        String dataSplit[] = data.split("@");

        System.out.println(">>>>> fan setAutomationData >>>" + data);

        this.mac = dataSplit[0];
        this.point = dataSplit[1];
        this.freeze = dataSplit[2];
        this.state = dataSplit[3];
        this.speed = dataSplit[4];

    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnableAlexa() {
        return enableAlexa;
    }

    public void setEnableAlexa(String enableAlexa) {
        this.enableAlexa = enableAlexa;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "FanObject{" +
                "filename='" + filename + '\'' +
                ", mac='" + mac + '\'' +
                ", state='" + state + '\'' +
                ", freeze='" + freeze + '\'' +
                ", name='" + name + '\'' +
                ", speed='" + speed + '\'' +
                ", fav='" + fav + '\'' +
                ", enableAlexa='" + enableAlexa + '\'' +
                ", point='" + point + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
