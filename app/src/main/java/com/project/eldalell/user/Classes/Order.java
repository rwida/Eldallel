package com.project.eldalell.user.Classes;

public class Order {

    private int OrderQuantity = 0;
    private float OrderPrice;
    private float totalOrderPrice;
    private String OrderName;
    private String item_shop_id;
    private String orderID;
    private boolean isOrderState;
    public Order() {
    }

    public Order(int orderQuantity, float orderPrice, String orderName,String item_shop_id) {
        OrderQuantity = orderQuantity;
        OrderPrice = orderPrice;
        OrderName = orderName;
        totalOrderPrice = OrderQuantity * OrderPrice;
        this.item_shop_id = item_shop_id;
    }

    public String getItem_shop_id() {
        return item_shop_id;
    }

    public void setItem_shop_id(String item_shop_id) {
        this.item_shop_id = item_shop_id;
    }

    public float calcTotalOrderPrice() {
        totalOrderPrice = OrderQuantity * OrderPrice;
        return totalOrderPrice;
    }

    public float getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(float totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public boolean isOrderState() {
        return isOrderState;
    }

    public void setOrderState(boolean orderState) {
        isOrderState = orderState;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public int getOrderQuantity() {
        return OrderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        OrderQuantity = orderQuantity;
    }

    public float getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(float orderPrice) {
        OrderPrice = orderPrice;
    }

    public String getOrderName() {
        return OrderName;
    }

    public void setOrderName(String orderName) {
        OrderName = orderName;
    }


}
