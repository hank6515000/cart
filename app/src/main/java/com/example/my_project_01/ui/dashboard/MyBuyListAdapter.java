package com.example.my_project_01.ui.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_project_01.R;
import com.example.my_project_01.pojo.Goods;

import java.util.List;

public class MyBuyListAdapter extends RecyclerView.Adapter<MyBuyListAdapter.MyViewHolder> {

    private List<Goods> goodsList;
    private String userId;

    public MyBuyListAdapter(List<Goods> goodsList, String userId) {
        this.goodsList = goodsList;
        this.userId = userId;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView goods_style_title, goods_style_introduce, goods_style_price;
        private ImageView goods_style_pic;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            goods_style_title = itemView.findViewById(R.id.goods_style_title);
            goods_style_introduce = itemView.findViewById(R.id.goods_style_introduce);
            goods_style_price = itemView.findViewById(R.id.goods_style_price);
            goods_style_pic = itemView.findViewById(R.id.goods_style_pic);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buylist_style, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        MyViewHolder viewHolder = (MyViewHolder) holder;
        Goods goods = goodsList.get(position);
        viewHolder.goods_style_title.setText(goods.getGoodsTitle());
        viewHolder.goods_style_introduce.setText(goods.getGoodsIntroduce());
        viewHolder.goods_style_price.setText(goods.getGoodsPrice());

        Glide.with(viewHolder.goods_style_pic.getContext()).load(goods.getGoodsImageUrl()).into(viewHolder.goods_style_pic);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goods_id = goods.getGoodsID();
//                Toast.makeText(holder.itemView.getContext(), "" + (position + 1), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(holder.itemView.getContext(), BuyList_inStyle.class);
                Bundle bundle = new Bundle();
                bundle.putString("goods_id", goods_id);
                bundle.putString("userId", userId);
                intent.putExtras(bundle);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }
}
