package com.example.my_project_01.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.my_project_01.R;

import com.example.my_project_01.pojo.Cart;
import com.example.my_project_01.pojo.CartNum;
import com.example.my_project_01.pojo.Goods;
import com.example.my_project_01.ui.notifications.BuyCartActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyList_inStyle extends AppCompatActivity {

    private DatabaseReference goodsRef, buyCartRef, cartNumRef;
    private ImageView buy_in_image;
    private TextView buy_in_title, buy_in_price, buy_in_introduce, buy_in_category, buy_in_detail, buy_in_cart, buy_in_buy;

    private List<Cart> cartDataList = new ArrayList<>();
    private List<CartNum> cartNumDataList = new ArrayList<>();

    private String userId, goods_id;
    private String imgUrl, title, price, introduce, cate, detail;

    private List<Goods> goodsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buylist_in_style);
        findId();
        goods_id = getIntent().getStringExtra("goods_id");
        Log.e("0000010", "goods_id" + goods_id);
        userId = getIntent().getStringExtra("userId");
        Log.e("0000011", "userId" + userId);

        goodsRef = FirebaseDatabase.getInstance().getReference("goods");
        goodsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Goods goods = ds.getValue(Goods.class);
                    goodsList.add(goods);
                    Log.e("0000012", "goodsList=" + goodsList);
                }
                for (int i = 0; i < goodsList.size(); i++) {
                    Log.e("00092", "" + goodsList.size());
                    if (goodsList.get(i).getGoodsID().equals(goods_id)) {

                        imgUrl = goodsList.get(i).getGoodsImageUrl();
                        title = goodsList.get(i).getGoodsTitle();
                        price = goodsList.get(i).getGoodsPrice();
                        introduce = goodsList.get(i).getGoodsIntroduce();
                        detail = goodsList.get(i).getGoodsDetail();
                        cate = goodsList.get(i).getGoodsCategory();

                        buy_in_title.setText(title);
                        buy_in_price.setText(price);
                        buy_in_introduce.setText(introduce);
                        buy_in_category.setText(cate);
                        buy_in_detail.setText(detail);

                        Glide.with(BuyList_inStyle.this).load(imgUrl).into(buy_in_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cartNumRef = FirebaseDatabase.getInstance().getReference("cart").child(userId).child("cartNum");
        cartNumRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CartNum cartNum = ds.getValue(CartNum.class);
                    cartNumDataList.add(cartNum);
                }

                for (int i = 0; i < cartNumDataList.size(); i++) {
                    Log.e("959595", "" + cartNumDataList.get(i).getProductId());
                    Log.e("959596", "" + cartNumDataList.get(i).getPrice());
                    Log.e("959597", "" + cartNumDataList.get(i).getNum());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buyCartRef = FirebaseDatabase.getInstance().getReference("cart");
        buyCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Cart cartInCart = ds.getValue(Cart.class);
                    cartDataList.add(cartInCart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buy_in_cart.setOnClickListener(add_cart);
        buy_in_buy.setOnClickListener(add_go_cart);


    }


    private void findId() {
        buy_in_image = findViewById(R.id.buy_in_image);
        buy_in_title = findViewById(R.id.buy_in_title);
        buy_in_price = findViewById(R.id.buy_in_price);
        buy_in_introduce = findViewById(R.id.buy_in_introduce);
        buy_in_category = findViewById(R.id.buy_in_category);
        buy_in_detail = findViewById(R.id.buy_in_detail);
        buy_in_cart = findViewById(R.id.buy_in_cart);
        buy_in_buy = findViewById(R.id.buy_in_buy);
    }

    /**
     * 直接購買
     */
    View.OnClickListener add_go_cart = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            add_cart();
            Bundle bundle = new Bundle();
            bundle.putString("userId", userId);
            finish();
            Intent intent = new Intent(BuyList_inStyle.this, BuyCartActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    /**
     * 加入購物車
     */
    View.OnClickListener add_cart = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            add_cart();
        }
    };

    private void add_cart() {

        int num = 0;
        int add_price = Integer.parseInt(price);
        if (cartNumDataList.isEmpty()) {
            //購物車為空
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            buyCartRef.child(userId).setValue(map);

            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("num", 1);
            map1.put("price", add_price);
            map1.put("productId", goods_id);
            cartNumRef.child(goods_id).setValue(map1);

            Toast.makeText(BuyList_inStyle.this, "加入購物車成功!", Toast.LENGTH_SHORT).show();
//            finish();

        } else {
            Boolean add_cart_repeat = false;
            Boolean add_cart_repeat2 = true;
            for (Cart cart : cartDataList) {
                System.out.println("cart:" + cart);
                Log.v("00060", "cart=" + cart);
                if (cart.getUserId().equals(userId)) {
                    Log.v("00061", "cart.getUserId()=" + cart.getUserId() + "userId=" + userId);

                    for (CartNum cartNum : cartNumDataList) {
                        if (cartNum.getProductId().equals(goods_id)) {
                            add_cart_repeat = true;
                            Log.v("00068", "add_cart_repeat=" + add_cart_repeat);
                        } else {
                            add_cart_repeat2 = false;
                            Log.v("00067", "add_cart_repeat2==" + add_cart_repeat2);
                        }
                    }
                    if (add_cart_repeat == true) {

                        Log.v("00069", "重複商品=" + add_cart_repeat);
                        for (int i = 0; i < cartNumDataList.size(); i++) {
                            if (cartNumDataList.get(i).getProductId().equals(goods_id)) {
                                num = cartNumDataList.get(i).getNum();
                            }
                        }
                        Log.e("00064", "num=" + num);
                        int new_num = num + 1;
                        Toast.makeText(this, "購物車已有相同商品，目前數量為:" + new_num, Toast.LENGTH_SHORT).show();
                        Log.e("00065", "new_num=" + new_num);
                        HashMap<String, Object> map2 = new HashMap();
                        map2.put("num", new_num);
                        cartNumRef.child(goods_id).updateChildren(map2);
//                        finish();

                    } else if (add_cart_repeat2 == false) {

                        Log.v("00065", "不重複商品==" + add_cart_repeat2);
                        int price = 0;
                        for (int i = 0; i < goodsList.size(); i++) {
                            if (goods_id.equals(goodsList.get(i).getGoodsID())) {
                                price = Integer.parseInt(goodsList.get(i).getGoodsPrice());
                                Log.e("00066", "price=" + price);
                            }
                        }
                        Log.e("00067", "price=" + price);
                        HashMap<String, Object> map3 = new HashMap<>();
                        map3.put("num", 1);
                        map3.put("price", add_price);
                        map3.put("productId", goods_id);
                        cartNumRef.child(goods_id).setValue(map3);
                        Toast.makeText(BuyList_inStyle.this, "加入購物車成功!", Toast.LENGTH_SHORT).show();
//                        finish();

                    }
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 什麼都不用寫
        } else {
            // 什麼都不用寫
        }
    }
}