package com.example.my_project_01.ui.notifications.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.my_project_01.R;
import com.example.my_project_01.ui.notifications.QRActivity;

/**
 * 邀請好友
 */
public class ShareActivity extends AppCompatActivity {
    private ImageView back;
    private Button qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        qr = findViewById(R.id.qr);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ShareActivity.this, QRActivity.class);
                startActivity(intent);
            }
        });
    }
}