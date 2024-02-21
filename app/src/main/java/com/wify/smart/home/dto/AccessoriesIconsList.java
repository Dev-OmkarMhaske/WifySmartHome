package com.wify.smart.home.dto;

import java.io.Serializable;

public class AccessoriesIconsList implements Serializable {

    String name;
    int icon;
    int disable_icon;


    public AccessoriesIconsList() {
    }

    public AccessoriesIconsList(String name, int icon, int disable_icon) {
        this.name = name;
        this.icon = icon;
        this.disable_icon = disable_icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getDisable_icon() {
        return disable_icon;
    }

    public void setDisable_icon(int disable_icon) {
        this.disable_icon = disable_icon;
    }
}
