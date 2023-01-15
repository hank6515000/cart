package com.example.my_project_01.pojo;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cart {

    public String username;
    public String userId;
    public List<CartNum> cartNum;

    public Cart() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartNum> getCartNum() {
        return cartNum;
    }

    public void setCartNum(List<CartNum> cartNum) {
        this.cartNum = cartNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                ", cartnum=" + cartNum +
                '}';
    }
}

