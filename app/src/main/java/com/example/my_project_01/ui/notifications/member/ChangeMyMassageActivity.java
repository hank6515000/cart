package com.example.my_project_01.ui.notifications.member;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_project_01.LoginActivity;
import com.example.my_project_01.R;
import com.example.my_project_01.ui.notifications.member.WalletActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChangeMyMassageActivity extends AppCompatActivity {
    private ImageView ch_back;
    private TextView dateTV;
    private TextView nameED;
    private RadioGroup genderRG;
    private String gender;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_my_massage);
        dateTV = findViewById(R.id.tv_date);
        nameED = findViewById(R.id.ed_name);

        userId = getIntent().getStringExtra("userId");

        /**
         * 姓名設置
         */
        String name = getIntent().getStringExtra("name");
        nameED.setText(name);

        EditText phoneED = findViewById(R.id.ed_phone);
        String phone = getIntent().getStringExtra("phone");
        phoneED.setText(phone);
        /**
         * 性別設置
         */
        genderRG = findViewById(R.id.rg_gender);
        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked) {
                switch (checked) {
                    case R.id.male:
                        gender = "男";
                        break;

                    case R.id.female:
                        gender = "女";
                        break;
                }
            }
        });


        /**
         * 生日設置
         */
        String birthDay = getIntent().getStringExtra("birthDay");
        dateTV.setText(birthDay);

        findViewById(R.id.btn_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String desc = String.format("%d/%d/%d", year, month + 1, day);
                                dateTV.setText(desc);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }

        });

        findViewById(R.id.ch_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("users").child(LoginActivity.user_ID);
                Map<String, Object> updateProfile = new HashMap<>();
                String nameStr = nameED.getText().toString();
                String phoneStr = phoneED.getText().toString();
                String birthbayStr = dateTV.getText().toString();
                if (!(nameStr.isEmpty() && phoneStr.isEmpty() && birthbayStr.isEmpty() && gender.isEmpty())) {
                    String regName = "((^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5}))";
                    if (!nameStr.matches(regName)) {
                        Toast.makeText(ChangeMyMassageActivity.this, "姓名必須是3-5位中文或6-16位英文", Toast.LENGTH_SHORT).show();
                    } else {
                        String regPhone = "(^09[0-9]{8}$)";
                        if (!phoneStr.matches(regPhone)) {
                            Toast.makeText(ChangeMyMassageActivity.this, "手機必須是09開頭加8位數字", Toast.LENGTH_SHORT).show();
                        } else {
                            updateProfile.put("name", nameStr);
                            updateProfile.put("phone", phoneStr);
                            updateProfile.put("gender", gender);
                            updateProfile.put("birthDay", birthbayStr);
                            reference.updateChildren(updateProfile);
                            Toast.makeText(ChangeMyMassageActivity.this, "儲存成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(ChangeMyMassageActivity.this, "不可有空白，請重新輸入", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /**
         * 返回按鈕
         */
        ch_back = findViewById(R.id.ch_back);

        ch_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }
}








