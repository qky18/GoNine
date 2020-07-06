package com.example.gonine.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gonine.R;
import com.example.gonine.bean.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

public class PatientSpeechActivity extends AppCompatActivity {
    // TODO: add lineChart View
    private ImageView pic;
    private TextView name, gender, age;
    private TextView speech_in, speech_formatted;
    private Button btn_speak;

    // for firebase auth
    FirebaseAuth auth;

    // TODO: 通过Intent传入patientId来初始化patientRef
    private DocumentReference patientRef;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognition);
        //设置此界面为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        auth = FirebaseAuth.getInstance();

        init();
    }


    //获取界面控件
    private void init() {
        Log.e("PatientSpeech", "init");
        pic = findViewById(R.id.patient_pic);
        name = findViewById(R.id.patient_name);
        gender = findViewById(R.id.patient_gender);
        age = findViewById(R.id.patient_age);
        speech_in = findViewById(R.id.speech_input);
        speech_formatted = findViewById(R.id.speech_output);
        btn_speak = findViewById(R.id.btn_speak);
        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: reaction to click

            }
        });

        //TODO: set patient info
        //e.g. pic.setBackground(xxx);
        name.setText("002 Jennifer Kal");

        //TODO: add Google Cloud speech recognition

    }
}
