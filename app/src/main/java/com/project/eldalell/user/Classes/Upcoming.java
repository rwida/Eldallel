package com.project.eldalell.user.Classes;

public class Upcoming {
  private String shopName, OrderID, OrderDate, image, shopID, note;
  private boolean OrderStatus;

  public Upcoming(boolean OrderStatus, String OrderID, String OrderDate) {

    this.OrderStatus = OrderStatus;
    this.OrderID = OrderID;
    this.OrderDate = OrderDate;

  }

  public Upcoming() {
  }




  public String getOrderDate() {
    return OrderDate;
  }

  public void setOrderDate(String OrderDate) {
    this.OrderDate = OrderDate;
  }


  public String getOrderID() {
    return OrderID;
  }

  public void setOrderID(String OrderID) {
    this.OrderID = OrderID;
  }

  public boolean isOrderStatus() {
    return OrderStatus;
  }

  public void setOrderStatus(boolean orderStatus) {
    OrderStatus = orderStatus;
  }

  public String getShopName() {
    return shopName;
  }

  public void setShopName(String shopName) {
    this.shopName = shopName;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getShopID() {
    return shopID;
  }

  public void setShopID(String shopID) {
    this.shopID = shopID;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
