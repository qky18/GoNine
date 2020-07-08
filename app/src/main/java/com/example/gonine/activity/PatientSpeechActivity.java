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
import com.example.gonine.bean.NoteItem;
import com.example.gonine.bean.Patient;
import com.example.gonine.bean.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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
    private Utils utils;                // 向firestore发送信息

    // for firestore
    public static final String KEY_PATIENT_ID = "key_patient_id";
    private FirebaseFirestore mFirestore;
    private DocumentReference patientRef;
    private Patient p;

    // for firebase auth
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognition);
        //Intent intent=getIntent();
        //p=(Patient)intent.getSerializableExtra("patient");

        //设置此界面为横屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        basicInformation = null;
        diagnosis = null;
        advice = null;

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
                Log.e("patient speech", "init");
                p = documentSnapshot.toObject(Patient.class);
                assert p != null;
                //TODO: crash if not commented
                /*
                Log.i("patient speech object", p.getName());
                initPatient();
                 */
            }
        });
    }

    //获取界面控件
    private void initView() {
        Log.e("PatientSpeech", "init");
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
                        diagnosis=diagnosis+"/"+result.get(0);
                        speech_formatted.setText(diagnosis);
                    }
                    else if(presentRadio.equals("医嘱")){
                        advice=advice+"/"+result.get(0);
                        speech_formatted.setText(advice);
                    }
                    else{
                        basicInformation=basicInformation+"/"+result.get(0);
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

        // TODO: test if data successfully sent
        if(basicInformation != null){
            // TODO: convert speech text into number
            /*
            float val = 0.f;
            String type = "temperature";
            DigitalItem digit = new DigitalItem(null, null, val);
            utils.addDigital(patientRef, digit, type);
             */

            basicInformation = null;
        }

        if(advice != null){
            // TODO: add firebase timestamp usage & doctor info
            //NoteItem note = new NoteItem(FieldValue.serverTimestamp(), null, advice);
            NoteItem note = new NoteItem(null, null, advice);
            utils.addDoctorAdvice(patientRef, note);
            advice = null;
        }

        if(diagnosis != null){
            // TODO: add firebase timestamp usage & doctor info
            NoteItem note = new NoteItem(null, null, advice);
            utils.addDiagnosis(patientRef, note);
            diagnosis = null;
        }
    }
}
