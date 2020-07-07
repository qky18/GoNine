package com.example.gonine.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gonine.R;
import com.example.gonine.bean.MedicalDataItem;
import com.example.gonine.bean.Patient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import lecho.lib.hellocharts.view.LineChartView;

public class PatientViewActivity extends AppCompatActivity {
    // TODO: add lineChart View
    // for chart
    private LineChartView lcv_heartwave1;
    private LineChartView lcv_heartwave2;
    private LineChartView lcv_breath;
    private LineChartView lcv_blood;
    private Timer timer;

    // TODO: 通过Intent传入patientId来初始化patientRef
    // for firestore
    public static final String KEY_PATIENT_ID = "key_patient_id";
    private FirebaseFirestore mFirestore;
    private DocumentReference patientRef;
    // for firebase auth
    FirebaseAuth auth;

    // TODO: 使用patientRef来获得patient
    private Patient p;

    // bind widgets
    private ImageButton btn_add_info;
    private ImageView pic;
    private TextView name, gender, age;
    private TextView temperature, pressure, heart_rate, oxygen, breathe_rate;
    private TextView patient_situ, symptom, doctor_advice;

    //封装好的display
    private MedicalDataItem medicalDataItem ;

    // for logging
    private static final String TAG = "PatientViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        //设置此界面为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        initFirestore();
        initView();
    }

    private void initFirestore() {
        // for auth
        auth = FirebaseAuth.getInstance();

        // Get patient ID from extras
        String patientId = getIntent().getExtras().getString(KEY_PATIENT_ID);
        if (patientId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_PATIENT_ID);
        }

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Get reference to the patient
        patientRef = mFirestore.collection("patients").document(patientId);
        patientRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                p = documentSnapshot.toObject(Patient.class);
                initPatient();
                Log.i("patient object", p.getName());
            }
        });
    }

    //获取界面控件
    private void initView() {
        Log.i("PatientView", "init");
        pic = findViewById(R.id.patient_pic);
        name = findViewById(R.id.patient_name);
        gender = findViewById(R.id.patient_gender);
        age = findViewById(R.id.patient_age);
        temperature = findViewById(R.id.temperature);
        pressure = findViewById(R.id.blood_pressure);
        heart_rate = findViewById(R.id.heart_rate);
        oxygen = findViewById(R.id.blood_oxygen);
        breathe_rate = findViewById(R.id.breathe_rate);
        patient_situ = findViewById(R.id.patient_situation);
        symptom = findViewById(R.id.symptom);
        doctor_advice = findViewById(R.id.doctor_advice);
        btn_add_info = findViewById(R.id.btn_add_info);
        btn_add_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PatientViewActivity.this, PatientSpeechActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Toolbar mToolbar = findViewById(R.id.toolbar);
        //点击返回键
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PatientViewActivity.this.finish();
                    }
                });
    }


    private void initPatient() {
        if(p == null){
            Log.e("patientView", "Not initializing patient");
            return;
        }

        // set patient info
        //name.setText(p.getName());
        //age.setText(p.getAge());
        //gender.setText(p.getGender());
        // ...
        patient_situ.setText("入院时间：2020年1月21日\n过往病史：糖尿病\n过敏史：N/A");

        // init patient
        Vector em = new Vector() ;
        MedicalDataItem heartwave1 = new MedicalDataItem("heartwave1",em) ;
        Vector<MedicalDataItem> use = new Vector<MedicalDataItem>() ;
        MedicalDataItem breathe = new MedicalDataItem("breathe",em) ;

        use.add(heartwave1) ;
        use.add(breathe) ;
        p.setAdvices(null);
        p.setData(use);

        //TODO: add line chart binding & setting
        Vector data = new Vector() ;
        medicalDataItem = new MedicalDataItem("breathe", data) ;

        lcv_heartwave1 = (LineChartView) findViewById(R.id.heartwave1_chart);
        lcv_breath = (LineChartView) findViewById(R.id.breath_chart);
        p.initChartAll(lcv_heartwave1,lcv_heartwave2,lcv_breath,lcv_blood);

        timer = new Timer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(timer == null){
            Log.i("PatientView", "timer not ready");
            return;
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //实时添加新的点
                p.displayAll(lcv_heartwave1,lcv_heartwave2,lcv_breath,lcv_blood) ;
            }
        }, 1000, 1000);

    }

}
