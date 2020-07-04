package com.example.gonine.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.gonine.MainActivity;
import com.example.gonine.R;
import com.example.gonine.bean.Gender;
import com.example.gonine.bean.MedicalDataItem;
import com.example.gonine.bean.Patient;
import com.example.gonine.bean.Severity;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class PatientViewActivity extends AppCompatActivity {
    // TODO: add lineChart View
    //for chart
    private LineChartView lcv_heartwave1;
    private LineChartView lcv_heartwave2 ;
    private LineChartView lcv_breath ;
    private LineChartView lcv_blood ;
    private Timer timer;
/*
   // private LineChartView heart_mChartView;//工具人
   // private List<PointValue> heart_values;//存画出来的数值,感觉没啥用
  //  private List<Line> heart_lines;//感觉没啥用

    private LineChartData lineChartData;//工具人，可能打包

    private List<Line> linesList;//线的集合，打包
    private List<PointValue> pointValueList;//点的集合
  //  private List<PointValue> points;//感觉没啥用
    private int position = 0;
    private Timer timer;
  //  private boolean isFinish = true;
    private Axis axisY, axisX;
    private Random random = new Random();


 */
    //
    private ImageButton btn_add_info;
    private ImageView pic;
    private TextView name, gender, age;
    private TextView temperature, pressure, heart_rate, oxygen, breathe_rate;
    private TextView patient_situ, symptom, doctor_advice;

    // for firebase auth
    FirebaseAuth auth;

    //封装好的display
    private MedicalDataItem medicalDataItem ;

    private Patient p ;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        //设置此界面为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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

        //init patient
        initp() ;


        //TODO: set patient info
        //pic.setBackground(xxx);
        patient_situ.setText("入院时间：2020年1月21日\n过往病史：糖尿病\n过敏史：N/A");

        //TODO: add line chart binding & setting
        Vector data = new Vector() ;
        medicalDataItem = new MedicalDataItem("breathe",data) ;

        lcv_heartwave1 = (LineChartView) findViewById(R.id.heartwave1_chart);
        lcv_breath = (LineChartView) findViewById(R.id.breath_chart);
        p.chartinit_all(lcv_heartwave1,lcv_heartwave2,lcv_breath,lcv_blood);
        //medicalDataItem.chartinit(lineChartVie);
/*
        pointValueList = new ArrayList<>();
        linesList = new ArrayList<>();




        //初始化坐标轴
        axisY = new Axis();
        //添加坐标轴的名称
        axisY.setLineColor(Color.parseColor("#aab2bd"));
        axisY.setTextColor(Color.parseColor("#aab2bd"));
        axisX = new Axis();
        axisX.setLineColor(Color.parseColor("#aab2bd"));
        lineChartData = initDatas(null);
        lineChartView.setLineChartData(lineChartData);

        Viewport port = initViewPort(0, 50);
        lineChartView.setCurrentViewportWithAnimation(port);
        lineChartView.setInteractive(true);
        lineChartView.setScrollEnabled(true);
        lineChartView.setValueTouchEnabled(true);
        lineChartView.setFocusableInTouchMode(true);
        lineChartView.setViewportCalculationEnabled(false);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.startDataAnimation();
    //    points = new ArrayList<>();
        timer = new Timer();
        //



 */

        timer = new Timer();
    }


    @Override
    protected void onResume() {
        super.onResume();

        timer.schedule(new TimerTask() {
            @Override

            public void run() {
                //实时添加新的点
                p.display_all(lcv_heartwave1,lcv_heartwave2,lcv_breath,lcv_blood) ;
               // medicalDataItem.display(lineChartView);
/*
                PointValue value1 = new PointValue(position * 5, random.nextInt(100) + 40);
                value1.setLabel("00:00");
                pointValueList.add(value1);

                float x = value1.getX();
                //根据新的点的集合画出新的线
                Line line = new Line(pointValueList);
                line.setColor(Color.RED);
                line.setShape(ValueShape.CIRCLE);
                line.setCubic(true);//曲线是否平滑，即是曲线还是折线

                linesList.clear();
                linesList.add(line);
                lineChartData = initDatas(linesList);
                lineChartView.setLineChartData(lineChartData);
                //根据点的横坐实时变幻坐标的视图范围
                Viewport port;
                if (x > 50) {
                    port = initViewPort(x - 50, x);
                } else {
                    port = initViewPort(0, 50);
                }
                lineChartView.setCurrentViewport(port);//当前窗口

               //Viewport maPort = initMaxViewPort(x);
               lineChartView.setMaximumViewport(port);//最大窗口

                position++;

         */
            }
        }, 1000, 1000);


    }
    private void initp(){
        //TODO for firebase or adapter?:p初始化
        Vector em = new Vector() ;
        MedicalDataItem heartwave1 = new MedicalDataItem("heartwave1",em) ;
        Vector<MedicalDataItem> use = new Vector<MedicalDataItem>() ;
        MedicalDataItem breathe = new MedicalDataItem("breathe",em) ;

        use.add(heartwave1) ;
        use.add(breathe) ;
        this.p = new Patient(1,"name", "male", "serious",0,0) ;
        this.p.setAdvices(null);
        this.p.setData(use);
    }

/*
    private LineChartData initDatas(List<Line> lines) {
        LineChartData data = new LineChartData(lines);
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        return data;
    }

    private Viewport initViewPort(float left, float right) {
        Viewport port = new Viewport();
        port.top = 150;
        port.bottom = 0;
        port.left = left;
        port.right = right;
        return port;
    }
    private Viewport initMaxViewPort(float right) {
        Viewport port = new Viewport();
        port.top = 150;
        port.bottom = 0;
        port.left = 0;
        port.right = right + 50;
        return port;
    }
    */

}
