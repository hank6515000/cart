package com.example.my_project_01.pojo;

public class CartNum {
    public int product;
    public int price, num;

    public CartNum() {
    }

    public CartNum(int product, int price, int num) {
       this.product = product;
        this.price = price;
        this.num = num;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
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

    @Override
    public String toString() {
        return "CartNum{" +
                "product=" + product +
                ", price=" + price +
                ", num=" + num +
                '}';
    }
}
