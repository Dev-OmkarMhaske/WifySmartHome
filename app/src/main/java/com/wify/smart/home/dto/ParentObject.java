package com.wify.smart.home.dto;

import java.io.Serializable;

public class ParentObject implements Serializable {

    private String name;

    private String range;

    private String realMAC;

    public ParentObject(String name, String range, String realMAC) {
        this.name = name;
        this.range = range;
        this.realMAC = realMAC;
    }

    public String getRealMAC() {
        return realMAC;
    }

    public void setRealMAC(String realMAC) {
        this.realMAC = realMAC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }
}
