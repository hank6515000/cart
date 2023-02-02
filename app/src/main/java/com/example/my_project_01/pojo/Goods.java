package com.example.my_project_01.pojo;

public class Goods {
    private String goodsTitle, goodsCategory, goodsID, goodsPrice, goodsIntroduce, goodsDetail, goodsImageUrl;

    public Goods() {
    }

    public Goods(String goodsTitle, String goodsCategory, String goodsID, String goodsPrice, String goodsIntroduce, String goodsDetail, String goodsImageUrl) {
        this.goodsTitle = goodsTitle;
        this.goodsCategory = goodsCategory;
        this.goodsID = goodsID;
        this.goodsPrice = goodsPrice;
        this.goodsIntroduce = goodsIntroduce;
        this.goodsDetail = goodsDetail;
        this.goodsImageUrl = goodsImageUrl;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(String goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsIntroduce() {
        return goodsIntroduce;
    }

    public void setGoodsIntroduce(String goodsIntroduce) {
        this.goodsIntroduce = goodsIntroduce;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public String getGoodsImageUrl() {
        return goodsImageUrl;
    }

    public void setGoodsImageUrl(String goodsImageUrl) {
        this.goodsImageUrl = goodsImageUrl;
    }
}
