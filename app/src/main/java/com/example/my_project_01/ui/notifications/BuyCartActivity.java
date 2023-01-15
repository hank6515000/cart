package com.example.my_project_01.ui.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my_project_01.ConnUtils;
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

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyCartActivity extends AppCompatActivity {

    private DatabaseReference buyListReference, usersReference, buyCartReference , cartNumReference ;
    private ImageView buy_cart_img_pay;
    private TextView buy_cart_total;
    private RecyclerView buy_cart_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_cart);
        findId();
        buy_cart_rv.setLayoutManager(new LinearLayoutManager(this));

        String userId = getIntent().getStringExtra("userId");
        /**
         * 讀取購物車資料
         */

        MyBuyCarAdapter myBuyCarAdapter =new MyBuyCarAdapter();

        ArrayList<CartNum> cartNumList = new ArrayList<>();
        buyCartReference = FirebaseDatabase.getInstance().getReference("cart");
        buyCartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Cart cartData = ds.getValue(Cart.class);
                        if (userId.equals(cartData.getUserId())){
                            cartNumReference = FirebaseDatabase.getInstance().getReference("cart").child(userId).child("cartNum");
                            cartNumReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    cartNumList.clear();
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        CartNum cartNum = ds.getValue(CartNum.class);
                                        cartNumList.add(cartNum);
                                    }

                                    myBuyCarAdapter.setCartNumList(cartNumList);
                                    int total = 0;
                                    for (CartNum cartNum : cartNumList) {
                                        total += cartNum.getPrice() * cartNum.getNum();
                                    }
                                    buy_cart_total.setText("" + total);


                                    /**
                                     * 查詢用戶
                                     */

                                    usersReference = FirebaseDatabase.getInstance().getReference("users");
                                    usersReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User user = null;
                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                User usersData = ds.getValue(User.class);
                                                if (userId.equals(usersData.getUserId())) {
                                                    user = usersData;
                                                }
                                            }

                                            myBuyCarAdapter.setUser(user);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    /**
                                     * 查詢商品
                                     */
                                    ArrayList<FetchData> fetchDataList = new ArrayList<>();
                                    buyListReference = FirebaseDatabase.getInstance().getReference("recyclerview");
                                    buyListReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                FetchData fetchData = ds.getValue(FetchData.class);
                                                for (CartNum cartNum : cartNumList) {
                                                    if (fetchData.getId() == cartNum.getProduct()) {
                                                        fetchDataList.add(fetchData);
                                                    }
                                                }

                                                myBuyCarAdapter.setFetchDataList(fetchDataList);
                                            }

                                            buy_cart_rv.setAdapter(myBuyCarAdapter);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        //    buy_cart_rv.setAdapter(myBuyCarAdapter);
        /**
         * 結帳按鈕
         */
//        buy_cart_img_pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(BuyCartActivity.this, "PAY~PAY", Toast.LENGTH_SHORT).show();
//                int bal = usersDataList.get(0).getBalance();
//                Log.e("0000077",""+bal);
//                AlertDialog.Builder builder = new AlertDialog.Builder(BuyCartActivity.this);
//                builder.setTitle("是否確定購買");
//                builder.setMessage("錢包餘額: "+bal);
//                builder.setNegativeButton("確定購買", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String user = "1";
//                        int total = Integer.parseInt(buy_cart_total.getText().toString());
//                        Log.e("0000078",""+total);
//                        int last_balance = bal-total;
//                        Log.e("0000077",""+last_balance);
//                        HashMap<String, Object> new_user_balance = new HashMap<>();
//                        new_user_balance.put("balance", last_balance);
//                        new_user_balance.put("cart", "");
//
//                        //清空購物車
//                        usersReference.child(user).updateChildren(new_user_balance);
//                        buyCartReference.child("0").child("cartnum").removeValue();
//                        //刷新購物車
//                        startActivity(new Intent(BuyCartActivity.this, BuyCartActivity.class));
//                        finish();
//                        overridePendingTransition(0, 0);
//
//                        Toast.makeText(BuyCartActivity.this, "購買成功!!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                builder.setPositiveButton("取消",null);
//                builder.show();
//            }
//        });
    }

    private void findId() {
        buy_cart_rv = findViewById(R.id.buy_cart_rv);
        buy_cart_total = findViewById(R.id.buy_cart_total);
        buy_cart_img_pay = findViewById(R.id.buy_cart_img_pay);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}