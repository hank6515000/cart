package com.example.my_project_01.ui.notifications.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.my_project_01.MainActivity;
import com.example.my_project_01.R;

/**
 * 按讚列表
 */
public class NullListActivity extends AppCompatActivity {

    private ImageView null_back;
    private Button null_goshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_null_list);

        null_back = findViewById(R.id.null_back);
        null_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        null_goshop = findViewById(R.id.null_goshop);
        null_goshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(NullListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}