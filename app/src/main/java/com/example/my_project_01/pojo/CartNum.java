package com.example.my_project_01.pojo;

public class CartNum {
    public String productId;
    public int price, num;
    public boolean checked;

    public CartNum() {
    }

    public CartNum(String productId, int price, int num, boolean checked) {
        this.productId = productId;
        this.price = price;
        this.num = num;
        this.checked = checked;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "CartNum{" +
                "product=" + productId +
                ", price=" + price +
                ", num=" + num +
                '}';
    }
}
