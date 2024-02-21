package com.wify.smart.home.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SceneObject implements Serializable {

    @SerializedName("0")
    private String file = "";

    @SerializedName("1")
    private String name = "";

    @SerializedName("2")
    private String logo = "";

    @SerializedName("3")
    private String fav = "";

    @SerializedName("4")
    private String data = "";

    @SerializedName("5")
    private String last = "";

    @SerializedName("6")
    private String controller_data = "";

    public SceneObject() {

    }

    public SceneObject(String file, String name, String logo, String fav, String data, String last) {
        this.file = file;
        this.name = name;
        this.logo = logo;
        this.fav = fav;
        this.data = data;
        this.last = last;
    }

    @Override
    public String toString() {
        return "SceneObject{" +
                "file='" + file + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", fav='" + fav + '\'' +
                ", data='" + data + '\'' +
                ", last='" + last + '\'' +
                ", controller_data='" + controller_data + '\'' +
                '}';
    }

    public String getController_data() {
        return controller_data;
    }

    public void setController_data(String controller_data) {
        this.controller_data = controller_data;
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

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
