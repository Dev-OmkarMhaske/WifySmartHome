package com.wify.smart.home.mqtt.dto;

import com.google.gson.annotations.SerializedName;
import com.wify.smart.home.utils.Utility;

public class TransactionObject {

    @SerializedName("0")
    private String transID;

    @SerializedName("1")
    private String type;

    @SerializedName("2")
    private String action;

    @SerializedName("3")
    private String data;

    @SerializedName("4")
    private String file;

    @SerializedName("5")
    private String key;

    @Override
    public String toString() {
        return "TransactionObject{" +
                "transID='" + transID + '\'' +
                ", type='" + type + '\'' +
                ", action='" + action + '\'' +
                ", data='" + data + '\'' +
                ", file='" + file + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public TransactionObject() {
        this.transID = "" + System.currentTimeMillis();
        this.transID = Utility.COMMUNICATION_MODE + "." + this.transID.substring(this.transID.length() - 4);
    }

    public TransactionObject(String transID, String type, String key, String data) {
        this.transID = transID;
        this.type = type;
        this.data = data;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
