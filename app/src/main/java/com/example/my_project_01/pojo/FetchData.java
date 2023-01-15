package com.example.my_project_01.pojo;


public class FetchData{
    String image, title, det, price;
    private int id;

    public FetchData() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDet() {
        return det;
    }

    public void setDet(String det) {
        this.det = det;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "FetchData{" +
                "image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", det='" + det + '\'' +
                ", price='" + price + '\'' +
                ", id=" + id +
                '}';
    }
}
