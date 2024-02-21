package com.wify.smart.home.dto;

public class VersionObject {

    private String MQTT;
    private String android_version;
    private String miniserver_version;
    private String ios_version;

    public String getMQTT() {
        return MQTT;
    }

    public void setMQTT(String MQTT) {
        this.MQTT = MQTT;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public String getMiniserver_version() {
        return miniserver_version;
    }

    public void setMiniserver_version(String miniserver_version) {
        this.miniserver_version = miniserver_version;
    }

    public String getIos_version() {
        return ios_version;
    }

    public void setIos_version(String ios_version) {
        this.ios_version = ios_version;
    }
}
