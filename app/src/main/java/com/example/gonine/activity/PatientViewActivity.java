package com.example.gonine.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gonine.R;
import com.example.gonine.bean.DigitalItem;
import com.example.gonine.bean.MedicalDataItem;
import com.example.gonine.bean.NoteItem;
import com.example.gonine.bean.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import lecho.lib.hellocharts.view.LineChartView;

public class PatientViewActivity extends AppCompatActivity {

    // for chart
    private LineChartView lcv_heartwave1;
    private LineChartView lcv_heartwave2;
    private LineChartView lcv_breath;
    private LineChartView lcv_blood;
    private Timer timer;

    // for firestore
    public static final String KEY_PATIENT_ID = "key_patient_id";
    private FirebaseFirestore mFirestore;
    private DocumentReference patientRef;
    private String patientId;
    // for firebase auth
    FirebaseAuth auth;

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

        initView();
        initFirestore();
    }

    private void initFirestore() {
        // for auth
        auth = FirebaseAuth.getInstance();

        // Get patient ID from extras
        patientId = getIntent().getExtras().getString(KEY_PATIENT_ID);
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
                intent.putExtra(PatientSpeechActivity.KEY_PATIENT_ID, patientId);
                startActivity(intent);
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
        Log.i("initPatient", p.getName());
        name.setText("【"+p.getID()+"】 "+p.getName());
        age.setText(String.valueOf(p.getAge()));
        gender.setText(p.getGender());

        // doctor advices & diagnosis
        bindNoteItem("doctor_advice");
        bindNoteItem("diagnosis");

        // basic information (for each type)
        bindDigitalItem("temperature");
        bindDigitalItem("blood_pressure");
        bindDigitalItem("heart_rate");
        bindDigitalItem("blood_oxygen");
        bindDigitalItem("respiratory_rate");

        patient_situ.setText("入院时间：2020年1月21日\n过往病史：糖尿病\n过敏史：N/A");


        // init graph
        Vector em = new Vector() ;
        MedicalDataItem heartwave1 = new MedicalDataItem("heartwave1",em) ;
        Vector<MedicalDataItem> use = new Vector<MedicalDataItem>() ;
        MedicalDataItem breathe = new MedicalDataItem("breathe",em) ;

        use.add(heartwave1) ;
        use.add(breathe) ;
        p.setAdvices(null);
        p.setData(use);

        // add line chart binding & setting
        Vector data = new Vector() ;
        medicalDataItem = new MedicalDataItem("breathe", data) ;

        lcv_heartwave1 = (LineChartView) findViewById(R.id.heartwave1_chart);
        lcv_breath = (LineChartView) findViewById(R.id.breath_chart);
        p.initChartAll(lcv_heartwave1,lcv_heartwave2,lcv_breath,lcv_blood);

    }

    private void bindDigitalItem(final String type) {
        Log.e("bind DigitalItem","start");
        patientRef.collection(type).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DigitalItem single = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        single = document.toObject(DigitalItem.class);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    if (single != null){
                        switch (type){
                            case "temperature": temperature.setText(String.valueOf(single.getValue()));break;
                            case "blood_pressure": pressure.setText(String.valueOf(single.getValue()));break;
                            case "heart_rate": heart_rate.setText(String.valueOf(single.getValue()));break;
                            case "blood_oxygen": oxygen.setText(String.valueOf(single.getValue()));break;
                            case "respiratory_rate": breathe_rate.setText(String.valueOf(single.getValue()));break;
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void bindNoteItem(final String type) {
        Log.e("bind NoteItem","start");
        patientRef.collection(type).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.e("bind NoteItem","1");
                        if (task.isSuccessful()) {
                            Log.e("bind NoteItem","2");
                            Vector<NoteItem> advices = new Vector<NoteItem>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                NoteItem single_advice = document.toObject(NoteItem.class);
                                //Log.e("advice/diagnosis",single_advice.getContent());
                                advices.add(single_advice);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            Log.e("bind NoteItem","3");
                            p.setAdvices(advices);
                            Vector<NoteItem> notes = p.getAdvices();
                            if(notes != null){
                                String info = null;
                                for(NoteItem item: notes){
                                    if(item.getContent()==null){
                                        continue;
                                    }
                                    if(info == null){
                                        info = item.getDoctorUserName() + " (" + item.getTime().toDate() + "):\n" + item.getContent();
                                    }
                                    else{
                                        info += "\n" + item.getDoctorUserName() + " (" + item.getTime().toDate() + "):\n" + item.getContent();
                                    }
                                }
                                if(type.equals("doctor_advice")){
                                    doctor_advice.setText(info);
                                }
                                else if(type.equals("diagnosis")){
                                    symptom.setText(info);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(timer == null){
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(p == null){
                    return;
                }
                //实时添加新的点
                p.displayAll(lcv_heartwave1,lcv_heartwave2,lcv_breath,lcv_blood) ;
            }
        }, 1000, 1000);

    }

}
