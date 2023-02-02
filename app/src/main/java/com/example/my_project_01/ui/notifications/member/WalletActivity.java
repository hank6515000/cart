package com.example.my_project_01.ui.notifications.member;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_project_01.LoginActivity;
import com.example.my_project_01.R;
import com.example.my_project_01.pojo.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 小蝦錢包
 */
public class WalletActivity extends AppCompatActivity {

    private ImageView setting, back;
    private LinearLayout add_money, take_out_record;
    private String money = "NT$";
    private DatabaseReference userRef;
    private List<User> userList = new ArrayList<>();
    private long balance = 0;
    private ProgressDialog pd;

    public static List<String> recordList=new ArrayList<>();

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    /**
     * 111
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();

//        Gson gson = new Gson();
//        String jsonText = sp.getString("record",null);
//        recordList = Arrays.asList(gson.fromJson(jsonText, String[].class));

        TextView wallet = findViewById(R.id.wallet);
//        String walletStr = getIntent().getStringExtra("balance");
        pd = new ProgressDialog(this);

        userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    userList.add(user);
                }
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getUserId().equals(LoginActivity.user_ID)) {
                        balance = userList.get(i).getBalance();
                        Log.e("00070", "balance=" + balance);
                    }
                }
                wallet.setText(money + balance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(WalletActivity.this, SettingActivity.class);
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

        add_money = findViewById(R.id.add_money);
        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = new EditText(WalletActivity.this);
                et.setHint("請輸入儲值金額");
                AlertDialog.Builder builder = new AlertDialog.Builder(WalletActivity.this);
                builder.setTitle("儲值");
                builder.setView(et);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pd.setTitle("Add Accounting...");
                        pd.setMessage("Please wait checking the credentials");
                        pd.setCanceledOnTouchOutside(false);
                        pd.show();

                        int get_balance = Integer.parseInt(et.getText().toString().trim());
                        long last_balance = balance + get_balance;
                        HashMap<String, Object> new_user_balance = new HashMap<>();
                        new_user_balance.put("balance", last_balance);
                        userRef.child(LoginActivity.user_ID).updateChildren(new_user_balance);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());

                        String re_time = simpleDateFormat.format(date);
                        String re = String.valueOf(get_balance);

                        recordList.add(re_time + "    +" + re);

                        Gson gson = new Gson();
                        String jsonText = gson.toJson(recordList);
                        editor.putString("record", jsonText);
                        editor.apply();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                pd.dismiss();
                                Toast.makeText(WalletActivity.this, "儲值成功!!", Toast.LENGTH_SHORT).show();
                            }
                        }, 1000);
                    }
                });
                builder.setPositiveButton("Cancel", null);
                builder.show();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WalletActivity.this, android.R.layout.simple_list_item_1, recordList);
        take_out_record = findViewById(R.id.take_out_record);
        take_out_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WalletActivity.this);
                builder.setTitle("交易紀錄");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(WalletActivity.this, "0011", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}