package com.example.my_project_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GameRock extends AppCompatActivity {

    ImageView iv_com;
    ImageButton ib_scissors, ib_stone, ib_paper;
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_rock);
        findId();
        setOnClickListener();
    }

    private void findId() {
        iv_com = findViewById(R.id.iv_com);
        ib_scissors = findViewById(R.id.ib_scissors);
        ib_stone = findViewById(R.id.ib_stone);
        ib_paper = findViewById(R.id.ib_paper);
        tv_result = findViewById(R.id.tv_result);
    }

    private void setOnClickListener() {
        ib_scissors.setOnClickListener(l1);
        ib_stone.setOnClickListener(l2);
        ib_paper.setOnClickListener(l3);
    }

    View.OnClickListener l1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int icom=(int)(Math.random()*3+1);
            switch (icom){
                case 1:
                    iv_com.setImageResource(R.drawable.scissors);
                    tv_result.setText("挖~平手!");
                    tv_result.setTextColor(Color.GREEN);
                    break;
                case 2:
                    iv_com.setImageResource(R.drawable.stone);
                    tv_result.setText("可惜，你輸了!");
                    tv_result.setTextColor(Color.BLUE);
                    break;
                case 3:
                    iv_com.setImageResource(R.drawable.paper);
                    tv_result.setText("恭喜，你贏了!");
                    tv_result.setTextColor(Color.RED);
                    break;
            }
        }
    };
    View.OnClickListener l2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int icom=(int)(Math.random()*3+1);
            switch (icom){
                case 1:
                    iv_com.setImageResource(R.drawable.scissors);
                    tv_result.setText("恭喜，你贏了!");
                    tv_result.setTextColor(Color.RED);
                    break;
                case 2:
                    iv_com.setImageResource(R.drawable.stone);
                    tv_result.setText("挖~平手!");
                    tv_result.setTextColor(Color.GREEN);
                    break;
                case 3:
                    iv_com.setImageResource(R.drawable.paper);
                    tv_result.setText("可惜，你輸了!");
                    tv_result.setTextColor(Color.BLUE);
                    break;
            }
        }
    };
    View.OnClickListener l3 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int icom=(int)(Math.random()*3+1);
            switch (icom){
                case 1:
                    iv_com.setImageResource(R.drawable.scissors);
                    tv_result.setText("可惜，你輸了!");
                    tv_result.setTextColor(Color.BLUE);
                    break;
                case 2:
                    iv_com.setImageResource(R.drawable.stone);
                    tv_result.setText("恭喜，你贏了!");
                    tv_result.setTextColor(Color.RED);
                    break;
                case 3:
                    iv_com.setImageResource(R.drawable.paper);
                    tv_result.setText("挖~平手!");
                    tv_result.setTextColor(Color.GREEN);
                    break;
            }
        }
    };
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) {
            //横向

        } else {
            //竖向

        }
    }
}