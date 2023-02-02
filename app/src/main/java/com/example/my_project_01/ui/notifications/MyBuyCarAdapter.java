package com.example.my_project_01.ui.notifications;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_project_01.R;
import com.example.my_project_01.pojo.CartNum;
import com.example.my_project_01.pojo.Goods;
import com.example.my_project_01.pojo.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyBuyCarAdapter extends RecyclerView.Adapter<MyBuyCarAdapter.MyCartViewHolder> {

    //    private ArrayList<FetchData> fetchDataList;
    private Map<String, CartNum> cartNumMap;
    private User user;
    private DatabaseReference buyCartReference, usersReference;
    private MyCartViewHolder myCartViewHolder;

    List<Goods> goodsList;

    public MyBuyCarAdapter() {
    }


    public void setCartNumMap(Map<String, CartNum> cartNumMap) {
        this.cartNumMap = cartNumMap;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public User getUser() {
        return user;
    }

    public static class MyCartViewHolder extends RecyclerView.ViewHolder {
        private TextView buy_cart_title, buy_cart_price, buy_cart_num;
        private ImageView buy_cart_pic, buy_cart_add, buy_cart_sub;

        public MyCartViewHolder(@NonNull View itemView) {
            super(itemView);
            buy_cart_title = itemView.findViewById(R.id.goods_style_title);
            buy_cart_price = itemView.findViewById(R.id.goods_style_price);
            buy_cart_pic = itemView.findViewById(R.id.goods_style_pic);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyCartViewHolder holder, int position) {
        buyCartReference = FirebaseDatabase.getInstance().getReference("cart");
        usersReference = FirebaseDatabase.getInstance().getReference("users");
        myCartViewHolder = (MyCartViewHolder) holder;
//        Log.e("0000030", "position: "+ position);
//        Log.e("0000030", "fetchData: "+ fetchDataList);
//        Log.e("0000030", "After cartNum: "+ cartNumMap);

//            FetchData fetchData = fetchDataList.get(position);
        Goods goods = goodsList.get(position);
        Glide.with(myCartViewHolder.buy_cart_pic.getContext()).load(goods.getGoodsImageUrl()).into(myCartViewHolder.buy_cart_pic);
        myCartViewHolder.buy_cart_title.setText(goods.getGoodsTitle());

        List<CartNum> cartNumList = new ArrayList<>();

        cartNumMap = cartNumMap.entrySet()        // Set<Entry<String, String>>
                .stream()           // Stream<Entry<String, String>>
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));
        for (CartNum cartNum : cartNumMap.values()) {
            cartNumList.add(cartNum);
        }
        Log.e("0000030", "Before cartNum: " + cartNumList);
        CartNum cartNum = cartNumList.get(position);
        Log.e("0000030", "Product" + cartNum);
        Log.e("0000030", "Price" + cartNum.getPrice());
        Log.e("0000030", "Num" + cartNum.getNum());
        myCartViewHolder.buy_cart_num.setText("" + cartNum.getNum());
//            myCartViewHolder.buy_cart_price.setText("" + (cartNum.getPrice() * cartNum.getNum()));

        /**
         * 商品增加鍵
         */
//            myCartViewHolder.buy_cart_add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int getNum = cartNum.getNum()+1;
//                    Log.e("0000031", "Num" + cartNum);
//
//                    String product = "product"+cartNum.getProductId();
//                    myCartViewHolder.buy_cart_num.setText("" + getNum);
//                    String price =fetchData.getPrice();
//                    myCartViewHolder.buy_cart_price.setText("" + Integer.parseInt(price) * getNum);
//                    int new_num = Integer.parseInt(myCartViewHolder.buy_cart_num.getText().toString().trim());
//                    HashMap<String, Object> new_data = new HashMap<>();
//                    new_data.put("num", new_num);
//                    buyCartReference.child(user.getUserId()).child("cartNum").child(product).updateChildren(new_data);
//                }
//            });
//
//            /**
//             * 商品減少鍵
//             */
//            myCartViewHolder.buy_cart_sub.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    int getNum =cartNum.getNum()-1;
//                    String product = "product"+cartNum.getProductId();
//                    myCartViewHolder.buy_cart_num.setText("" + getNum);
//                    String price =fetchData.getPrice();
//                    myCartViewHolder.buy_cart_price.setText("" + Integer.parseInt(price) * getNum);
//                    int new_num = Integer.parseInt(myCartViewHolder.buy_cart_num.getText().toString().trim());
//                    HashMap<String, Object> new_data = new HashMap<>();
//                    new_data.put("num", new_num);
//                    buyCartReference.child(user.getUserId()).child("cartNum").child(product).updateChildren(new_data);
//
//                    if (new_num == 0) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
//                        builder.setTitle("是否刪除購物車內容:");
//                        builder.setMessage("");
//                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int j) {
//                                buyCartReference.child(user.getUserId()).child("cartNum").child(product).removeValue();
//                            }
//                        });
//                        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Log.e("000050","???");
//                                Log.e("000050","2 "+myCartViewHolder);
//                                myCartViewHolder.buy_cart_num.setText("1");
//                               String price = fetchData.getPrice();
//                               myCartViewHolder.buy_cart_price.setText(""+price);
//                               Map<String,Object>map = new HashMap<>();
//                               map.put("num", 1);
//                                buyCartReference.child(user.getUserId()).child("cartNum").child(product).updateChildren(map);
//                         }
//                        });
//
//                        builder.show();
//                    }
//                }
//            });
    }

    @Override
    public int getItemCount() {
        return cartNumMap.size();
    }

    @Override
    public String toString() {
        return "MyBuyCarAdapter{" +
                "fetchDataList=" + goodsList +
                ", cartNumList=" + cartNumMap +
                ", user=" + user +
                '}';
    }
}
