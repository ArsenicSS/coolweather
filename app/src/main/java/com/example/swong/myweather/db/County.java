package com.example.swong.myweather.db;


import org.litepal.crud.DataSupport;

public class County extends DataSupport {

    private int id;

    private int countyId;

    private String weatherId;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountyId() {return countyId;}

    public void setCountyId(int countyId) {this.countyId = countyId;}

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
