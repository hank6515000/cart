package com.example.my_project_01.ui.dashboard;

import androidx.lifecycle.ViewModel;

public class BuyListViewModel extends ViewModel {
private String title;
private int image;


    public BuyListViewModel(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
}