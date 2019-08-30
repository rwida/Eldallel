package com.project.eldalell.user.Classes;

public class ProdectType {
    private String id , item_category_name , image ;

    public ProdectType(String id, String item_category_name, String image) {
        this.id = id;
        this.item_category_name = item_category_name;
        this.image = image;
    }
    public ProdectType() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_category_name() {
        return item_category_name;
    }

    public void setItem_category_name(String item_category_name) {
        this.item_category_name = item_category_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
