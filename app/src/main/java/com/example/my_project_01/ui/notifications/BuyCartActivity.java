package com.example.my_project_01.ui.notifications;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_project_01.LoginActivity;
import com.example.my_project_01.R;
import com.example.my_project_01.pojo.Cart;
import com.example.my_project_01.pojo.CartNum;
import com.example.my_project_01.pojo.Goods;
import com.example.my_project_01.pojo.User;
import com.example.my_project_01.ui.notifications.member.WalletActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyCartActivity extends AppCompatActivity {

    private ImageView buy_cart_img_pay;
    private TextView buy_cart_total;
    private RecyclerView buy_cart_rv;
    private CheckBox buy_cart_all_check;

    private DatabaseReference goodsRef, cartRef, cartNumRef, userRef;
    private List<User> userList = new ArrayList<>();
    private List<Goods> goodsList = new ArrayList<>();
    private List<CartNum> cartNumList = new ArrayList<>();

    private BuyCartAdapter2 buyCartAdapter2;
    private CartNum cartNum;
    private long balance = 0;
    private int total = 0;
    private String cost;
    private boolean checkedAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_cart);
        findId();

        buy_cart_img_pay.setOnClickListener(cart_pay);
        buy_cart_rv.setLayoutManager(new LinearLayoutManager(this));

