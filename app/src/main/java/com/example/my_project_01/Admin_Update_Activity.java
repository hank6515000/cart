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
import com.example.my_project_01.pojo.Goods;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Admin_Update_Activity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;
    private StorageTask uploadTask;
    private StorageReference goods_Storage_Ref;
    private String goods_id, imgurl, title, price, itd, det, gd_cate;
    private DatabaseReference goodsRef;
    private List<Goods> goodsList = new ArrayList<>();

    private EditText re_goods_title, re_goods_price, re_goods_introduce, re_goods_detail;
    private Spinner re_goods_spinner;
    private TextView re_goods_id;
    private ImageView re_goods_image;
    private Button re_btn_choose_image, re_btn_update;
    private ProgressBar re_progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update);
        findId();
        goods_Storage_Ref = FirebaseStorage.getInstance().getReference();
        goods_id = getIntent().getStringExtra("goods_id");
        Log.e("00095", goods_id);

        goodsRef = FirebaseDatabase.getInstance().getReference("goods");
        Log.e("00094", "" + goodsRef);
        goodsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Goods goods = ds.getValue(Goods.class);
                    goodsList.add(goods);
                    Log.e("00093", "" + goodsList);
                }

                for (int i = 0; i < goodsList.size(); i++) {
                    Log.e("00092", "" + goodsList.size());
                    if (goodsList.get(i).getGoodsID().equals(goods_id)) {

                        title = goodsList.get(i).getGoodsTitle();
                        price = goodsList.get(i).getGoodsPrice();
                        itd = goodsList.get(i).getGoodsIntroduce();
                        det = goodsList.get(i).getGoodsDetail();
                        imgurl = goodsList.get(i).getGoodsImageUrl();

                        re_goods_title.setText(title);
                        re_goods_price.setText(price);
                        re_goods_introduce.setText(itd);
                        re_goods_detail.setText(det);
                        re_goods_id.setText(goods_id);

                        gd_cate = goodsList.get(i).getGoodsCategory();
                        switch (gd_cate) {
                            case "美食":
                                re_goods_spinner.setSelection(0);
                                break;
                            case "寵物用品":
                                re_goods_spinner.setSelection(1);
                                break;
                            case "運動用品":
                                re_goods_spinner.setSelection(2);
                                break;
                            case "衣物":
                                re_goods_spinner.setSelection(3);
                                break;
                            case "美妝保養":
                                re_goods_spinner.setSelection(4);
                                break;
                            case "嬰兒用品":
                                re_goods_spinner.setSelection(5);
                                break;
                            case "3C用品":
                                re_goods_spinner.setSelection(6);
                                break;
                            case "汽機車":
                                re_goods_spinner.setSelection(7);
                                break;
                            case "電腦與周邊":
                                re_goods_spinner.setSelection(8);
                                break;
                            case "文具與美術":
                                re_goods_spinner.setSelection(9);
                                break;
                            case "其他":
                                re_goods_spinner.setSelection(10);
                                break;
                        }
                        Glide.with(Admin_Update_Activity.this).load(goodsList.get(i).getGoodsImageUrl()).into(re_goods_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        re_btn_choose_image.setOnClickListener(choose_image);
        re_btn_update.setOnClickListener(update);
    }

    private void findId() {
        re_goods_title = findViewById(R.id.re_goods_title);
        re_goods_price = findViewById(R.id.re_goods_price);
        re_goods_introduce = findViewById(R.id.re_goods_introduce);
        re_goods_detail = findViewById(R.id.re_goods_detail);
        re_goods_spinner = findViewById(R.id.re_goods_spinner);
        re_goods_id = findViewById(R.id.re_goods_id);
        re_goods_image = findViewById(R.id.re_goods_image);
        re_btn_choose_image = findViewById(R.id.re_btn_choose_image);
        re_btn_update = findViewById(R.id.re_btn_update);
        re_progress_bar = findViewById(R.id.re_progress_bar);
    }

    View.OnClickListener choose_image = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openFileChooser();
        }
    };
    View.OnClickListener update = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(Admin_Update_Activity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else if (imgurl != null) {
                //未重新選圖片
                Log.e("00052", "" + imgurl);
                String gd_title = re_goods_title.getText().toString().trim();
                String gd_cate = re_goods_spinner.getSelectedItem().toString().trim();
                String gd_price = re_goods_price.getText().toString().trim();
                String gd_itd = re_goods_introduce.getText().toString().trim();
                String gd_det = re_goods_detail.getText().toString().trim();
                HashMap<String, Object> goods_map = new HashMap<>();
                goods_map.put("goodsTitle", gd_title);
                goods_map.put("goodsCategory", gd_cate);
                goods_map.put("goodsID", goods_id);
                goods_map.put("goodsPrice", gd_price);
                goods_map.put("goodsIntroduce", gd_itd);
                goods_map.put("goodsDetail", gd_det);
                goods_map.put("goodsImageUrl", imgurl);
                goodsRef.child(goods_id).setValue(goods_map);

                finish();
                Toast.makeText(Admin_Update_Activity.this, "更新成功!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Admin_Update_Activity.this, AdminGoodsActivity.class));
            } else {
                //重新選圖
                updateFile();
            }
        }
    };

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void updateFile() {
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
                                    re_progress_bar.setProgress(0);
                                }
                            }, 500);
                            if (taskSnapshot.getMetadata() != null) {
                                Task<Uri> re = taskSnapshot.getStorage().getDownloadUrl();
                                re.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String gd_title = re_goods_title.getText().toString().trim();
                                        String gd_cate = re_goods_spinner.getSelectedItem().toString().trim();
                                        String gd_price = re_goods_price.getText().toString().trim();
                                        String gd_itd = re_goods_introduce.getText().toString().trim();
                                        String gd_det = re_goods_detail.getText().toString().trim();
                                        String img_url = uri.toString().trim();
                                        Log.e("00050", img_url);
                                        if (TextUtils.isEmpty(gd_title)) {
                                            Toast.makeText(Admin_Update_Activity.this, "未輸入title", Toast.LENGTH_SHORT).show();
                                        } else if (TextUtils.isEmpty(gd_cate)) {
                                            Toast.makeText(Admin_Update_Activity.this, "未輸入category", Toast.LENGTH_SHORT).show();
                                        } else if (TextUtils.isEmpty(gd_price)) {
                                            Toast.makeText(Admin_Update_Activity.this, "未輸入price", Toast.LENGTH_SHORT).show();
                                        } else if (TextUtils.isEmpty(gd_itd)) {
                                            Toast.makeText(Admin_Update_Activity.this, "未輸入introduce", Toast.LENGTH_SHORT).show();
                                        } else if (TextUtils.isEmpty(gd_det)) {
                                            Toast.makeText(Admin_Update_Activity.this, "未輸入detail", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Admin_Update_Activity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                                            //Goods goods = new Goods(et_file_name.getText().toString().trim(),img_url );

                                            HashMap<String, Object> goods_map = new HashMap<>();
                                            goods_map.put("goodsTitle", gd_title);
                                            goods_map.put("goodsCategory", gd_cate);
                                            goods_map.put("goodsID", goods_id);
                                            goods_map.put("goodsPrice", gd_price);
                                            goods_map.put("goodsIntroduce", gd_itd);
                                            goods_map.put("goodsDetail", gd_det);
                                            goods_map.put("goodsImageUrl", img_url);

                                            goodsRef.child(goods_id).setValue(goods_map);

                                            finish();
                                            Toast.makeText(Admin_Update_Activity.this, "更新成功!!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Admin_Update_Activity.this, AdminGoodsActivity.class));
                                        }
                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Admin_Update_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            re_progress_bar.setProgress((int) progress);
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
            Glide.with(this).load(mImageUri).into(re_goods_image);
        }
    }
}