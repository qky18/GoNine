package com.example.gonine.bean;


import java.util.Vector;

import lecho.lib.hellocharts.view.LineChartView;

public class Patient {
    private int ID ;
    private String name ;
    private String gender ;
    private String severity ;
    private int photoID  ;
    private Vector<DoctorAdvice> advices ;
    private Vector<MedicalDataItem> data ;
    private int age ;

    public Patient(int i, String n, String g, String s, int p, int a){
        this.ID = i ;
        this.name = n ;
        this.gender = g ;
        this.severity = s ;
        this.photoID = p ;
        this.age = a ;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getSeverity() {
        return severity;
    }

    public int getPhotoResID() {
        return photoID;
    }

    public Vector<DoctorAdvice> getDoctor_advices() {
        return advices;
    }

    public Vector<MedicalDataItem> getMedical_data() {
        return data;
    }

    public int getAge() {
        return age;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setPhotoResID(int photoResID) {
        this.photoID = photoResID;
    }

    public void setAdvices(Vector<DoctorAdvice> advices) {
        this.advices = advices;
    }

    public void setData(Vector<MedicalDataItem> data) {
        this.data = data;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void add_advices(DoctorAdvice da){
        this.advices.add(da) ;
    }

    public void add_advices(int index,DoctorAdvice da){
        this.advices.add(index,da) ;
    }

    public void remove_advices(int index){
        this.advices.remove(index) ;
    }

    public void clear_advices(){
        this.advices.clear() ;
    }

    public void add_data(MedicalDataItem m){
        this.data.add(m) ;
    }

    public void add_data(int index, MedicalDataItem m){
        this.data.add(index,m) ;
    }

    public void remove_data(int index){
        this.data.remove(index) ;
    }
    public void clear_data(){
        this.data.clear() ;
    }

    private void update(){
        //TODO for firebase:同步数据，可以直接使用medicalItem的update接口，display已经自带更新
        for(int i = 0; i < data.size(); i++){
            MedicalDataItem temp = (MedicalDataItem)data.toArray()[i] ;
            temp.update();
        }
    }


    public void chartinit_all(LineChartView lcv_heart,LineChartView lcv_heart2,LineChartView lcv_breath, LineChartView lcv_blood){//display自带更新
        for(int i = 0; i < data.size(); i++){
            MedicalDataItem temp = (MedicalDataItem)data.toArray()[i] ;
            if(temp.getName() == "heartwave1" ){
                temp.chartinit(lcv_heart);
            }
            else if(temp.getName() == "heartwave2"){
                temp.chartinit(lcv_heart2);
            }
            else if(temp.getName() == "breathe"){
                temp.chartinit(lcv_breath);
            }
            else if(temp.getName() == "blood"){
                temp.chartinit(lcv_blood);
            }
            else continue ;
        }
    }

    public void display_all(LineChartView lcv_heart,LineChartView lcv_heart2,LineChartView lcv_breath, LineChartView lcv_blood){//display自带更新
        for(int i = 0; i < data.size(); i++){
            MedicalDataItem temp = (MedicalDataItem)data.toArray()[i] ;
            if(temp.getName() == "heartwave1" ){
                temp.display(lcv_heart);
            }
            else if(temp.getName() == "heartwave2"){
                temp.display(lcv_heart2);
            }
            else if(temp.getName() == "breathe"){
                temp.display(lcv_breath);
            }
            else if(temp.getName() == "blood"){
                temp.display(lcv_blood);
            }
            else continue ;
        }
    }
}




