package com.project.eldalell.user.Classes;

public class City {
    private String id, city_name;

    public City(String id, String city_name) {
        this.id = id;
        this.city_name = city_name;
    }

    public City() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
