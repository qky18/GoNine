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

import com.example.gonine.MainActivity;
import com.example.gonine.R;
import com.google.firebase.auth.FirebaseAuth;

public class PatientViewActivity extends AppCompatActivity {
    // TODO: add lineChart View
    private ImageButton btn_add_info;
    private ImageView pic;
    private TextView name, gender, age;
    private TextView temperature, pressure, heart_rate, oxygen, breathe_rate;
    private TextView patient_situ, symptom, doctor_advice;

    // for firebase auth
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        //设置此界面为横屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        auth = FirebaseAuth.getInstance();

        init();
    }

    //获取界面控件
    private void init() {
        Log.e("PatientView", "init");
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

        //TODO: set patient info
        //pic.setBackground(xxx);
        patient_situ.setText("入院时间：2020年1月21日\n过往病史：糖尿病\n过敏史：N/A");

        //TODO: add line chart binding & setting

    }
}
