package com.example.my_project_01.ui.notifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_project_01.LoginActivity;
import com.example.my_project_01.R;
import com.example.my_project_01.pojo.CartNum;
import com.example.my_project_01.pojo.Goods;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyCartAdapter2 extends RecyclerView.Adapter<BuyCartAdapter2.BuyCartViewHolder> {
    private List<Goods> goodsList;
    private List<CartNum> cartNumList;
    private DatabaseReference cartRef, usersRef;
    private Context context;
    private Boolean checkedAll;

    private String user_id = LoginActivity.user_ID;


    public static boolean checked=false;

    public BuyCartAdapter2(List<Goods> goodsList, List<CartNum> cartNumList, Boolean checkedAll, Context context) {
        this.goodsList = goodsList;
        this.cartNumList = cartNumList;
        this.checkedAll = checkedAll;
        this.context = context;
    }

    public void selectAll(){
        checkedAll =true;
        notifyDataSetChanged();
    }
    public void un_selectAll(){
        checkedAll =false;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public BuyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_cart_style, parent, false);
        BuyCartViewHolder buyCartViewHolder = new BuyCartViewHolder(view);
        return buyCartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BuyCartViewHolder holder, int position) {
        Log.e("00088", ""+ checkedAll);
        cartRef = FirebaseDatabase.getInstance().getReference("cart");
        usersRef = FirebaseDatabase.getInstance().getReference("users");


        Log.e("00083", user_id);

        BuyCartViewHolder buyCartViewHolder = (BuyCartViewHolder) holder;
        Goods goods = new Goods();

        Log.e("00082", "position=" + position);


        for (int i = 0; i < goodsList.size(); i++) {

            if (cartNumList.get(position).getProductId().equals(goodsList.get(i).getGoodsID())) {
                Log.e("00080", "cartNumList.get(position).getProductId()" + cartNumList.get(position).getProductId());
                Log.e("00081", "goodsList.get(i).getGoodsID()" + goodsList.get(i).getGoodsID());
                goods = goodsList.get(i);
            }
        }

        buyCartViewHolder.buy_cart_title.setText(goods.getGoodsTitle());
        Glide.with(buyCartViewHolder.buy_cart_pic.getContext()).load(goods.getGoodsImageUrl()).into(buyCartViewHolder.buy_cart_pic);
        int now_num = cartNumList.get(position).getNum();
        int price = cartNumList.get(position).getPrice();
        int now_price = now_num * price;
        String product = cartNumList.get(position).getProductId();

        buyCartViewHolder.buy_cart_num.setText("" + now_num);
        buyCartViewHolder.buy_cart_price.setText("" + now_price);

        buyCartViewHolder.buy_cart_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int getNum = Integer.parseInt(buyCartViewHolder.buy_cart_num.getText().toString().trim());
//                int getNum=cartNumList.get(position).getNum();
                getNum++;

                buyCartViewHolder.buy_cart_num.setText("" + getNum);
                buyCartViewHolder.buy_cart_price.setText("" + price * getNum);

                HashMap<String, Object> new_data = new HashMap<>();
                new_data.put("num", getNum);
                cartRef.child(user_id).child("cartNum").child(product).updateChildren(new_data);

              //  notifyDataSetChanged();

//                Intent intent = new Intent(buyCartViewHolder.itemView.getContext(), BuyCartActivity.class);
//                ((Activity) context).finish();
//                ((Activity) context).startActivity(intent);
//                ((Activity) context).overridePendingTransition(0, 0);

            }
        });

        buyCartViewHolder.buy_cart_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int getNum = Integer.parseInt(buyCartViewHolder.buy_cart_num.getText().toString().trim());
                getNum--;

                buyCartViewHolder.buy_cart_num.setText("" + getNum);
                buyCartViewHolder.buy_cart_price.setText("" + price * getNum);

                HashMap<String, Object> new_data = new HashMap<>();
                new_data.put("num", getNum);
                cartRef.child(user_id).child("cartNum").child(product).updateChildren(new_data);

//                Intent intent = new Intent(buyCartViewHolder.itemView.getContext(), BuyCartActivity.class);
//                ((Activity) context).finish();
//                ((Activity) context).startActivity(intent);
//                ((Activity) context).overridePendingTransition(0, 0);

                if (getNum == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(buyCartViewHolder.itemView.getContext());
                    builder.setTitle("是否刪除購物車內容:");
                    builder.setMessage("");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            cartRef.child(user_id).child("cartNum").child(product).removeValue();
                            Intent intent = new Intent(buyCartViewHolder.itemView.getContext(), BuyCartActivity.class);
                            ((Activity) context).finish();
                            ((Activity) context).startActivity(intent);
                            ((Activity) context).overridePendingTransition(0, 0);
                        }
                    });
                    builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            buyCartViewHolder.buy_cart_num.setText("1");
                            buyCartViewHolder.buy_cart_price.setText("" + price);
                            Map<String, Object> map = new HashMap<>();
                            map.put("num", 1);
                            cartRef.child(user_id).child("cartNum").child(product).updateChildren(map);
                        }
                    });
                    builder.show();
                }
            }
        });

        if(!checkedAll){
            buyCartViewHolder.buy_cart_ch.setChecked(false);
        }else {
            buyCartViewHolder.buy_cart_ch.setChecked(true);

        }

        if(buyCartViewHolder.buy_cart_ch.isChecked()){
            checked=true;
            Log.e("00092", "checked=" + checked);

            HashMap<String, Object> new_data = new HashMap<>();
            new_data.put("checked", checked);
            cartRef.child(user_id).child("cartNum").child(product).updateChildren(new_data);
        }else {
            checked=false;
            HashMap<String, Object> new_data = new HashMap<>();
            new_data.put("checked", checked);
            cartRef.child(user_id).child("cartNum").child(product).updateChildren(new_data);
        }

        buyCartViewHolder.buy_cart_ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checked=buyCartViewHolder.buy_cart_ch.isChecked();

                HashMap<String, Object> new_data = new HashMap<>();
                new_data.put("checked", checked);
                cartRef.child(user_id).child("cartNum").child(product).updateChildren(new_data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartNumList.size();
    }

    public static class BuyCartViewHolder extends RecyclerView.ViewHolder {
        private TextView buy_cart_title, buy_cart_price, buy_cart_num;
        private ImageView buy_cart_pic, buy_cart_add, buy_cart_sub;
        private CheckBox buy_cart_ch;

        public BuyCartViewHolder(@NonNull View itemView) {
            super(itemView);
            buy_cart_title = itemView.findViewById(R.id.goods_style_title);
            buy_cart_price = itemView.findViewById(R.id.goods_style_price);
            buy_cart_pic = itemView.findViewById(R.id.goods_style_pic);
            buy_cart_num = itemView.findViewById(R.id.buy_cart_num);
            buy_cart_add = itemView.findViewById(R.id.buy_cart_add);
            buy_cart_sub = itemView.findViewById(R.id.buy_cart_sub);
            buy_cart_ch = itemView.findViewById(R.id.buy_cart_ch);
        }
    }
}
