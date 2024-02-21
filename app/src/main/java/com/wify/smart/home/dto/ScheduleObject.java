package com.wify.smart.home.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ScheduleObject implements Serializable {

    @SerializedName("0")
    private String file = "";

    @SerializedName("1")
    private String days = "";

    @SerializedName("2")
    private String active = "";

    @SerializedName("3")
    private String name = "";

    @SerializedName("4")
    private String scene_ids = "";

    @SerializedName("5")
    private String time = "";

    @SerializedName("6")
    private String data = "";

    @SerializedName("7")
    private String last = "";

    @SerializedName("8")
    private String controller_data = "";

    @Override
    public String toString() {
        return "ScheduleObject{" +
                "file='" + file + '\'' +
                ", days='" + days + '\'' +
                ", active='" + active + '\'' +
                ", name='" + name + '\'' +
                ", scene_ids='" + scene_ids + '\'' +
                ", time='" + time + '\'' +
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getScene_ids() {
        return scene_ids;
    }

    public void setScene_ids(String scene_ids) {
        this.scene_ids = scene_ids;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
