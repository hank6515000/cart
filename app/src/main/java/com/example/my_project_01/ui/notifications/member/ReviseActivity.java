package com.example.my_project_01.ui.notifications.member;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_project_01.R;
import com.example.my_project_01.databinding.ActivityMainBinding;

/**
 * 小蝦會員
 */
public class ReviseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise);

        TextView name = findViewById(R.id.tv_name);
        String nameStr = getIntent().getStringExtra("name");
        name.setText(nameStr);
        TextView email = findViewById(R.id.tv_email);
        String emailStr = getIntent().getStringExtra("email");
        email.setText(emailStr);
        TextView phone = findViewById(R.id.tv_phone);
        String phoneStr = getIntent().getStringExtra("phone");
        phone.setText(phoneStr);
        TextView gender = findViewById(R.id.tv_gender);
        String genderStr = getIntent().getStringExtra("gender");
        gender.setText(genderStr);
        TextView birthDay = findViewById(R.id.tv_birthday);
        String birthDayStr = getIntent().getStringExtra("birthDay");
        birthDay.setText(birthDayStr);


        findViewById(R.id.revise_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}

