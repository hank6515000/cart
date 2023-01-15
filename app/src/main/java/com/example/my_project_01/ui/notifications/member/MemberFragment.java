package com.example.my_project_01.ui.notifications.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.my_project_01.ConnUtils;
import com.example.my_project_01.LoginActivity;
import com.example.my_project_01.R;
import com.example.my_project_01.databinding.FragmentMemberBinding;
import com.example.my_project_01.pojo.User;
import com.example.my_project_01.ui.notifications.BuyCartActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

/**
 * 會員主Activity
 */
public class MemberFragment extends Fragment {

    Button btCancel, bt01, bt02;

    private FragmentMemberBinding binding;

    private String userId;

    private String name;
    private String gender;
    private String birthDay;
    private String phone ;
    private String email;

    ConnUtils connUtils = new ConnUtils();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

           }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMemberBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        userId =  getActivity().getIntent().getStringExtra("userId");
        return root;
    }
    private String[] func = {"小蝦會員", "小蝦錢包", "按讚好物", "瀏覽紀錄", "帳號設定", "邀請朋友", "登出"};
    private int[] c = {R.drawable.cup, R.drawable.wallet, R.drawable.heart, R.drawable.clock, R.drawable.people, R.drawable.shere, R.drawable.exit};
    ListView member_lv;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        member_lv = view.findViewById(R.id.member_list);
        MyMemberAdapter adapter = new MyMemberAdapter();
        member_lv.setAdapter(adapter);
        member_lv.setOnItemClickListener(listener);
        TextView nameTV = view.findViewById(R.id.mem_id);
        nameTV.setText(getActivity().getIntent().getStringExtra("email"));

       Button buyCart= view.findViewById(R.id.buy_cart);
       buyCart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Bundle bundle = new Bundle();
               bundle.putString("userId",userId);
               Intent intent = new Intent();
               intent.setClass(getContext(), BuyCartActivity.class);
               intent.putExtras(bundle);
               startActivity(intent);
           }
       });
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            connUtils.connFireBase("users","com.example.my_project_01.pojo.User",null,null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    class MyMemberAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return func.length;
        }

        @Override
        public Object getItem(int i) {
            return func[i];
        }

        @Override
        public long getItemId(int i) {
            return c[i];
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View r = view;
            if (r == null) {
                r = getLayoutInflater().inflate(R.layout.member_style, null);
                ImageView member_img = r.findViewById(R.id.member_img);
                TextView member_tv = r.findViewById(R.id.member_tv);
                member_img.setImageResource(c[i]);
                member_tv.setText(func[i]);
            }
            return r;
        }


    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
            Bundle bundle = new Bundle();
            Intent intent;
              switch (i) {
                case 0:
                    Toast.makeText(getActivity(), "小蝦會員", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getContext(),ReviseActivity.class);
                    getUserData(intent,bundle);
                    break;
                case 1:
                    Toast.makeText(getActivity(), "小蝦錢包", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getContext(), WalletActivity.class);
                    String balance =  getActivity().getIntent().getStringExtra("balance");
                    bundle.putString("balance",balance);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 2:
                    Toast.makeText(getActivity(), "按讚好物", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getContext(), NullListActivity.class);
                    startActivity(intent);
                    break;
                case 3:
                    Toast.makeText(getActivity(), "瀏覽紀錄", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getContext(), WalletActivity.class);
                    startActivity(intent);
                    break;
                case 4:

                    Toast.makeText(getActivity(), "帳號設定", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getContext(),ChangeMyMassageActivity.class);
                    getUserData(intent,bundle);
                    break;
                case 5:
                    Toast.makeText(getActivity(), "邀請朋友", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getContext(), ShareActivity.class);
                    startActivity(intent);
                    break;

                case 6:
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());//初始化BottomSheet
                    view = LayoutInflater.from(getContext()).inflate(R.layout.activity_bottom_sheet,null);//連結的介面
                    Button btCancel = view.findViewById(R.id.button_cancel);
                    Button bt01 = view.findViewById(R.id.button_01);
                    bottomSheetDialog.setContentView(view);//將介面載入至BottomSheet內
                    ViewGroup parent = (ViewGroup) view.getParent();//取得BottomSheet介面設定
                    parent.setBackgroundResource(android.R.color.transparent);//將背景設為透明，否則預設白底

                    bt01.setOnClickListener((v)->{
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        Toast.makeText(getContext(), "您已登出", Toast.LENGTH_SHORT).show();
                        Intent intentLogin = new Intent(getContext(), LoginActivity.class);
                        startActivity(intentLogin);
                    });
                    btCancel.setOnClickListener((v)->{
                        bottomSheetDialog.dismiss();
                    });
                    bottomSheetDialog.show();//顯示BottomSheet

                    break;
            }


        }
    };
public void getUserData(Intent intent ,Bundle bundle){
    for (Object obj : connUtils.getDataList()){
        User user = (User) obj;
        if (userId.equals(user.getUserId())){
            name = user.getName();
            gender = user.getGender();
            birthDay = user.getBirthDay();
            phone = user.getPhone();
            email = user.getEmail();
        }
    }

    bundle.putString("gender",gender);
    bundle.putString("birthDay",birthDay);
    bundle.putString("name",name);
    bundle.putString("userId",userId);
    bundle.putString("phone",phone);
    bundle.putString("email",email);
    intent.putExtras(bundle);
    startActivity(intent);
}
}
