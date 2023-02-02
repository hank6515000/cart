package com.example.my_project_01;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_project_01.pojo.Goods;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.MyGoodsViewHolder> {
    private List<Goods> goodsList;
    private DatabaseReference goodsRef;

    public AdminAdapter(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    @NonNull
    @Override
    public MyGoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_style, parent, false);
        MyGoodsViewHolder myGoodsViewHolder = new MyGoodsViewHolder(view);
        return myGoodsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyGoodsViewHolder holder, int position) {
        goodsRef = FirebaseDatabase.getInstance().getReference("goods");

        MyGoodsViewHolder myGoodsViewHolder = (MyGoodsViewHolder) holder;
        Goods goods = goodsList.get(position);
        myGoodsViewHolder.goods_style_title.setText(goods.getGoodsTitle());
        myGoodsViewHolder.goods_style_introduce.setText(goods.getGoodsIntroduce());
        myGoodsViewHolder.goods_style_detail.setText(goods.getGoodsDetail());
        myGoodsViewHolder.goods_style_price.setText(goods.getGoodsPrice());
        myGoodsViewHolder.goods_style_category.setText(goods.getGoodsCategory());
        Glide.with(myGoodsViewHolder.goods_style_pic.getContext()).load(goods.getGoodsImageUrl()).into(myGoodsViewHolder.goods_style_pic);


        myGoodsViewHolder.goods_style_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(myGoodsViewHolder.itemView.getContext());
                builder.setTitle("確定要刪除嗎?");
                builder.setMessage("");
                builder.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String goods_id = goods.getGoodsID();
                        Log.e("00099", goods_id);
                        Toast.makeText(myGoodsViewHolder.itemView.getContext(), "030", Toast.LENGTH_SHORT).show();
                        if (!goods_id.equals("")) {
                            goodsRef.child(goods_id).removeValue();
                            myGoodsViewHolder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), AdminGoodsActivity.class));
                            ((Activity) myGoodsViewHolder.itemView.getContext()).finish();
                        }
                    }
                });
                builder.setPositiveButton("取消", null);
                builder.show();
            }
        });

        myGoodsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goods_id = goods.getGoodsID();
                Intent intent = new Intent(holder.itemView.getContext(), Admin_Update_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("goods_id", goods_id);
                intent.putExtras(bundle);
                myGoodsViewHolder.itemView.getContext().startActivity(intent);
                ((Activity) myGoodsViewHolder.itemView.getContext()).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    public static class MyGoodsViewHolder extends RecyclerView.ViewHolder {
        TextView goods_style_title, goods_style_category, goods_style_introduce, goods_style_detail, goods_style_price, goods_style_remove;
        ImageView goods_style_pic;
        ScrollView scrollView;

        public MyGoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            scrollView = itemView.findViewById(R.id.goods_scroll);
            goods_style_title = itemView.findViewById(R.id.goods_style_title);
            goods_style_category = itemView.findViewById(R.id.goods_style_category);
            goods_style_introduce = itemView.findViewById(R.id.goods_style_introduce);
            goods_style_detail = itemView.findViewById(R.id.goods_style_detail);
            goods_style_price = itemView.findViewById(R.id.goods_style_price);
            goods_style_pic = itemView.findViewById(R.id.goods_style_pic);
            goods_style_remove = itemView.findViewById(R.id.goods_style_remove);

            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
        }
    }
}
