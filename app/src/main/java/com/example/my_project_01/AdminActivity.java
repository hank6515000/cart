package com.example.my_project_01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class AdminActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView goods_title, show_goods;
    private Spinner goods_spinner;
    private EditText goods_price, goods_introduce, goods_detail;
    private ImageView goods_image;
    private Button btn_choose_image, btn_upload;
    private ProgressBar progress_bar;

    private Uri mImageUri;

    private StorageReference goods_Storage_Ref;
    private DatabaseReference goods_Ref;

    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        findID();

        goods_Storage_Ref = FirebaseStorage.getInstance().getReference();
        goods_Ref = FirebaseDatabase.getInstance().getReference("goods");

        btn_choose_image.setOnClickListener(choose_image);
        btn_upload.setOnClickListener(upload);
        show_goods.setOnClickListener(goods_show);
    }

    private void findID() {
        goods_title = findViewById(R.id.goods_title);
        show_goods = findViewById(R.id.show_goods);
        goods_spinner = findViewById(R.id.goods_spinner);
        goods_price = findViewById(R.id.goods_price);
        goods_introduce = findViewById(R.id.goods_introduce);
        goods_detail = findViewById(R.id.goods_detail);
        goods_image = findViewById(R.id.goods_image);
        btn_choose_image = findViewById(R.id.btn_choose_image);
        btn_upload = findViewById(R.id.btn_upload);
        progress_bar = findViewById(R.id.progress_bar);
    }

    View.OnClickListener choose_image = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openFileChooser();
        }
    };


    View.OnClickListener upload = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(AdminActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();
            }
        }
    };

    View.OnClickListener goods_show = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(AdminActivity.this, AdminGoodsActivity.class));
        }
    };

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileRef = goods_Storage_Ref.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            uploadTask = fileRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progress_bar.setProgress(0);
                                }
                            }, 500);
                            if (taskSnapshot.getMetadata() != null) {
                                Task<Uri> re = taskSnapshot.getStorage().getDownloadUrl();
                                re.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String gd_title = goods_title.getText().toString().trim();
                                        String gd_cate = goods_spinner.getSelectedItem().toString().trim();
                                        String gd_Id = goods_Ref.push().getKey();
                                        String gd_price = goods_price.getText().toString().trim();
                                        String gd_itd = goods_introduce.getText().toString().trim();
                                        String gd_det = goods_detail.getText().toString().trim();
                                        String img_url = uri.toString().trim();
                                        Log.e("00050", img_url);
                                        if (TextUtils.isEmpty(gd_title)) {
                                            Toast.makeText(AdminActivity.this, "未輸入title", Toast.LENGTH_SHORT).show();
                                        } else if (TextUtils.isEmpty(gd_cate)) {
                                            Toast.makeText(AdminActivity.this, "未輸入category", Toast.LENGTH_SHORT).show();
                                        } else if (TextUtils.isEmpty(gd_price)) {
                                            Toast.makeText(AdminActivity.this, "未輸入price", Toast.LENGTH_SHORT).show();
                                        } else if (TextUtils.isEmpty(gd_itd)) {
                                            Toast.makeText(AdminActivity.this, "未輸入introduce", Toast.LENGTH_SHORT).show();
                                        } else if (TextUtils.isEmpty(gd_det)) {
                                            Toast.makeText(AdminActivity.this, "未輸入detail", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(AdminActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                                            //Goods goods = new Goods(et_file_name.getText().toString().trim(),img_url );

                                            HashMap<String, Object> goods_map = new HashMap<>();
                                            goods_map.put("goodsTitle", gd_title);
                                            goods_map.put("goodsCategory", gd_cate);
                                            goods_map.put("goodsID", gd_Id);
                                            goods_map.put("goodsPrice", gd_price);
                                            goods_map.put("goodsIntroduce", gd_itd);
                                            goods_map.put("goodsDetail", gd_det);
                                            goods_map.put("goodsImageUrl", img_url);

                                            goods_Ref.child(gd_Id).setValue(goods_map);

                                            goods_title.setText("");
                                            goods_price.setText("");
                                            goods_introduce.setText("");
                                            goods_detail.setText("");
                                            Glide.with(AdminActivity.this).load("").into(goods_image);
                                        }
                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progress_bar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Glide.with(this).load(mImageUri).into(goods_image);
        }
    }
}