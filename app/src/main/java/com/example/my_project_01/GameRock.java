package com.example.my_project_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.my_project_01.pojo.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GameRock extends AppCompatActivity {

    private ImageView iv_com;
    private ImageButton ib_scissors, ib_stone, ib_paper;
    private TextView tv_result;
    private Button btn_start;
    private int count = 5;
    private int frameNumber;
    private DatabaseReference usersRef;
    private ArrayList<User> userList;
    private AnimationDrawable anim;
    private int win = 0, tie = 0, lose = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_rock);
        findId();
        String userId = getIntent().getStringExtra("userId");
        Log.e("0000029", "userId=" + userId);

        anim = (AnimationDrawable) iv_com.getBackground();
        btn_start.setOnClickListener(l4_start);
        userList = new ArrayList<>();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    User usersData = ds.getValue(User.class);
                    if (userId.equals(usersData.getUserId())) {
                        userList.add(usersData);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void findId() {
        iv_com = findViewById(R.id.iv_com);
        ib_scissors = findViewById(R.id.ib_scissors);
        ib_stone = findViewById(R.id.ib_stone);
        ib_paper = findViewById(R.id.ib_paper);
        tv_result = findViewById(R.id.tv_result);
        btn_start = findViewById(R.id.btn_start);
    }

    private void setOnClickListener() {
        ib_scissors.setOnClickListener(l1_scissors);
        ib_stone.setOnClickListener(l2_stone);
        ib_paper.setOnClickListener(l3_paper);
    }

    private void exit_listener() {
        ib_scissors.setOnClickListener(null);
        ib_stone.setOnClickListener(null);
        ib_paper.setOnClickListener(null);
    }

    public void com_set() {
        Drawable currentFrame, checkFrame;
        currentFrame = anim.getCurrent();
        for (int i = 0; i < anim.getNumberOfFrames(); i++) {
            checkFrame = anim.getFrame(i);
            if (checkFrame == currentFrame) {
                frameNumber = i;
                break;
            }
        }
    }

    View.OnClickListener l1_scissors = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            anim.stop();
            com_set();
            int icom = frameNumber;
            Log.e("0000021", "我出剪刀，電腦出拳=" + frameNumber);
            switch (icom) {
                case 0:
                    win++;
                    tv_result.setText("恭喜，你贏了!");
                    tv_result.setTextColor(Color.RED);
                    exit_listener();
                    btn_start.setOnClickListener(l4_start);
                    break;
                case 1:
                    tie++;
                    tv_result.setText("挖~平手!");
                    tv_result.setTextColor(Color.GREEN);
                    exit_listener();
                    btn_start.setOnClickListener(l4_start);
                    break;
                case 2:
                    lose++;
                    tv_result.setText("可惜，你輸了!");
                    tv_result.setTextColor(Color.BLUE);
                    exit_listener();
                    btn_start.setOnClickListener(l4_start);
                    break;
            }
        }
    };
    View.OnClickListener l2_stone = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            anim.stop();
            com_set();
            int icom = frameNumber;
            Log.e("0000022", "我出石頭，電腦出拳=" + frameNumber);
            switch (icom) {
                case 0:
                    lose++;
                    tv_result.setText("可惜，你輸了!");
                    tv_result.setTextColor(Color.BLUE);
                    exit_listener();
                    btn_start.setOnClickListener(l4_start);
                    break;
                case 1:
                    win++;
                    tv_result.setText("恭喜，你贏了!");
                    tv_result.setTextColor(Color.RED);
                    exit_listener();
                    btn_start.setOnClickListener(l4_start);
                    break;
                case 2:
                    tie++;
                    tv_result.setText("挖~平手!");
                    tv_result.setTextColor(Color.GREEN);
                    exit_listener();
                    btn_start.setOnClickListener(l4_start);
                    break;
            }
        }
    };
    View.OnClickListener l3_paper = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            anim.stop();
            com_set();
            int icom = frameNumber;
            Log.e("0000023", "我出布，電腦出拳=" + frameNumber);
            switch (icom) {
                case 0:
                    tie++;
                    tv_result.setText("挖~平手!");
                    tv_result.setTextColor(Color.GREEN);
                    exit_listener();
                    btn_start.setOnClickListener(l4_start);
                    break;
                case 1:
                    lose++;
                    tv_result.setText("可惜，你輸了!");
                    tv_result.setTextColor(Color.BLUE);
                    exit_listener();
                    btn_start.setOnClickListener(l4_start);
                    break;
                case 2:
                    win++;
                    tv_result.setText("恭喜，你贏了!");
                    tv_result.setTextColor(Color.RED);
                    exit_listener();
                    btn_start.setOnClickListener(l4_start);
                    break;
            }
        }
    };


    View.OnClickListener l4_start = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setOnClickListener();
            count--;
            switch (count) {
                case 4:
                    tv_result.setText("");
                    btn_start.setText("剩下2次機會");
                    anim.start();
                    btn_start.setOnClickListener(l5_start_Toast);
                    break;
                case 3:
                    tv_result.setText("");
                    btn_start.setText("剩下1次機會");
                    anim.start();
                    btn_start.setOnClickListener(l5_start_Toast);
                    break;
                case 2:
                    btn_start.setText("顯示結果");
                    anim.start();
                    btn_start.setOnClickListener(l5_start_Toast);
                    break;
                case 1:
                    tv_result.setTextSize(20);
                    tv_result.setTextColor(Color.RED);
                    tv_result.setText("贏了:" + win + "場,平手" + tie + "場,輸了:" + lose + "場" + "\n獲得了" + (win * 100) + "貨幣");
                    Log.e("000050", "win" + win + ",tie" + tie + ",lose" + lose);
                    btn_start.setText("結束");

                    String userId = getIntent().getStringExtra("userId");
                    int bal = (int) userList.get(0).getBalance();
                    Log.e("0000077", "" + bal);
                    int new_bal = bal + (win * 100);
                    Log.e("0000077", "" + new_bal);
                    HashMap<String, Object> new_user_balance = new HashMap<>();
                    new_user_balance.put("balance", new_bal);
                    usersRef.child(userId).updateChildren(new_user_balance);

                    Log.e("000056", "userId=" + userId);
                    break;
                case 0:
                    finish();
                    break;
            }
        }
    };

    View.OnClickListener l5_start_Toast = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(GameRock.this, "遊戲尚未結束，還有" + (count - 1) + "次機會", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横向

        } else {
            //竖向

        }
    }
}