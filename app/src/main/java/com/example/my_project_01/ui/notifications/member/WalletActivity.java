package com.example.my_project_01.ui.notifications.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.my_project_01.R;

/**
 * 小蝦錢包
 */
public class WalletActivity extends AppCompatActivity {

    private ImageView setting,back;
    private LinearLayout take_out_money,take_out_record;
    private String money = "NT$";
    /**
     * 111
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        TextView wallet  = findViewById(R.id.wallet);
        Bundle bundle = this.getIntent().getExtras();
        String walletStr = bundle.getString("balance");
        wallet.setText(money + walletStr);


        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(WalletActivity.this,SettingActivity.class);
                startActivity(intent);

            }
        });

        back = findViewById(R.id.wallet_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        take_out_money=findViewById(R.id.take_out_money);
        take_out_record=findViewById(R.id.take_out_record);
    }
}