package com.wify.smart.home.dto;

public class WifyThemes {

    String name;
    int theme;
    int background_img;

    public WifyThemes(String name, int theme, int background_img) {
        this.name = name;
        this.theme = theme;
        this.background_img = background_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public int getBackground_img() {
        return background_img;
    }

    public void setBackground_img(int background_img) {
        this.background_img = background_img;
    }
}
