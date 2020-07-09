package com.example.gonine.bean;


import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Collections;
import java.util.Vector;

import lecho.lib.hellocharts.view.LineChartView;

@IgnoreExtraProperties
public class Patient {
    private int ID ;
    private String name ;
    private String gender ;
    private Severity severity ;
    private int photoID ;
    private int age ;


    private Vector<NoteItem> diagnosis = null;
    private Vector<NoteItem> advices = null;
    private Vector<MedicalDataItem> data = null;

    public Patient(){}

    public Patient(int i, String n, String g, Severity s, int p, int a){
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

    public Severity getSeverity() {
        return severity;
    }

    public int getPhotoResID() {
        return photoID;
    }

    public Vector<MedicalDataItem> getMedicalData() {
        return data;
    }

    public Vector<NoteItem> getAdvices() {
        return advices;
    }

    public Vector<NoteItem> getDiagnosis() {
        return diagnosis;
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

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public void setPhotoResID(int photoResID) {
        this.photoID = photoResID;
    }

    public void setData(Vector<MedicalDataItem> data) {
        this.data = data;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAdvices(Vector<NoteItem> advices) {
        this.advices = advices;
        if(advices != null){
            Collections.sort(this.advices);
        }
    }

    public void setDiagnosis(Vector<NoteItem> diagnosis) {
        this.diagnosis = diagnosis;
        if(diagnosis != null) {
            Collections.sort(this.diagnosis);
        }
    }

    public void addAdvices(NoteItem da){
        this.advices.add(da) ;
    }

    public void addAdvices(int index, NoteItem da){
        this.advices.add(index, da) ;
    }

    public void addDiagnosis(NoteItem diag) {
        this.diagnosis.add(diag);
    }

    public void removeAdvices(int index){
        this.advices.remove(index) ;
    }

    public void clearAdvices(){
        this.advices.clear() ;
    }

    public void addData(MedicalDataItem m){
        this.data.add(m) ;
    }

    public void addData(int index, MedicalDataItem m){
        this.data.add(index,m) ;
    }

    public void removeData(int index){
        this.data.remove(index) ;
    }
    public void clearData(){
        this.data.clear() ;
    }

    private void update(){
        //TODO for firebase:同步数据，可以直接使用medicalItem的update接口，display已经自带更新
        for(int i = 0; i < data.size(); i++){
            MedicalDataItem temp = (MedicalDataItem)data.toArray()[i] ;
            temp.update();
        }
    }



    public void initChartAll(LineChartView lcv_heart,LineChartView lcv_heart2,LineChartView lcv_breath, LineChartView lcv_blood){ //display自带更新
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

    public void displayAll(LineChartView lcv_heart,LineChartView lcv_heart2,LineChartView lcv_breath, LineChartView lcv_blood){//display自带更新
        if(data == null){
            return;
        }
        for(int i = 0; i < data.size(); i++){
            MedicalDataItem temp = (MedicalDataItem) data.toArray()[i] ;
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
        }
    }
}




