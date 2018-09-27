package com.example.swong.myweather.db;


import org.litepal.crud.DataSupport;

public class Province extends DataSupport{

    private int id;

    private int provinceId;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProviceId() {return provinceId;}

    public void setProvinceId(int provinceId) {this.provinceId = provinceId;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}