//        String userId = getIntent().getStringExtra("userId");
//        Log.v("00099", userId);

        userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    userList.add(user);
                }
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getUserId().equals(LoginActivity.user_ID)) {
                        balance = userList.get(i).getBalance();
                        Log.e("00070", "balance=" + balance);
                        cost = userList.get(i).getCost();
                        Log.e("00070", "balance=" + cost);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        goodsRef = FirebaseDatabase.getInstance().getReference("goods");
        goodsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Goods goods = ds.getValue(Goods.class);
                    goodsList.add(goods);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cartRef = FirebaseDatabase.getInstance().getReference("cart");
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Cart cartData = ds.getValue(Cart.class);
                    if (LoginActivity.user_ID.equals(cartData.getUserId())) {
                        Log.v("00098", "userId" + LoginActivity.user_ID);
                        Log.v("00098", "cartData.getUserId()" + cartData.getUserId());

                        cartNumRef = FirebaseDatabase.getInstance().getReference("cart").child(LoginActivity.user_ID).child("cartNum");
                        cartNumRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    cartNum = ds.getValue(CartNum.class);
                                    cartNumList.add(cartNum);
                                }
                                buyCartAdapter2 = new BuyCartAdapter2(goodsList, cartNumList, checkedAll, BuyCartActivity.this);
                                buy_cart_rv.setAdapter(buyCartAdapter2);

                                //checkbox全選
                                buy_cart_all_check.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (buy_cart_all_check.isChecked()) {
                                            buyCartAdapter2.selectAll();
                                        } else {
                                            buyCartAdapter2.un_selectAll();
                                            buy_cart_total.setText("0");
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        cartNumRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                if (BuyCartAdapter2.checked == false) {
                                    Log.e("00098", "checked=" + checkedAll);
                                    buy_cart_all_check.setChecked(false);
                                    buy_cart_total.setText("0");
                                }

                                if (snapshot.getValue() != null) {
                                    cartNum = snapshot.getValue(CartNum.class);
                                    for (int i = 0; i < cartNumList.size(); i++) {

                                        if (cartNumList.get(i).getProductId() == cartNum.getProductId()) {
                                            Log.e("00069", "cartNumList." + cartNumList.get(i).getNum());
                                            Log.e("00069", "cartNum" + cartNum.getNum());
                                            cartNumList.remove(i);
                                            cartNumList.add(i, cartNum);
                                            break;
                                        }
                                    }
                                }

                                List<Integer> totalList = new ArrayList<>();
                                for (int i = 0; i < cartNumList.size(); i++) {
                                    boolean dataCheck = cartNumList.get(i).isChecked();
                                    Log.v("00097", "check" + i + "=" + dataCheck);
                                    int tol_price = cartNumList.get(i).getNum() * cartNumList.get(i).getPrice();

                                    Log.v("00096", "" + cartNumList.get(i).getNum());
                                    Log.v("00095", "" + cartNumList.get(i).getPrice());
                                    Log.v("00094", "tol_price=" + tol_price);

                                    if (dataCheck == true) {
                                        totalList.add(tol_price);
                                        for (int j = 0; j < totalList.size(); j++) {
                                            Log.v("00093", "totalList=" + totalList.size());
                                            Log.v("00093", "totalList_tol_P=" + totalList.get(j).toString());
                                        }
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                            total = totalList.stream().reduce(Integer::sum).orElse(0);
                                        }
                                        buy_cart_total.setText("" + total);
                                    }
                                }
                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

//        /**
//         * 讀取購物車資料
//         */
//
//        MyBuyCarAdapter myBuyCarAdapter =new MyBuyCarAdapter();
//
//        Map<String,CartNum>cartNumMap = new HashMap<>();
//        buyCartRef = FirebaseDatabase.getInstance().getReference("cart");
//        buyCartRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.e("", "snapshot: "+snapshot);
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    Cart cartData = ds.getValue(Cart.class);
//                    Log.e("0000099", "userId: "+userId);
//                    Log.e("0000088", "cartDataId: "+cartData.getUserId());
//
//                    if (userId.equals(cartData.getUserId())){
//                        cartNumReference = FirebaseDatabase.getInstance().getReference("cart").child(userId).child("cartNum");
//                        cartNumReference.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                cartNumMap.clear();
//                                for (DataSnapshot ds : snapshot.getChildren()) {
//                                    CartNum cartNum = ds.getValue(CartNum.class);
//                                    cartNumMap.put("product"+cartNum.getProductId(),cartNum);
//                                }
//                                myBuyCarAdapter.setCartNumMap(cartNumMap);
//
//                                int total = 0;
//                                for (CartNum cartNum : cartNumMap.values()) {
//                                    total += cartNum.getPrice() * cartNum.getNum();
//                                }
//                                buy_cart_total.setText("" + total);
//
//
//                                /**
//                                 * 查詢用戶
//                                 */
//
//                                usersReference = FirebaseDatabase.getInstance().getReference("users");
//                                usersReference.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                        for (DataSnapshot ds : snapshot.getChildren()) {
//                                            User usersData = ds.getValue(User.class);
//                                            if (userId.equals(usersData.getUserId())) {
//                                                user = usersData;
//                                            }
//                                        }
//
//                                        myBuyCarAdapter.setUser(user);
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//
//                                /**
//                                 * 查詢商品
//                                 */
////                                ArrayList<FetchData> fetchDataList = new ArrayList<>();
//                                List<Goods> goodsList = new ArrayList<>();
//                                buyListReference = FirebaseDatabase.getInstance().getReference("goods");
//                                buyListReference.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        for (DataSnapshot ds : snapshot.getChildren()) {
//                                            Goods goods = ds.getValue(Goods.class);
//                                            for (CartNum cartNum : cartNumMap.values()) {
//                                                if (goods.getGoodsID() == cartNum.getProductId()) {
//                                                    goodsList.add(goods);
//                                                }
//                                            }
//
//                                            myBuyCarAdapter.setGoodsList(goodsList);
//                                        }
//
//                                        buy_cart_rv.setAdapter(myBuyCarAdapter);
//                                    }
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//                                    }
//                                });
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//
    }

    private void findId() {
        buy_cart_rv = findViewById(R.id.buy_cart_rv);
        buy_cart_total = findViewById(R.id.buy_cart_total);
        buy_cart_img_pay = findViewById(R.id.buy_cart_img_pay);
        buy_cart_all_check = findViewById(R.id.buy_cart_all_check);
    }

    /**
     * 結帳按鈕
     */
    View.OnClickListener cart_pay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            buy_cart_img_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long bal = balance;
                    Log.e("0000077", "" + bal);
                    AlertDialog.Builder builder = new AlertDialog.Builder(BuyCartActivity.this);
                    builder.setTitle("是否確定購買");
                    builder.setMessage("錢包餘額: " + bal);
                    builder.setNegativeButton("確定購買", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            int total = Integer.parseInt(buy_cart_total.getText().toString());
                            Log.e("0000078", "" + total);

                            long last_balance = bal - total;
                            Log.e("0000077", "" + last_balance);

                            if (last_balance <= 0) {
                                Toast.makeText(BuyCartActivity.this, "餘額不足，請充值", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(BuyCartActivity.this, WalletActivity.class));
                            } else {
                                //計算消費總額
                                int now_cost = Integer.parseInt(cost);
                                int new_cost = now_cost + total;
                                String str_new_cost = String.valueOf(new_cost);

                                HashMap<String, Object> new_user_balance = new HashMap<>();
                                new_user_balance.put("balance", last_balance);
                                new_user_balance.put("cart", "");
                                new_user_balance.put("cost", str_new_cost);
                                //清空購物車
                                userRef.child(LoginActivity.user_ID).updateChildren(new_user_balance);
                                cartRef.child(LoginActivity.user_ID).child("cartNum").removeValue();
                                //刷新購物車
                                startActivity(new Intent(BuyCartActivity.this, BuyCartActivity.class));
                                finish();
                                overridePendingTransition(0, 0);

                                Toast.makeText(BuyCartActivity.this, "購買成功!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setPositiveButton("取消", null);
                    builder.show();
                }
            });
        }
    };
}