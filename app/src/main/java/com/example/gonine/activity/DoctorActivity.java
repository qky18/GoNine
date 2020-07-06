package com.example.gonine.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gonine.R;
import com.example.gonine.adapter.DoctorAdapter;
import com.example.gonine.bean.Patient;
import com.example.gonine.bean.Severity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AppCompatActivity {
    // toolbar
    private Toolbar mToolbar;
    private SearchView mSearchView;

    // recycler view for patients
    private RecyclerView mPatientsRecycler;
    private DoctorAdapter mAdapter;

    // buttons
    private ImageButton addPatient;

    // for firebase auth
    FirebaseAuth auth;
    private FirebaseFirestore mFirestore;

    // for logging
    private static final String TAG = "DoctorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        //设置此界面为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        auth = FirebaseAuth.getInstance();

        init();
        initRecyclerView();
    }


    //获取界面控件
    private void init() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar); //使活动支持ToolBar
        mSearchView = findViewById(R.id.search_view);
        mPatientsRecycler = findViewById(R.id.recycler_patients);
        /*------------------ SearchView有三种默认展开搜索框的设置方式，区别如下： ------------------*/
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
        mSearchView.setIconified(false);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.setIconifiedByDefault(false);
        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.onActionViewExpanded();

        //设置字体颜色
        TextView textView = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);

        //点击返回键
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DoctorActivity.this.finish();
                    }
                });
        //搜索框展开时后面叉叉按钮的点击事件
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(getApplicationContext(), "Close", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //搜索图标按钮(打开搜索框的按钮)的点击事件
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Open", Toast.LENGTH_SHORT).show();
            }
        });
        //搜索框文字变化监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //TODO: submit query text & go to next page
                Log.e("Debug", "TextSubmit : " + s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //TODO: show search hint
                Log.e("Debug", "TextChange --> " + s);
                return false;
            }
        });

        mFirestore = FirebaseFirestore.getInstance();
        addPatient = findViewById(R.id.btn_add_patient);
        addPatient.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onAddPatient();
            }
        });
    }

    private void initRecyclerView() {

        // debug
        final List<Patient> patientList = new ArrayList<>();
        mFirestore.collection("patients").get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        for(QueryDocumentSnapshot snap: documentSnapshots){
                            patientList.add(snap.toObject(Patient.class));
                        }
                    }
                }
        );

//        for (int i = 0; i < 12; i++){
//            int resourceId = R.mipmap.doctor;
//            // id, name, gender, severity, photo, age
//            patientList.add(new Patient(i, "003 Yefren Lee", "Female", "serious", resourceId, 40));
//        }

        mAdapter = new DoctorAdapter(patientList);
        mPatientsRecycler.setAdapter(mAdapter);
    }

    private void onAddPatient(){
        final CollectionReference patients = mFirestore.collection("patients");
        // TODO: should be transaction
        patients.orderBy("id", Query.Direction.DESCENDING).limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if(documentSnapshots.size() == 0){
                            Patient p = new Patient(1, "Alice", "female", Severity.MILD, R.mipmap.doctor, 23);
                            Log.i(TAG, "====== add " + p.getName() + " ======" );
                            patients.document(Integer.toString(p.getID())).set(p);
                        }
                        else for(QueryDocumentSnapshot snap: documentSnapshots){
                            int m = Integer.valueOf(snap.getData().get("id").toString());
                            Patient p = new Patient(m+1, "Alice", "female", Severity.SEVERE, R.mipmap.doctor, 23);
                            Log.i(TAG, "====== add " + p.getName() + " ======" );
                            patients.document(Integer.toString(p.getID())).set(p);
                        }
                    }
                });

    }
}
