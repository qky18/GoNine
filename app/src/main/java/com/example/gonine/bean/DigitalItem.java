package com.example.gonine.bean;

import com.google.firebase.Timestamp;

public class DigitalItem {
    private Timestamp time;//用的firebase时间戳
    private String doctor_user_name ;
    private float value;

    public DigitalItem(Timestamp _timestamp, String _doctor_user_name, float _value){
        this.time = _timestamp ;
        this.doctor_user_name = _doctor_user_name;
        this.value = _value;
    }
    public Timestamp getTime() {
        return time;
    }

    public String getDoctorUserName() {
        return this.doctor_user_name;
    }

    public float getValue() {
        return value;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setDoctor_id(String _doctor_user_name) {
        this.doctor_user_name = _doctor_user_name;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
