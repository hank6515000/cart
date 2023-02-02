package com.example.my_project_01.ui.notifications;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_project_01.ConnUtils;
import com.example.my_project_01.LoginActivity;
import com.example.my_project_01.R;
import com.example.my_project_01.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;
import com.google.firebase.ktx.Firebase;

/**
 * 邀請朋友Activity
 */
public class RegisterActivity extends AppCompatActivity {
    private ImageView back, img_password_eye, img_check_eye, line;
    private TextView login;
    private EditText password, checkPwd;
    private EditText account;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        account = findViewById(R.id.account_email);
        password = findViewById(R.id.password);
        checkPwd = findViewById(R.id.et_check_pwd);
        findViewById(R.id.tv_result);

        auth = FirebaseAuth.getInstance();


        ConnUtils connUtils = new ConnUtils();
        try {
            connUtils.connFireBase("users", "com.example.my_project_01.pojo.User", null, null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        /**
         * 註冊按鈕
         */
        findViewById(R.id.btn_register)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (Object obj : connUtils.getDataList()) {
                            User user = (User) obj;
                            if (user.getEmail().equals(account.getText().toString())) {
                                Toast.makeText(RegisterActivity.this, "用戶已存在，請重新輸入", Toast.LENGTH_SHORT).show();
                            } else {
                                String regPassword = "(^(?![0-9]+$)(?![a-zA-Z]+$)[0-9a-zA-Z]{8,16}$)";
                                if (!password.getText().toString().matches(regPassword)) {
                                    Toast.makeText(RegisterActivity.this, "密碼必須是8-16位數字且1位字母", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (!password.getText().toString().equals(checkPwd.getText().toString())) {
                                        Toast.makeText(RegisterActivity.this, "密碼不一致，請重新輸入", Toast.LENGTH_SHORT).show();
                                    } else {
                                        auth.createUserWithEmailAndPassword(account.getText().toString(), password.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                                        User newUser = new User();
                                                        newUser.setEmail(auth.getCurrentUser().getEmail());
                                                        String uid = auth.getUid();
                                                        newUser.setUserId(uid);
                                                        newUser.setBalance(50000);
                                                        newUser.setCost("0");
                                                        connUtils.getDatabaseReference().child(uid).setValue(newUser);
                                                        Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent();
                                                        intent.setClass(RegisterActivity.this, LoginActivity.class);
                                                        startActivity(intent);

                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    }
                });


        /**
         * 返回按鈕
         */
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /**
         * 顯示密碼
         */
        img_password_eye = findViewById(R.id.img_password_eye);

        img_password_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransformationMethod method = password.getTransformationMethod();
                if (method == HideReturnsTransformationMethod.getInstance()) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_password_eye.setBackgroundResource(R.drawable.eye);
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    img_password_eye.setBackgroundResource(R.drawable.eye_open);
                }


            }

        });

        /**
         * 顯示確認密碼
         */
        img_check_eye = findViewById(R.id.img_check_eye);

        img_check_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransformationMethod method = checkPwd.getTransformationMethod();
                if (method == HideReturnsTransformationMethod.getInstance()) {
                    checkPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_check_eye.setBackgroundResource(R.drawable.eye);
                } else {
                    checkPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    img_check_eye.setBackgroundResource(R.drawable.eye_open);
                }
            }
        });


    }
}