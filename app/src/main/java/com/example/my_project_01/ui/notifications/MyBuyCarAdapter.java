package com.example.my_project_01.ui.notifications;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_project_01.R;
import com.example.my_project_01.pojo.Cart;
import com.example.my_project_01.pojo.CartNum;
import com.example.my_project_01.pojo.FetchData;
import com.example.my_project_01.pojo.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBuyCarAdapter extends RecyclerView.Adapter<MyBuyCarAdapter.MyCartViewHolder> {

    private ArrayList<FetchData> fetchDataList;
    private ArrayList<CartNum> cartNumList;
    private User user;
    private DatabaseReference buyCartReference, usersReference;

    public MyBuyCarAdapter() {
    }

    public void setFetchDataList(ArrayList<FetchData> fetchDataList) {
        this.fetchDataList = fetchDataList;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCartNumList(ArrayList<CartNum> cartNumList) {
        this.cartNumList = cartNumList;
    }

    public ArrayList<FetchData> getFetchDataList() {
        return fetchDataList;
    }

    public ArrayList<CartNum> getCartNumList() {
        return cartNumList;
    }

    public User getUser() {
        return user;
    }

    public static class MyCartViewHolder extends RecyclerView.ViewHolder {
        private TextView buy_cart_title, buy_cart_price, buy_cart_num;
        private ImageView buy_cart_pic, buy_cart_add, buy_cart_sub;

        public MyCartViewHolder(@NonNull View itemView) {
            super(itemView);
            buy_cart_title = itemView.findViewById(R.id.buy_cart_title);
            buy_cart_price = itemView.findViewById(R.id.buy_cart_price);
            buy_cart_pic = itemView.findViewById(R.id.buy_cart_pic);
            buy_cart_num = itemView.findViewById(R.id.buy_cart_num);
            buy_cart_add = itemView.findViewById(R.id.buy_cart_add);
            buy_cart_sub = itemView.findViewById(R.id.buy_cart_sub);
        }
    }

    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_cart_style, parent, false);
        MyCartViewHolder myCartViewHolder = new MyCartViewHolder(view);

        return myCartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        buyCartReference = FirebaseDatabase.getInstance().getReference("cart");
        usersReference = FirebaseDatabase.getInstance().getReference("users");
            MyCartViewHolder myCartViewHolder = (MyCartViewHolder) holder;
        Log.e("0000030", "position: "+ position);
        Log.e("0000030", "detchData: "+ fetchDataList);
        Log.e("0000030", "cartNum: "+ cartNumList);
            FetchData fetchData = fetchDataList.get(position);
            Glide.with(myCartViewHolder.buy_cart_pic.getContext()).load(fetchData.getImage()).into(myCartViewHolder.buy_cart_pic);
            myCartViewHolder.buy_cart_title.setText(fetchData.getTitle());

            CartNum cartNum = cartNumList.get(position);
            Log.e("0000030", "Product" +cartNum.getProduct());
            Log.e("0000030", "Price" + cartNum.getPrice());
            Log.e("0000030", "Num" + cartNum.getNum());
            myCartViewHolder.buy_cart_num.setText("" + cartNum.getNum());
            myCartViewHolder.buy_cart_price.setText("" + (cartNum.getPrice() * cartNum.getNum()));

            /**
             * 商品增加鍵
             */
            myCartViewHolder.buy_cart_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String getNum = myCartViewHolder.buy_cart_num.getText().toString();
                    int num = Integer.parseInt(getNum);
                    String product = String.valueOf(cartNum.getProduct());
                    num++;
                    myCartViewHolder.buy_cart_num.setText("" + num);
                    String price =fetchData.getPrice();
                    myCartViewHolder.buy_cart_price.setText("" + Integer.parseInt(price) * num);
                    int new_num = Integer.parseInt(myCartViewHolder.buy_cart_num.getText().toString().trim());
                    HashMap<String, Object> new_data = new HashMap<>();
                    new_data.put("num", new_num);
                    buyCartReference.child(user.getUserId()).child("cartNum").child(product).updateChildren(new_data);
                }
            });

            /**
             * 商品減少鍵
             */
            myCartViewHolder.buy_cart_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String getNum = myCartViewHolder.buy_cart_num.getText().toString();
                    int num = Integer.parseInt(getNum);
                    String product = String.valueOf(cartNum.getProduct());
                    num--;
                    myCartViewHolder.buy_cart_num.setText("" + num);
                    String price =fetchData.getPrice();
                    myCartViewHolder.buy_cart_price.setText("" + Integer.parseInt(price) * num);
                    int new_num = Integer.parseInt(myCartViewHolder.buy_cart_num.getText().toString().trim());
                    HashMap<String, Object> new_data = new HashMap<>();
                    new_data.put("num", new_num);
                    buyCartReference.child(user.getUserId()).child("cartNum").child(product).updateChildren(new_data);

                    if (new_num == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("是否刪除購物車內容:");
                        builder.setMessage("");
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                buyCartReference.child(user.getUserId()).child("cartNum").child(product).removeValue();
                            }
                        });
                        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myCartViewHolder.buy_cart_num.setText("1");
                                String price = fetchData.getPrice();
                                myCartViewHolder.buy_cart_price.setText(""+price);
                            }
                        });
                        builder.show();
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return cartNumList.size();
    }

    @Override
    public String toString() {
        return "MyBuyCarAdapter{" +
                "fetchDataList=" + fetchDataList +
                ", cartNumList=" + cartNumList +
                ", user=" + user +
                '}';
    }
}
