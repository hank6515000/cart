package com.example.my_project_01.ui.notifications.member;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.my_project_01.R;


public class SettingActivity extends AppCompatActivity {

    private ListView setting_list;
    private ImageView setting_back;

    private String[] func = {"銀行帳號/信用卡", "變更錢包密碼"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setting_back = findViewById(R.id.setting_back);
        setting_list = findViewById(R.id.setting_list);
        setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


//        setting_list = findViewById(R.id.setting_list);
//        MySettingActivity adapter = new MySettingActivity();
//        setting_list.setAdapter(adapter);

    }

//    class MySettingActivity extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return func.length;
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return func[i];
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            View r = view;
//            if (r == null) {
//                r = getLayoutInflater().inflate(R.layout.revise_style, null);
//                TextView tv_steeing = r.findViewById(R.id.tv_revise);
//                tv_steeing.setText(func[i]);
//            }
//            return r;
//        }
//
//    }
}
