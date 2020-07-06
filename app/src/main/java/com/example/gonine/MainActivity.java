package com.example.gonine;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gonine.activity.DoctorActivity;
import com.example.gonine.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    //返回按钮
    private TextView tv_back;
    //登录按钮
    private Button btn_login;

    // add for firebase
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        initFirestore();
        init();
    }

    private void init() {
        //从main_title_bar.xml 页面布局中获取对应的UI控件（已删除）
        //布局根元素（已删除）
        //从activity_main.xml 页面中获取对应的UI控件
        tv_back=findViewById(R.id.tv_back);
        btn_login=findViewById(R.id.btn_login);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent data=new Intent();
            //datad.putExtra( ); name , value ;
            data.putExtra("isLogin",true);
            data.putExtra("userName", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            Log.i("display name", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            //RESULT_OK为Activity系统常量，状态码为-1
            // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
            setResult(RESULT_OK, data);
            startActivity(new Intent(MainActivity.this, DoctorActivity.class));
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }
}
