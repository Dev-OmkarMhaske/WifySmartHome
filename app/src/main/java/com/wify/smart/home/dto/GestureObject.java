package com.wify.smart.home.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GestureObject implements Serializable {

    @SerializedName("0")
    private String name;

    @SerializedName("1")
    private String scene;

    @SerializedName("2")
    private int icon;

    public GestureObject() {

    }

    public GestureObject(String name, String scene, int icon) {

        this.name = name;

        this.scene = scene;

        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    @Override
    public String toString() {
        return "GestureObject{" +
                "name='" + name + '\'' +
                ", scene='" + scene + '\'' +
                ", icon=" + icon +
                '}';
    }
}
