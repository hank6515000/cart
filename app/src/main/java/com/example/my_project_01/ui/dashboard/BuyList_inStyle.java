package com.example.my_project_01.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.my_project_01.R;

import com.example.my_project_01.pojo.Cart;
import com.example.my_project_01.pojo.CartNum;
import com.example.my_project_01.pojo.FetchData;
import com.example.my_project_01.pojo.User;
import com.example.my_project_01.ui.notifications.BuyCartActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuyList_inStyle extends AppCompatActivity {
    private ArrayList<FetchData> fetchDataList;
    private DatabaseReference buyListReference,usersReference,buyCartReference,cartNumReference;
    private ImageView buy_in_image;
    private TextView buy_in_til, buy_in_det, buy_in_pri, buy_in_cart, buy_in_buy;
    private int position;
    private ArrayList<Cart> cartDataArrayList = new ArrayList<>();
    private ArrayList<CartNum> cartNumDataArrayList = new ArrayList<>();
    private String userId;
    private boolean hasCart;
    private int count;
    private int product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buylist_in_style);
        findId();
        position = getIntent().getIntExtra("position", 0);
        Log.e("0000000", "" + position);

        userId = getIntent().getStringExtra("userId");
        buy_in_cart.setOnClickListener(l1);
        buy_in_buy.setOnClickListener(l2);
        fetchDataList = new ArrayList<>();

        buyListReference = FirebaseDatabase.getInstance().getReference("recyclerview");
        /**
         * 商品內容設置
         */
        buyListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FetchData fetchData = ds.getValue(FetchData.class);
                    fetchDataList.add(fetchData);
                }
                buy_in_til.setText(fetchDataList.get(position).getTitle());
                buy_in_pri.setText(fetchDataList.get(position).getPrice());
                Glide.with(buy_in_image.getContext()).load(fetchDataList.get(position).getImage()).into(buy_in_image);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cartNumReference = FirebaseDatabase.getInstance().getReference("cart").child(userId).child("cartNum");
        cartNumReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CartNum cartNum = ds.getValue(CartNum.class);
                    if (cartNum.getProduct() == fetchDataList.get(position).getId()) {
                        count = cartNum.getNum()+1;
                        product = cartNum.getProduct();
                    }else {
                        product = 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buyCartReference = FirebaseDatabase.getInstance().getReference("cart");
        buyCartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    System.out.println(ds);
                    Cart cartInCart = ds.getValue(Cart.class);
                    System.out.println(cartInCart);
                    if (cartInCart.getUserId().equals(userId)){
                        hasCart = true;
                    }else {
                        hasCart = false;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findId() {
        buy_in_image = findViewById(R.id.buy_in_image);
        buy_in_til = findViewById(R.id.buy_in_til);
        buy_in_det = findViewById(R.id.buy_in_det);
        buy_in_cart = findViewById(R.id.buy_in_cart);
        buy_in_buy = findViewById(R.id.buy_in_buy);
        buy_in_pri = findViewById(R.id.buy_in_pri);
    }

    /**
     * 直接購買
     */
    View.OnClickListener l2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            add_cart();
            finish();
            Intent intent = new Intent(BuyList_inStyle.this, BuyCartActivity.class);
            startActivity(intent);
        }
    };

    /**
     * 加入購物車
     */
    View.OnClickListener l1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            add_cart();

        }
    };
    private void add_cart() {
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        if (product == 0){
            if (hasCart){
                System.out.println("沒有product有cart");
                CartNum newCartNum = new CartNum();
                newCartNum.setNum(1);
                newCartNum.setProduct(fetchDataList.get(position).getId());
                newCartNum.setPrice(Integer.parseInt(fetchDataList.get(position).getPrice()));
                buyCartReference.child(userId).setValue(newCart);
                cartNumReference.child(String.valueOf(position+1)).setValue(newCartNum);
            }else {
                System.out.println("沒有product沒有cart");
                Map<String, Object> map = new HashMap<>();
                map.put("num",1);
                map.put("price",Integer.parseInt(fetchDataList.get(position).getPrice()));
                map.put("product",fetchDataList.get(position).getId());
                cartNumReference.child(String.valueOf(position+1)).setValue(map);
            }
        }else {
            System.out.println("有product");
            Map<String, Object> map = new HashMap<>();
            map.put("num",count);
            buyCartReference.child(userId).child("cartNum").child(String.valueOf(position+1)).updateChildren(map);
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