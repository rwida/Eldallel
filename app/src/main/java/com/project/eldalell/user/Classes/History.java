package com.project.eldalell.user.Classes;


public class History {

  private String OrderID, OrderDate, ShopID, Image, ShopName, note;
  private boolean OrderStatus;

  public History(boolean OrderStatus, String OrderID, String OrderDate
          , String ShopID, String Image, String ShopName) {
    this.OrderStatus = OrderStatus;
    this.OrderID = OrderID;
    this.ShopID = ShopID;
    this.OrderDate = OrderDate;
    this.Image = Image;
    this.ShopName = ShopName;
  }

  public History() {
  }

  public String getShopID() {
    return ShopID;
  }

  public void setShopID(String shopID) {
    ShopID = shopID;
  }

  public String getImage() {
    return Image;
  }

  public void setImage(String image) {
    Image = image;
  }

  public void setShopName(String shopName) {
    ShopName = shopName;
  }

  public String getShopName() {
    return ShopName;
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

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
