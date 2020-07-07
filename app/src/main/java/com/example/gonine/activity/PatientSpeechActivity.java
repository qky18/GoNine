package com.example.gonine.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

//speech to text使用
import androidx.annotation.Nullable;
import android.speech.RecognizerIntent;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gonine.R;
import com.example.gonine.bean.Patient;
import com.google.firebase.auth.FirebaseAuth;

public class PatientSpeechActivity extends AppCompatActivity {
    // TODO: add lineChart View
    private ImageView pic;
    private TextView name, gender, age;
    private TextView speech_in, speech_formatted;
    private Button btn_speak, btn_submit;
    private RadioGroup radio_group;
    private String presentRadio;
    private String diagnosis;//诊断结果
    private String advice;//医嘱
    private String basicInformation;//基本信息

    private Patient p;
    // for firebase auth
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognition);
        Intent intent=getIntent();
        p=(Patient)intent.getSerializableExtra("patient");
        //设置此界面为横屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        diagnosis="";
        advice="";
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
        radio_group=findViewById(R.id.radio_group);
        //为单选按钮组添加事件监听
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb= findViewById(checkedId);
                presentRadio= (String) rb.getText();
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
            public void onClick(View v) {getSpeechInput(v);}
        });

        btn_submit = findViewById(R.id.btn_submit);
        //speech recognition part end

        //TODO: set patient info
        //e.g. pic.setBackground(xxx);
        name.setText("002 Jennifer Kal");

        //TODO: add Google Cloud speech recognition

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
                        diagnosis=diagnosis+" "+result.get(0);
                        speech_formatted.setText(diagnosis);
                    }
                    else if(presentRadio.equals("医嘱")){
                        advice=advice+" "+result.get(0);
                        speech_formatted.setText(advice);
                    }
                    else{
                        basicInformation=basicInformation+" "+result.get(0);
                        speech_formatted.setText(basicInformation);
                    }
                }
                break;
        }
    }
}
