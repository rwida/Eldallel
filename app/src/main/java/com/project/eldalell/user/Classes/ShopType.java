package com.project.eldalell.user.Classes;

public class ShopType {
    private String shop_type_name, image, id;

    public ShopType() {
    }

    public ShopType(String shop_type_name, String image, String id) {
        this.id = id;
        this.shop_type_name = shop_type_name;
        this.image = image;
    }

    public String getShop_type_name() {
        return shop_type_name;
    }

    public void setShop_type_name(String shop_type_name) {
        this.shop_type_name = shop_type_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
