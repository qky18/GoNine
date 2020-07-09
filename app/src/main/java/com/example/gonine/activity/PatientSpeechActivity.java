package com.example.gonine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gonine.R;
import com.example.gonine.bean.DigitalItem;
import com.example.gonine.bean.NoteItem;
import com.example.gonine.bean.Patient;
import com.example.gonine.bean.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// speech to text使用

public class PatientSpeechActivity extends AppCompatActivity {

    // for binding widgets
    private ImageView pic;
    private TextView name, gender, age;
    private TextView speech_in, speech_formatted;

    // controller
    private Button btn_speak, btn_submit;
    private RadioGroup radio_group;
    private String presentRadio;

    // result
    private String diagnosis;           // 诊断结果
    private String advice;              // 医嘱
    private String basicInformation;    // 基本信息
    private Float temperature,blood_pressure,heart_rate,blood_oxygen,respiratory_rate;
    private Utils utils_firestore;                // 向firestore发送信息

    // for firestore
    public static final String KEY_PATIENT_ID = "key_patient_id";
    private FirebaseFirestore mFirestore;
    private DocumentReference patientRef;
    private Patient p;
    private String user_name;

    // for firebase auth
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognition);

        basicInformation = null;
        temperature=0.0f;
        blood_pressure=0.0f;
        heart_rate=0.0f;
        blood_oxygen=0.0f;
        respiratory_rate=0.0f;
        diagnosis = null;
        advice = null;
        utils_firestore=new Utils();
        user_name=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        initView();
        initFirestore();
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
                Log.i("patient speech object", p.getName());
                initPatient();
            }
        });
    }

    //获取界面控件
    private void initView() {
        Log.e("PatientSpeech", "init");
        Log.e("userName",user_name);
        pic = findViewById(R.id.patient_pic);
        name = findViewById(R.id.patient_name);
        gender = findViewById(R.id.patient_gender);
        age = findViewById(R.id.patient_age);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        //点击返回键
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PatientSpeechActivity.this.finish();
                    }
                });

        //speech recognition part start
        speech_in = findViewById(R.id.speech_input);
        speech_formatted = findViewById(R.id.speech_output);
        radio_group = findViewById(R.id.radio_group);
        //为单选按钮组添加事件监听
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                presentRadio = (String) rb.getText();
                Log.e("PatientSpeech", (String) rb.getText());
                speech_in.setText("请输入"+rb.getText());
                if((rb.getText()).equals("诊断结果")){
                    speech_formatted.setText(diagnosis);
                }
                else if((rb.getText()).equals("医嘱")) {
                    speech_formatted.setText(advice);
                }
                else{
                    speech_formatted.setText(basicInformation);
                }
            }
        });

        btn_speak = findViewById(R.id.btn_speak);
        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { getSpeechInput(v); }
        });

        //speech recognition part end
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onAddData(v); }
        });
    }

    private void initPatient() {
        if (p == null) {
            Log.e("patientView", "Not initializing patient");
            return;
        }

        // set patient info
        Log.i("initPatient", p.getName());
        name.setText(p.getName());
        age.setText(String.valueOf(p.getAge()));
        gender.setText(p.getGender());
    }


    // 获得语音输入
    public void getSpeechInput(View view){
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh");

        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,10);
        }
        else{
            Toast.makeText(this,"设备不支持语音输入",Toast.LENGTH_SHORT).show();
        }
    }

    // 获得转化结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 10:
                if(resultCode==RESULT_OK && data!=null){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speech_in.setText(result.get(0));
                    if(presentRadio.equals("诊断结果")){
                        if(diagnosis!=null) {
                            diagnosis = diagnosis + "/" + result.get(0);
                        }
                        else{
                            diagnosis=result.get(0);
                        }
                        speech_formatted.setText(diagnosis);
                    }
                    else if(presentRadio.equals("医嘱")){
                        if(advice!=null) {
                            advice = advice + "/" + result.get(0);
                        }
                        else{
                            advice=result.get(0);
                        }
                        speech_formatted.setText(advice);
                    }
                    else{
                        String raw_data=result.get(0);
                        //Log.e("substring",raw_data.substring(0,2));
                        //Log.e("has data",String.valueOf(raw_data.contains("体温")));
                        Pattern pattern=Pattern.compile("[^\\d+(\\.\\d+)?]");
                        Matcher matcher=pattern.matcher(raw_data);
                        String digital=matcher.replaceAll("");
                        Log.e("digital",digital);
                        if(raw_data.contains("体温")){ temperature=Float.parseFloat(digital); }
                        else if(raw_data.contains("血压")){ blood_pressure=Float.parseFloat(digital); }
                        else if(raw_data.contains("心率")){ heart_rate=Float.parseFloat(digital); }
                        else if(raw_data.contains("血氧")){ blood_oxygen=Float.parseFloat(digital); }
                        else if(raw_data.contains("呼吸率")){ respiratory_rate=Float.parseFloat(digital); }
                        //转换成输出格式
                        basicInformation="体温：";
                        if(temperature!=0.0f){ basicInformation+=(String.valueOf(temperature)+"°C"); }
                        basicInformation+="\n血压：";
                        if(blood_pressure!=0.0f){ basicInformation+=String.valueOf(blood_pressure); }
                        basicInformation+="\n心率：";
                        if(heart_rate!=0.0f){ basicInformation+=String.valueOf(heart_rate); }
                        basicInformation+="\n血氧：";
                        if(blood_oxygen!=0.0f){ basicInformation+=(String.valueOf(blood_oxygen)+"%"); }
                        basicInformation+="\n呼吸率：";
                        if(respiratory_rate!=0.0f){ basicInformation+=String.valueOf(respiratory_rate); }
                        speech_formatted.setText(basicInformation);

                    }
                }
                break;
        }
    }

    private void onAddData(View view){
        if(patientRef == null){
            return;
        }

        if(basicInformation != null){
            // convert speech text into number
            if(temperature!=0.0f){
                DigitalItem digit = new DigitalItem(Timestamp.now(), user_name, temperature);
                utils_firestore.addDigital(patientRef, digit, "temperature");
                temperature=0.0f;
            }
            if(blood_pressure!=0.0f){
                DigitalItem digit = new DigitalItem(Timestamp.now(), user_name, blood_pressure);
                utils_firestore.addDigital(patientRef, digit, "blood_pressure");
                blood_pressure=0.0f;
            }
            if(heart_rate!=0.0f){
                DigitalItem digit = new DigitalItem(Timestamp.now(), user_name, heart_rate);
                utils_firestore.addDigital(patientRef, digit, "heart_rate");
                heart_rate=0.0f;
            }
            if(blood_oxygen!=0.0f){
                DigitalItem digit = new DigitalItem(Timestamp.now(), user_name, blood_oxygen);
                utils_firestore.addDigital(patientRef, digit, "blood_oxygen");
                blood_oxygen=0.0f;
            }
            if(respiratory_rate!=0.0f){
                DigitalItem digit = new DigitalItem(Timestamp.now(), user_name, respiratory_rate);
                utils_firestore.addDigital(patientRef, digit, "respiratory_rate");
                respiratory_rate=0.0f;
            }
            basicInformation = null;
        }

        if(advice != null){
            Log.e("convert","converting advice");
            NoteItem note = new NoteItem(Timestamp.now(), user_name, advice);
            utils_firestore.addDoctorAdvice(patientRef, note);
            advice = null;
        }

        if(diagnosis != null){
            NoteItem note = new NoteItem(Timestamp.now(), user_name, advice);
            utils_firestore.addDiagnosis(patientRef, note);
            diagnosis = null;
        }
    }
}
