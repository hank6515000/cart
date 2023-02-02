package com.example.my_project_01;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_project_01.pojo.Admin;
import com.example.my_project_01.pojo.User;
import com.example.my_project_01.ui.notifications.RegisterActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * 登入Activity
 */
public class LoginActivity extends AppCompatActivity {
    private ImageView back, img_password_eye;
    private Button login;
    private SignInButton gooleBtn;
    private TextView apply, tv_user, tv_admin, tv_title;
    private EditText account;
    private EditText password;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private ConnUtils connUtils;
    public static String user_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        img_password_eye = findViewById(R.id.img_password_eye);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        apply = findViewById(R.id.apply);
        login = findViewById(R.id.login);
        gooleBtn = findViewById(R.id.btn_google);

        connUtils = new ConnUtils();
        try {
            connUtils.connFireBase("users", "com.example.my_project_01.pojo.User", null, null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);


        gooleBtn.setOnClickListener(v -> {
            startActivityForResult(googleSignInClient.getSignInIntent(), 200);
        });

        /**
         * 登入按鈕
         */
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (account.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "請輸入帳號密碼", Toast.LENGTH_SHORT).show();
                } else {

                    if (account.getText().toString().trim().equals("admin@gmail.com")) {
                        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("admins");
                        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Admin admin = snapshot.child("admin").getValue(Admin.class);
                                if (admin.getEmail().equals(account.getText().toString().trim()) && admin.getPass().equals(password.getText().toString().trim())) {
                                    Toast.makeText(LoginActivity.this, "admin登入成功", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                } else {
                                    Toast.makeText(LoginActivity.this, "admin登入失敗", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        auth = FirebaseAuth.getInstance();
                        auth.signInWithEmailAndPassword(account.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.e("00011",account.getText().toString());
                                        if (task.isSuccessful()) {
                                            for (Object obj : connUtils.getDataList()) {
                                                User user = (User) obj;
                                                if (user.getUserId().equals(auth.getUid())) {
                                                    user_ID = user.getUserId();
                                                    Intent intent = new Intent();
                                                    intent.setClass(LoginActivity.this, MainActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("userId", user.getUserId());
                                                    bundle.putString("email", user.getEmail());
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                    Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(LoginActivity.this, "帳號或密碼錯誤，請重新登入", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }

                }
            }
        });

        /**
         * 註冊按鈕
         */
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 顯示密碼
         */
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent backHome = new Intent(Intent.ACTION_MAIN);
            backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            backHome.addCategory(Intent.CATEGORY_HOME);
            startActivity(backHome);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println(requestCode);
        if (requestCode == 200) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Bundle bundle = new Bundle();
                for (Object obj : connUtils.getDataList()) {
                    User user = (User) obj;
                    if (!user.getUserId().equals(account.getId())) {
                        User newUser = new User();
                        newUser.setEmail(account.getEmail());
                        String uid = account.getId();
                        newUser.setUserId(uid);
                        newUser.setBalance(50000);
                        newUser.setCost("0");
                        connUtils.getDatabaseReference().child(uid).setValue(newUser);

                        bundle.putString("userId", account.getId());
                        bundle.putString("email", account.getEmail());
                    } else {
                        bundle.putString("userId", user.getUserId());
                        bundle.putString("email", user.getEmail());
                    }
                }
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();

            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
}