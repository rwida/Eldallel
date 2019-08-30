package com.project.eldalell.user.Classes;

public class Product {
    private int  ProductExist, num;
    private String ProductName , id, item_id,shop_id,imgProduct,item_category_id;
    private float ProductPrice;

    public Product() {
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Product(String imgProduct, int productExist, String productName, String id, String item_id, String shop_id, float productPrice) {
        this.imgProduct = imgProduct;
        ProductExist = productExist;
        ProductName = productName;
        this.id = id;
        this.item_id = item_id;
        this.shop_id = shop_id;
        ProductPrice = productPrice;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }

    public int getProductExist() {
        return ProductExist;
    }

    public void setProductExist(int productExist) {
        ProductExist = productExist;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public float getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(float productPrice) {
        ProductPrice = productPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getItem_category_id() {
        return item_category_id;
    }

    public void setItem_category_id(String item_category_id) {
        this.item_category_id = item_category_id;
    }
}
