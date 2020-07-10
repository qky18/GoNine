package com.example.gonine.activity;

import android.content.Intent;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DoctorActivity extends AppCompatActivity implements
        DoctorAdapter.OnPatientSelectedListener {
    // toolbar
    private Toolbar mToolbar;
    private SearchView mSearchView;

    // recycler view for patients
    private RecyclerView mPatientsRecycler;
    private DoctorAdapter mAdapter = null;

    // buttons
    private ImageButton addPatient;

    // for firebase auth
    FirebaseAuth auth;
    private FirebaseFirestore mFirestore;
    private Query mQuery = null;
    private String user_name;
    private int m;

    // for logging
    private static final String TAG = "DoctorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        //设置此界面为横屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m=0;
        initFirestore();
        initView();
        initRecyclerView();
    }

    @Override
    public void onPatientSelected(DocumentSnapshot patient) {
        // Go to the details page for the selected patient
        Intent intent = new Intent(DoctorActivity.this, PatientViewActivity.class);
        intent.putExtra(PatientViewActivity.KEY_PATIENT_ID, patient.getId());

        startActivity(intent);
    }

    private void initFirestore() {
        // init firestore
        auth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        // Get patients
        mQuery = mFirestore.collection("patients")
                .orderBy("id", Query.Direction.DESCENDING);
    }

    //获取界面控件
    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar); //使活动支持ToolBar
        mSearchView = findViewById(R.id.search_view);
        mPatientsRecycler = findViewById(R.id.recycler_patients);
        /*------------------ SearchView有三种默认展开搜索框的设置方式，区别如下： ------------------*/
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
        mSearchView.setIconified(true);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.setIconifiedByDefault(false);
        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        //mSearchView.onActionViewExpanded();

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
                // TODO: submit query text & go to next page
                Log.e("Debug", "TextSubmit : " + s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // TODO: show search hint
                Log.e("Debug", "TextChange --> " + s);
                return false;
            }
        });

        addPatient = findViewById(R.id.btn_add_patient);
        addPatient.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onAddPatient();
            }
        });
    }

    private void initRecyclerView() {

        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new DoctorAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mPatientsRecycler.setVisibility(View.GONE);
                } else {
                    mPatientsRecycler.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mPatientsRecycler.setAdapter(mAdapter);
    }

    private void onAddPatient(){
        final CollectionReference patients = mFirestore.collection("patients");
        // TODO: should be transaction
        patients.orderBy("id", Query.Direction.DESCENDING).limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        String name="Alice";
                        String gender="female";
                        Severity sev=Severity.MILD;
                        int photoID;
                        int age=23;
                        if(gender.equals("female")){
                            photoID=R.drawable.patient1;
                        }
                        else{
                            photoID=R.drawable.patient2;
                        }
                        if(documentSnapshots.size() == 0){
                            Patient p = new Patient(1, name, gender, sev, photoID, age);
                            Log.i(TAG, "====== add " + p.getName() + " ======" );
                            patients.document(Integer.toString(p.getID())).set(p);
                        }
                        else for(QueryDocumentSnapshot snap: documentSnapshots){
                            int m = Integer.valueOf(snap.getData().get("id").toString());
                            Patient p = new Patient(m+1, name, gender, sev, photoID, age);
                            Log.i(TAG, "====== add " + p.getName() + " ======" );
                            patients.document(Integer.toString(p.getID())).set(p);
                        }
                    }
                });

    }


    @Override
    public void onStart() {
        super.onStart();

        // TODO:Start sign in if necessary

        // TODO: Apply filters

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

}
