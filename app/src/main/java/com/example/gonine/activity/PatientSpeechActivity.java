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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gonine.R;
import com.google.firebase.auth.FirebaseAuth;

//speech to text相关
import android.speech.RecognizerIntent;
import android.widget.Toast;

import java.util.ArrayList;

public class PatientSpeechActivity extends AppCompatActivity {
    // TODO: add lineChart View
    private ImageView pic;
    private TextView name, gender, age;
    private TextView speech_in, speech_formatted;
    private Button btn_speak, btn_submit;
    private RadioGroup radio_group;

    // for firebase auth
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognition);
        //设置此界面为横屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //上面这句话我这边报错了
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

        radio_group=findViewById(R.id.radio_group);
        //为单选按钮组添加事件监听
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton) findViewById(checkedId);
                if(rb.getText()=="基本信息"){
                    speech_in.setText("");
                }
            }
        });

        btn_speak = findViewById(R.id.btn_speak);
        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {getSpeechInput(v);}
        });

        btn_submit = findViewById(R.id.btn_submit);
        //TODO: set patient info
        //e.g. pic.setBackground(xxx);
        name.setText("002 Jennifer Kal");
    }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 10:
                if(resultCode==RESULT_OK && data!=null){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speech_in.setText(result.get(0));
                }
                break;
        }
    }
}
