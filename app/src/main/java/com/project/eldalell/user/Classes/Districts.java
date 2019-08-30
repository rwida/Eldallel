package com.project.eldalell.user.Classes;

public class Districts {
    private String id, district_name, city_id;

    public Districts(String id, String district_name, String city_id) {
        this.id = id;
        this.district_name = district_name;
        this.city_id = city_id;
    }

    public Districts() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }
}
