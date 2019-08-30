package com.project.eldalell.user.Classes;

public class Address {
    private String  city, District_id, Street, BuildingNumber, FloorNumber, ApartmentNumber, AddressSubject,id,User_id
            ,district;

    public Address(String country, String city, String district_id, String street, String buildingNumber, String floorNumber, String apartmentNumber) {
        this.city = city;
        District_id = district_id;
        Street = street;
        BuildingNumber = buildingNumber;
        FloorNumber = floorNumber;
        ApartmentNumber = apartmentNumber;
    }

    public Address() {
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict_id() {
        return District_id;
    }

    public void setDistrict_id(String district_id) {
        District_id = district_id;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getBuildingNumber() {
        return BuildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        BuildingNumber = buildingNumber;
    }

    public String getFloorNumber() {
        return FloorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        FloorNumber = floorNumber;
    }

    public String getApartmentNumber() {
        return ApartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        ApartmentNumber = apartmentNumber;
    }

    public String getAddressSubject() {
        return AddressSubject;
    }

    public void setAddressSubject(String addressSubject) {
        AddressSubject = addressSubject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
