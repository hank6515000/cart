package com.example.my_project_01.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_project_01.R;
import com.example.my_project_01.databinding.FragmentBuylistBinding;
import com.example.my_project_01.pojo.Goods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyListFragment extends Fragment {

    private FragmentBuylistBinding binding;
    private ImageView buy_change;
    private boolean temp = false;
    private String userId;
    private DatabaseReference goodsRef;
    private List<Goods> goodsList = new ArrayList<>();

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBuylistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        buy_change = root.findViewById(R.id.buy_change);
        recyclerView = root.findViewById(R.id.recycleView);

        userId = getActivity().getIntent().getStringExtra("userId");
        System.out.println(userId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        goodsRef = FirebaseDatabase.getInstance().getReference("goods");
        goodsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Goods goods = ds.getValue(Goods.class);
                    goodsList.add(goods);
                }
                MyBuyListAdapter myBuyListAdapter = new MyBuyListAdapter(goodsList, userId);
                recyclerView.setAdapter(myBuyListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//
//        ConnUtils connUtils = new ConnUtils();
//        try {
//            connUtils.connFireBase("recyclerview","com.example.my_project_01.pojo.FetchData"
//                    ,"com.example.my_project_01.Adapter.MyBuyListAdapter",recyclerView);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (java.lang.InstantiationException e) {
//            e.printStackTrace();
//        }


        buy_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp == false) {
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    buy_change.setImageResource(R.drawable.list1);
                    temp = true;
                } else if (temp == true) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    buy_change.setImageResource(R.drawable.list2);
                    temp = false;
                }
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}