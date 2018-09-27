package com.example.swong.myweather.db;


import org.litepal.crud.DataSupport;

public class City extends DataSupport {

    private int id;

    private int cityId;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {return cityId;}

    public void setCityId(int cityId) {this.cityId = cityId;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
