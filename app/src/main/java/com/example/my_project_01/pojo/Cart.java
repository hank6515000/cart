package com.example.my_project_01.pojo;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cart {
    public String userId;
    public Map<String, CartNum> cartNum;

    public Cart() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, CartNum> getCartNum() {
        return cartNum;
    }

    public void setCartNum(Map<String, CartNum> cartNum) {
        this.cartNum = cartNum;
    }

    @Override
    public String toString() {
        return "Cart{" +
                ", userId='" + userId + '\'' +
                ", cartnum=" + cartNum +
                '}';
    }
}

