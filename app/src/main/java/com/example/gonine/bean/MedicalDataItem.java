package com.example.gonine.bean;

import android.graphics.Color;
import android.util.Pair;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MedicalDataItem {
    private String name ;
    private Vector<Pair<Timestamp,Float>> data ;
    //classes for chart
    private LineChartData lineChartData;//工具人，可能打包
    private List<Line> linesList;//线的集合，打包
    private List<PointValue> pointValueList;//点的集合
    private int position = 0;

    //  private boolean isFinish = true;
    private Axis axisY, axisX;
    private Random random = new Random();
    //


    public MedicalDataItem(String n, Vector<Pair<Timestamp,Float>> d){
        this.name = n ;
        this.data = d ;
    }
    public String getName() {
        return name;
    }

    public  Vector<Pair<Timestamp,Float>> getData() {
        return data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData( Vector<Pair<Timestamp,Float>> data) {
        this.data = data;
    }

    public void add_data(Pair<Timestamp,Float> n){
        this.data.add(n);
    }
    public void add_data(int index, Pair<Timestamp,Float> m){
        this.data.add(index,m) ;
    }

    public void remove_data(int index){
        this.data.remove(index) ;
    }
    public void clear_notes(){
        this.data.clear() ;
    }

    public void update(){
        //TODO for firebase:从firebase中获取数据，进行vector的更新，不过保证vector的项数不会无限递增（比如获取最新的n项，例子中随机初始化一个十项的vector，随后每次添加一项）
        //TODO for firebase:目前采取但比较粗糙的做法是，每次展示vector中的所有元素（所以需要保证vector中元素数量恒定），其他做法可以再调整代码（比如加载一百项，但只展示十项，可以左右滑动查看etc）
        //目前随机生成十项

        if(data.size() == 0){
            for(int i = 1; i <= 10 ; i++) {
                Pair<Timestamp,Float> tmp = new Pair<Timestamp, Float>(null, (float)random.nextInt(100) + 40) ;
                data.add(tmp) ;
            }
        }
        else{
            data.remove(0) ;
            Pair<Timestamp,Float> tmp = new Pair<Timestamp, Float>(null, (float)random.nextInt(100) + 40) ;
            data.add(tmp) ;
        }

    }

    public void chartinit(LineChartView lineChartView){

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

        //

    }
    public void display(LineChartView lineChartView){//心电波形
        this.update();//TODO for firebase：可以在这里进行也可以分开，看firebase需要
        //TODO for optional:设置触碰时停止更新
        pointValueList.clear() ;
        linesList.clear() ;
        for(int i = 0 ; i < data.size() ; i++){
            Pair<Timestamp, Float> tmp =(Pair<Timestamp, Float>)data.toArray()[i];
            PointValue value = new PointValue(i*50/(data.size()-1),tmp.second);
            pointValueList.add(value) ;
        }
        /*
        PointValue value1 = new PointValue(position * 5, random.nextInt(100) + 40);
        value1.setLabel("00:00");
        pointValueList.add(value1);
*/
        //float x = value1.getX();
        //根据新的点的集合画出新的线
        Line line = new Line(pointValueList);
        setline(line) ;

        linesList.clear();
        linesList.add(line);
        lineChartData = initDatas(linesList);
        lineChartView.setLineChartData(lineChartData);
        //根据点的横坐实时变幻坐标的视图范围
        Viewport port = initViewPort(0,50);//根据数据具体调整，比如可以多设置一些点然后左右拖动查看

        lineChartView.setCurrentViewport(port);//当前窗口

        //Viewport maPort = initMaxViewPort(x);
        lineChartView.setMaximumViewport(port);//最大窗口

        //position++;
    }
    private LineChartData initDatas(List<Line> lines) {
        LineChartData data = new LineChartData(lines);
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        return data;
    }

    private Viewport initViewPort(float left, float right) {
        Viewport port = new Viewport();
        port.top = 150;//TODO：目前150是最大值，可以修改
        port.bottom = 0;
        port.left = left;
        port.right = right;
        return port;
    }
    private void setline(Line line){
        if(name == "heartwave1" || name == "heartwave2"){//对应图上的心电波形和心电级联波形
            line.setColor(Color.RED);
            line.setHasPoints(false);//是否显示圆点 ，如果为false则没有原点只有点显示（每个数据点都是个大圆点）
            //line.setShape(ValueShape.CIRCLE);
            line.setCubic(false);//曲线是否平滑，即是曲线还是折线
            line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据,设置了line.setHasLabels(true);之后点击无效
        }
        else if(name == "breathe" || name == "blood"){//对应呼吸与血氧容积
            line.setColor(Color.BLUE);
            line.setHasPoints(true);//是否显示圆点 ，如果为false则没有原点只有点显示（每个数据点都是个大圆点）
            //line.setShape(ValueShape.CIRCLE);
            line.setCubic(true);//曲线是否平滑，即是曲线还是折线
            line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据,设置了line.setHasLabels(true);之后点击无效
            line.setFilled(true) ;
        }
        else{
            //报错
        }
    }
}
