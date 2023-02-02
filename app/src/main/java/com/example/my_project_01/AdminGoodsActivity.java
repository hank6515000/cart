package com.example.my_project_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.my_project_01.pojo.Goods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminGoodsActivity extends AppCompatActivity {

    private DatabaseReference goodsRef;
    private List<Goods> goodsList = new ArrayList<>();
    private RecyclerView re_goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_goods);

        re_goods = findViewById(R.id.re_goods);
        re_goods.setLayoutManager(new LinearLayoutManager(this));

        goodsRef = FirebaseDatabase.getInstance().getReference("goods");
        goodsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Goods goods = ds.getValue(Goods.class);
                    goodsList.add(goods);
                }
                AdminAdapter adminAdapter = new AdminAdapter(goodsList);
                re_goods.setAdapter(adminAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}