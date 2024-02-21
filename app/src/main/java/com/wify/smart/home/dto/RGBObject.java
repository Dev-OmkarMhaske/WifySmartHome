package com.wify.smart.home.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RGBObject implements Serializable, Cloneable {

    @SerializedName("0")
    private String filename = "";

    @SerializedName("1")
    private String mac;

    @SerializedName("2")
    private String name;

    @SerializedName("3")
    private String freeze;

    @SerializedName("4")
    private String state;

    @SerializedName("5")
    private String brightness_function;

    @SerializedName("6")
    private String brightness_master;

    @SerializedName("7")
    private String function_name;

    @SerializedName("8")
    private String mode;

    @SerializedName("9")
    private String rgb;

    @SerializedName("A")
    private String fav;

    @SerializedName("B")
    private String speed;

    @SerializedName("C")
    private String Led_count;

    @SerializedName("D")
    private String type = "0";

    @SerializedName("E")
    private String enableAlexa;

    public RGBObject(String filename, String mac, String name, String freeze, String state, String brightness_function, String brightness_master, String function_name, String mode, String rgb, String fav, String speed, String led_count, String type, String enableAlexa) {
        this.filename = filename;
        this.mac = mac;
        this.name = name;
        this.freeze = freeze;
        this.state = state;
        this.brightness_function = brightness_function;
        this.brightness_master = brightness_master;
        this.function_name = function_name;
        this.mode = mode;
        this.rgb = rgb;
        this.fav = fav;
        this.speed = speed;
        this.Led_count = led_count;
        this.type = type;
        this.enableAlexa = enableAlexa;
    }

    public String getAutomationData() {

        //mac@freeze@state@brightness_function@brightness_master@function_name@mode@rgb@Led_count@speed@type@RGB

        System.out.println(">>> type >>>" + this.type + ">>>> mode >>>" + this.mode);
        if (this.type.equalsIgnoreCase("0")) {

            return new StringBuilder(this.mac).append("@").append(this.type).append("@").append(this.state).append("@").append(this.rgb).toString();

        } else if (this.type.equalsIgnoreCase("1")) {

            if (this.mode.equalsIgnoreCase("1") || this.mode.equalsIgnoreCase("0")) {

                return new StringBuilder(this.mac).append("@").append(this.type).append("@").append(this.state).append("@").append(this.rgb).append("@").append("1").append("@").append(this.brightness_master).toString();

            } else if (this.mode.equalsIgnoreCase("2")) {

                return new StringBuilder(this.mac).append("@").append(this.type).append("@").append(this.state).append("@").append(this.rgb).append("@").append(this.mode).append("@").append(this.function_name).append("@").append(this.brightness_function).append("@").append(this.speed).toString();

            }

        } else if (this.type.equalsIgnoreCase("2")) {

            return new StringBuilder(this.mac).append("@").append(this.type).append("@").append(this.state).append("@").append(this.brightness_master).toString();

        }

        return null;
    }

    public void setAutomationData(String data, RGBObject rgbObject) {

        //mac@freeze@state@brightness_function@brightness_master@function_name@mode@rgb@Led_count@speed@type@RGB
        String dataSplit[] = data.split("@");

        this.mac = dataSplit[0];
        this.type = dataSplit[1];

        if (this.type.equalsIgnoreCase("1")) {

            if (dataSplit[2].equalsIgnoreCase("1")) {

                this.mode = dataSplit[2];
                this.rgb = dataSplit[4];
                this.brightness_master = dataSplit[5];
                System.out.println(">>>>setAutomationData >>>> state >>> " + this.state);

            } else if (dataSplit[2].equalsIgnoreCase("2")) {

                this.brightness_function = dataSplit[3];
                this.function_name = dataSplit[4];
                this.speed = dataSplit[5];

            }

        } else if (this.type.equalsIgnoreCase("0")) {

            this.state = dataSplit[2];
            this.rgb = dataSplit[3];
            this.brightness_master = dataSplit[4];


        } else if (this.type.equalsIgnoreCase("2")) {

            this.state = dataSplit[2];
            this.brightness_master = brightness_master;

        }

//        this.freeze = dataSplit[1];
//        this.state = dataSplit[2];
//        this.brightness_function = dataSplit[3];
//        this.brightness_master = dataSplit[4];
//        this.function_name = dataSplit[5];
//        this.mode = dataSplit[6];
//        this.rgb = dataSplit[7];
//        this.Led_count = dataSplit[8];
//        this.speed = dataSplit[9];
//
//        if (dataSplit[10] != "0" && dataSplit[10].trim().length() > 0) {
//            this.type = dataSplit[10];
//        }
//        if (this.mode.equalsIgnoreCase("0")) {
//            this.mode = "1";
//        }

    }

    public void setAutomationDataByType(String data, RGBObject rgbObject) {

        String dataSplit[] = data.split("@");

        if (this.type.equalsIgnoreCase("0")) {

            this.mac = dataSplit[0];
            this.type = dataSplit[1];
            this.state = dataSplit[2];
            this.rgb = dataSplit[3];

        } else if (this.type.equalsIgnoreCase("1")) {

            this.mac = dataSplit[0];
            this.type = dataSplit[1];
            this.state = dataSplit[2];
            this.rgb = dataSplit[3];
            this.mode = dataSplit[4];

            if (this.mode.equalsIgnoreCase("1")) {

                this.brightness_master = dataSplit[5];

            } else if (this.mode.equalsIgnoreCase("2")) {

                this.function_name = dataSplit[5];
                this.brightness_function = dataSplit[6];
                this.speed = dataSplit[7];

            }

        } else if (this.type.equalsIgnoreCase("2")) {

            this.mac = dataSplit[0];
            this.type = dataSplit[1];
            this.state = dataSplit[2];
            this.brightness_master = dataSplit[3];

        }

    }

    public String getEnableAlexa() {
        return enableAlexa;
    }

    public void setEnableAlexa(String enableAlexa) {
        this.enableAlexa = enableAlexa;
    }

    @Override
    public String toString() {
        return "RGBObject{" +
                "filename='" + filename + '\'' +
                ", mac='" + mac + '\'' +
                ", name='" + name + '\'' +
                ", freeze='" + freeze + '\'' +
                ", state='" + state + '\'' +
                ", brightness_function='" + brightness_function + '\'' +
                ", brightness_master='" + brightness_master + '\'' +
                ", function_name='" + function_name + '\'' +
                ", mode='" + mode + '\'' +
                ", rgb='" + rgb + '\'' +
                ", fav='" + fav + '\'' +
                ", speed='" + speed + '\'' +
                ", Led_count='" + Led_count + '\'' +
                ", type='" + type + '\'' +
                '}';
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLed_count() {
        return Led_count;
    }

    public void setLed_count(String led_count) {
        Led_count = led_count;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
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

    public String getBrightness_function() {
        return brightness_function;
    }

    public void setBrightness_function(String brightness_function) {
        this.brightness_function = brightness_function;
    }

    public String getBrightness_master() {
        return brightness_master;
    }

    public void setBrightness_master(String brightness_master) {
        this.brightness_master = brightness_master;
    }

    public String getFunction_name() {
        return function_name;
    }

    public void setFunction_name(String function_name) {
        this.function_name = function_name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }
}
