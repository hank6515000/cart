package com.example.my_project_01.ui.dashboard;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_project_01.R;
import com.example.my_project_01.pojo.FetchData;

import java.util.List;

public class MyBuyListAdapter extends RecyclerView.Adapter<MyBuyListAdapter.MyViewHolder> {

    private List<FetchData> fetchDataList;
    private String userId;

    public MyBuyListAdapter(List<FetchData> fetchDataList,String userId) {
        this.fetchDataList = fetchDataList;
        this.userId = userId;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_title, textView_detail, textView_price;
        private ImageView imageView_pic;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.buy_cart_title);
            textView_detail = itemView.findViewById(R.id.buy_cart_detail);
            textView_price = itemView.findViewById(R.id.buy_cart_price);
            imageView_pic = itemView.findViewById(R.id.buy_cart_pic);
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


        MyViewHolder viewHolder = (MyViewHolder) holder;
        FetchData fetchData = fetchDataList.get(position);
        viewHolder.textView_title.setText(fetchData.getTitle());
        viewHolder.textView_detail.setText(fetchData.getDet());
        viewHolder.textView_price.setText(fetchData.getPrice());
        Glide.with(viewHolder.imageView_pic.getContext()).load(fetchData.getImage()).into(viewHolder.imageView_pic);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(holder.itemView.getContext(), ""+(position+1), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(holder.itemView.getContext(), BuyList_inStyle.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position",(position));
                bundle.putString("userId",userId);
                intent.putExtras(bundle);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fetchDataList.size();
    }
}
