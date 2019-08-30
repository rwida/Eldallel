package com.project.eldalell.user.Classes;

public class Shop {
    private String shopLogo;
    private String shopName, shopDetails, shop_type_id,id,district_id,vendor_id;
    private float delivery_cost;
    private int delivery_time;
    private float ShopRate;
    private boolean statework;

    public Shop() {
    }

    public Shop(String shopLogo, String shopName, String shopDetails, String shop_type_id, String id, String district_id, String vendor_id, float delivery_cost, int delivery_time, float shopRate, boolean statework) {
        this.shopLogo = shopLogo;
        this.shopName = shopName;
        this.shopDetails = shopDetails;
        this.shop_type_id = shop_type_id;
        this.id = id;
        this.district_id = district_id;
        this.vendor_id = vendor_id;
        this.delivery_cost = delivery_cost;
        this.delivery_time = delivery_time;
        ShopRate = shopRate;
        this.statework = statework;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDetails() {
        return shopDetails;
    }

    public void setShopDetails(String shopDetails) {
        this.shopDetails = shopDetails;
    }

    public String getShop_type_id() {
        return shop_type_id;
    }

    public void setShop_type_id(String shop_type_id) {
        this.shop_type_id = shop_type_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public float getDelivery_cost() {
        return delivery_cost;
    }

    public void setDelivery_cost(float delivery_cost) {
        this.delivery_cost = delivery_cost;
    }

    public int getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(int delivery_time) {
        this.delivery_time = delivery_time;
    }

    public float getShopRate() {
        return ShopRate;
    }

    public void setShopRate(float shopRate) {
        ShopRate = shopRate;
    }

    public boolean isStatework() {
        return statework;
    }

    public void setStatework(boolean statework) {
        this.statework = statework;
    }
}
